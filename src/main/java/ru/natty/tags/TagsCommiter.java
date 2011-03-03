/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.tags;

import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;
import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.io.TextEncoding;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import ru.natty.persist.Album;
import ru.natty.persist.Artist;
import ru.natty.persist.Genre;
import ru.natty.persist.Track;

/**
 *
 * @author zayac
 */
public class TagsCommiter {
//    private Map<String, ID3V2Tag> tagCollection = null;
//    private HashMap<String, Genre> genres = null;
//    private HashMap<String, Artist> artists= null;
//    private HashMap<String, Album> albums = null;
//    private HashMap<String, Track> tracks = null;

    private EntityManagerFactory emf = null;
    private EntityManager em = null;
    
    private final static Logger log = Logger.getLogger(TagsCommiter.class);
    
    public TagsCommiter()
    {
        emf = Persistence.createEntityManagerFactory("natty");
        em = emf.createEntityManager();
        //org.blinkenlights.jid3.io.TextEncoding.setDefaultTextEncoding(TextEncoding.ISO_8859_1);
    }

    public Track findTrack(String name)
    {
        try{
            return (Track) em.createNamedQuery("Track.findByName").setParameter("name", name).getSingleResult();
        }catch(NoResultException ex)
        {
            return null;
        }
    }

    public Artist findArtist(String name)
    {
        try{
            return (Artist) em.createNamedQuery("Artist.findByName").setParameter("name", name).getSingleResult();
        }catch(NoResultException ex)
        {
            return null;
        }
    }

    public Album findAlbum(String name)
    {
        try{
            return (Album) em.createNamedQuery("Album.findByName").setParameter("name", name).getSingleResult();
        }catch(NoResultException ex)
        {
            return null;
        }
    }

    public Genre findGenre(String name)
    {
        try{
            return (Genre) em.createNamedQuery("Genre.findByName").setParameter("name", name).getSingleResult();
        }catch(NoResultException ex)
        {
            return null;
        }
    }
    
    public void commit(String path, ID3V2Tag tag)
    {
        boolean trackExists = true;
        boolean artistExists = true;
        boolean albumExists = true;
        boolean genreExists = true;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(tag.getYear(), 0, 1);
            
            Track tr;
            if ((tr = findTrack(tag.getTitle())) == null) {
                tr = new Track();
                tr.setName(tag.getTitle());
                tr.setUrl(path);
                tr.setYear(calendar.getTime());
                trackExists = false;
            }
            // Fill Track Fields
            Artist art;
            if ((art = findArtist(tag.getArtist())) == null) {
                art = new Artist();
                art.setName(tag.getArtist());
                artistExists = false;
            }
            // Fill Album Fields
            Album alb;
            if ((alb = findAlbum(tag.getAlbum())) == null)
            {
                alb = new Album();
                alb.setName(tag.getAlbum());
                alb.setYear(calendar.getTime());
                albumExists = false;
            }
            //Fill Genre Fields
            Genre gen;
            if ((gen = findGenre(tag.getGenre())) == null)
            {
                gen = new Genre();
                gen.setName(tag.getGenre());
            }
            em.getTransaction().begin();
            if (tr.getName() != null) em.persist(tr);
            if (art.getName() != null) em.persist(art);
            if (alb.getName() != null) em.persist(alb);
            if (gen.getName() != null) em.persist(gen);

            //Fill links
            if ((art.getName() != null && gen.getName() != null) && (!artistExists || !genreExists)) art.getGenreCollection().add(gen);
            if (alb.getName() != null && gen.getName() != null && (!albumExists || !genreExists)) alb.getGenreCollection().add(gen);
            if (tr.getName() != null && alb.getName() != null && (!trackExists || !albumExists)) alb.getTrackCollection().add(tr);
            if (tr.getName() != null && art.getName() != null && (!trackExists || !artistExists)) art.getTrackCollection().add(tr);
            if (tr.getName() != null && gen.getName() != null && (!trackExists || !genreExists)) gen.getTrackCollection().add(tr);
            em.getTransaction().commit();
            
            log.debug("Artist: " + tag.getArtist() + " Title: " + tag.getTitle() + " Album: " + tag.getAlbum() + " Year: " + tag.getYear() + " Genre: " + tag.getGenre() + " added successfully");


        } catch (ID3Exception ex) {
            log.error(path + " " + ex);
        } catch (NullPointerException ex) {
            log.debug(path + " is not a music file. " + ex);
        }
    }

    public void close()
    {
        em.close();
        emf.close();
    }
}
