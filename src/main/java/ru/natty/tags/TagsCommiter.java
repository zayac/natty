/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.tags;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;
import org.blinkenlights.jid3.ID3Exception;
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
    private Map<String, ID3V2Tag> tagCollection = null;
    private HashMap<String, Genre> genres = null;
    private HashMap<String, Artist> artists= null;
    private HashMap<String, Album> albums = null;
    private HashMap<String, Track> tracks = null;

    private EntityManagerFactory emf = null;
    private EntityManager em = null;
    
    private final static Logger log = Logger.getLogger(TagsCommiter.class);
    

    public TagsCommiter(Map<String, ID3V2Tag> tagCollection)
    {
        this.tagCollection = tagCollection;
        genres = new HashMap<String, Genre>();
        artists = new HashMap<String, Artist>();
        albums = new HashMap<String, Album>();
        tracks = new HashMap<String, Track>();

        emf = Persistence.createEntityManagerFactory("natty");
        em = emf.createEntityManager();
    }

    public void commit()
    {
        Iterator iter = tagCollection.entrySet().iterator();
        Calendar calendar = Calendar.getInstance();
        while (iter.hasNext())
        {
            Map.Entry<String, ID3V2Tag> entry = (Map.Entry) iter.next();
            try {
                ID3V2Tag tag = entry.getValue();
                calendar.set(tag.getYear(), 0, 1);
                // Fill Track Fields
                Track tr;
                if (!tracks.containsKey(tag.getTitle()))
                {
                    tr = new Track();
                    tr.setName(tag.getTitle());
                    tr.setUrl(entry.getKey());
                    tr.setYear(calendar.getTime());
                }else
                {
                    tr = tracks.get(tag.getTitle());
                }

                // Fill Artist Fields
                Artist art;
                if (!artists.containsKey(tag.getArtist()))
                {
                    art = new Artist();
                    art.setName(tag.getArtist());
                } else
                {
                    art = artists.get(tag.getArtist());
                }

                // Fill Album Fields
                Album alb;
                if (!albums.containsKey(tag.getAlbum()))
                {
                    alb = new Album();
                    alb.setName(tag.getAlbum());
                    alb.setYear(calendar.getTime());
                } else
                {
                    alb = albums.get(tag.getAlbum());
                }

                //Fill Genre Fields
                Genre gen;
                if (!genres.containsKey(tag.getGenre()))
                {
                    gen = new Genre();
                    gen.setName(tag.getGenre());
                } else
                {
                    gen = genres.get(tag.getGenre());
                }

                em.getTransaction().begin();
                if (tr.getName() != null) em.persist(tr);
                if (art.getName() != null) em.persist(art);
                if (alb.getName() != null) em.persist(alb);
                if (gen.getName() != null) em.persist(gen);
                
                em.flush();
                //Fill links      
                if ((art.getName() != null && gen.getName() != null) && (!artists.containsKey(art.getName()) || !genres.containsKey(gen.getName()))) art.getGenreCollection().add(gen);
                if (alb.getName() != null && gen.getName() != null && (!albums.containsKey(alb.getName()) || !genres.containsKey(gen.getName()))) alb.getGenreCollection().add(gen);
                if (tr.getName() != null && alb.getName() != null && (!tracks.containsKey(tr.getName()) || !albums.containsKey(alb.getName()))) alb.getTrackCollection().add(tr);
                if (tr.getName() != null && art.getName() != null && (!tracks.containsKey(tr.getName()) || !artists.containsKey(art.getName()))) art.getTrackCollection().add(tr);
                if (tr.getName() != null && gen.getName() != null && (!tracks.containsKey(art.getName()) || !genres.containsKey(gen.getName()))) gen.getTrackCollection().add(tr);
               
                em.getTransaction().commit();
                log.debug("Artist: " + tag.getArtist() + " Title: " + tag.getTitle() + " Album: " + tag.getAlbum() + " Year: " + tag.getYear() + " Genre: " + tag.getGenre() + " added successfully");

                artists.put(tag.getArtist(), art);
                tracks.put(tag.getTitle(), tr);
                albums.put(tag.getAlbum(), alb);
                genres.put(tag.getGenre(), gen);
            } catch (ID3Exception ex) {
                log.error(ex.getMessage());
            } catch (NullPointerException ex) {
                log.error("Can't parse file " + entry.getKey());
            }
        }
    }

    public void close()
    {
        em.close();
        emf.close();
    }
}
