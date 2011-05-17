/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.tags;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import javax.persistence.*;
import org.apache.log4j.Logger;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import ru.natty.persist.Album;
import ru.natty.persist.Artist;
import ru.natty.persist.Genre;
import ru.natty.persist.Track;

/**
 *
 * @author zayac
 */
public class TagsCommiter {
    private EntityManagerFactory emf = null;
    private EntityManager em = null;
    private final static Logger log = Logger.getLogger(TagsCommiter.class);
    //private LinkedList<MusicFileCollection> fileCollection = null;
    private HashMap<String, Track>trackCollection = null;
    private HashMap<String, Genre>genreCollection = null;
    private HashMap<String, Artist>artistCollection = null;
    private HashMap<String, Album>albumCollection = null;
    private final static int BUFFER_SIZE = 1000;
    protected TagsCommiter()
    {
        emf = Persistence.createEntityManagerFactory("natty");
        em = emf.createEntityManager();
        trackCollection = new HashMap<String, Track>();
        genreCollection = new HashMap<String, Genre>();
        artistCollection = new HashMap<String, Artist>();
        albumCollection = new HashMap<String, Album>();
    }

    private static class SingletonHolder
    {
        public static TagsCommiter instance = new TagsCommiter();
    }

    public static TagsCommiter getInstance()
    {
        return SingletonHolder.instance;
    }

    public Track findTrack(Track tr)
    {
        String trName = tr.getName();
        try{
            List<Track> list = em.createNamedQuery("Track.findByName").setParameter("name", trName.replaceAll("\u0000", "")).getResultList();
            if (list == null || list.isEmpty())
                return null;
            return list.get(0);
        } catch(NoResultException ex)
        {
            return null;
        }
    }

    public Artist findArtist(Artist art)
    {
        String artName = art.getName();
        try{
            List<Artist> list = em.createNamedQuery("Artist.findByName").setParameter("name", artName.replaceAll("\u0000", "")).getResultList();
            if (list == null || list.isEmpty())
                return null;
            return list.get(0);
        }catch(NoResultException ex)
        {
            return null;
        }
    }

    public Album findAlbum(Album alb)
    {
        String albName = alb.getName();
        try{
            List<Album> list = em.createNamedQuery("Album.findByName").setParameter("name", albName.replaceAll("\u0000", "")).getResultList();
            if (list == null || list.isEmpty())
                return null;
            return list.get(0);
        }catch(NoResultException ex)
        {
            return null;
        }
    }

    public Genre findGenre(Genre gen)
    {
        String genName = gen.getName();
        try{
            List<Genre> list = em.createNamedQuery("Genre.findByName").setParameter("name", genName.replaceAll("\u0000", "")).getResultList();
            if (list == null || list.isEmpty())
            {
                return null;
            }
            return list.get(0);
        }catch(NoResultException ex)
        {
            return null;
        }
    }

    public Integer getYear(byte[] raw)
    {
        if(raw.length >= 20)
        {
            String source = new String(raw);
            String ret = "";
            ret += source.charAt(13);
            ret += source.charAt(15);
            ret += source.charAt(17);
            ret += source.charAt(19);
            return Integer.parseInt(ret);
        }
        return null;
    }
    
    public void commit(String path, Tag tag)
    {
        Logger.getLogger("org.jaudiotagger.tag").setLevel(org.apache.log4j.Level.OFF);
        Date year = null;
        Boolean trackExists = false;
        Boolean genreExists = false;
        Boolean artistExists = false;
        Boolean albumExists = false;
        
        try {
            if (tag != null && !tag.isEmpty() && tag.getFirstField(FieldKey.YEAR) != null && !tag.getFirstField(FieldKey.YEAR).isEmpty() && getYear(tag.getFirstField(FieldKey.YEAR).getRawContent()) != null)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(getYear(tag.getFirstField(FieldKey.YEAR).getRawContent()), 0, 1);
                year = calendar.getTime();
                log.debug("Year: " + year.toString());
            }
            else 
                year = null;
        } catch (UnsupportedEncodingException ex) {
            log.debug("Unsupported Encoding. "  + ex);
            year = null;
        } catch (NullPointerException ex)
        {
            log.debug("Can't get year. <" + tag.getFirstField(FieldKey.YEAR).toString().trim() + ">" + ex);
            year = null;
        } catch (NumberFormatException ex)
        {
            log.debug("Can't get year. <" + tag.getFirstField(FieldKey.YEAR).toString().trim() + ">" + ex);   
            year = null;
        }
        
        
  
        Track tr;
        try {
            tr = new Track();
            if (tag.getFirst(FieldKey.TITLE) != null)
            {
                String str = tag.getFirst(FieldKey.TITLE);
                tr.setName(tag.getFirst(FieldKey.TITLE));
                if (tr.getName().length() > 255)
                    tr.setName(tr.getName().substring(0, 255));
            log.debug(tr.getName());
            }else
                tr = null;
        } catch(NullPointerException ex) {
            log.debug("Track is null");
            tr = null;
        }

        if (tr != null)
        {
            try {
                tr.setUrl(path);
            } catch (NullPointerException ex)
            {
                log.debug("Track url is null");
                tr.setUrl(null);
            }
            tr.setYear(year);
            Track tmpTrack;
            log.debug("Track " + tr.getName() + " exists: " + trackExists);
            if (trackCollection.containsKey(tr.getName()))
            {
                tr = trackCollection.get((tr.getName()));
            }
            else if ((tmpTrack  = findTrack(tr)) != null)
            {
                tr = tmpTrack;
                trackExists = true;
            }
            else
                trackCollection.put(tr.getName(), tr);
        }     
        
        Genre gen;
        try {
            gen = new Genre();
            if (tag.getFirst(FieldKey.GENRE) != null)
            {
                gen.setName(tag.getFirst(FieldKey.GENRE));
                if (gen.getName().length() > 255)
                    gen.setName(gen.getName().substring(0, 255));
                
                log.debug("Genre: " + gen.getName());
                Genre tmpGenre;
                log.debug("Genre contains; " + gen.getName() + " " + genreCollection.containsKey(gen.getName()));
                if (genreCollection.containsKey(gen.getName()))
                    gen = genreCollection.get((gen.getName()));
                else if ((tmpGenre  = findGenre(gen)) != null)
                {
                    gen = tmpGenre;
                    genreExists = true;
                }
                else
                {
                    log.debug("Putting new genre " + gen.getName());
                    genreCollection.put(gen.getName(), gen);
                }

            }else
                gen = null;
        } catch(NullPointerException ex) {
            log.debug("Genre is null");
            gen = null;
        }

        Artist art;
        try {
            art = new Artist();
            if (tag.getFirst(FieldKey.ARTIST) != null)
            {
                art.setName(tag.getFirst(FieldKey.ARTIST));
                if (art.getName().length() > 255)
                    art.setName(art.getName().substring(0, 255));                
                Artist tmpArtist;
                if (artistCollection.containsKey(art.getName()))
                    art = artistCollection.get((art.getName()));
                else if ((tmpArtist = findArtist(art)) != null)
                {
                    art = tmpArtist;
                    artistExists = true;
                }
                else
                {
                    log.debug("Putting new artist " + art.getName() + "...");
                    artistCollection.put(art.getName(), art);
                }
            }else
                art = null;
        } catch(NullPointerException ex) {
            log.debug("Artist is null");
            art = null;
        }

        Album alb;
        try {
            alb = new Album();
            if (tag.getFirst(FieldKey.ALBUM) != null)
            {
                alb.setName(tag.getFirst(FieldKey.ALBUM));
                if (alb.getName().length() > 255)
                    alb.setName(alb.getName().substring(0, 255));
                Album tmpAlbum;
                if (albumCollection.containsKey(alb.getName()))
                    alb = albumCollection.get((alb.getName()));
                else if ((tmpAlbum = findAlbum(alb)) != null)
                {
                    alb = tmpAlbum;
                    albumExists = true;
                }
                else
                    albumCollection.put(alb.getName(), alb);
            }
            else
                alb = null;
        } catch (NullPointerException ex)
        {
            log.debug("Album is null");
            alb = null;
        }
        if (alb != null)
        {
            alb.setYear(year);
            albumExists = findAlbum(alb) != null;
        }

        
        // Get info from Last.FM
//        TagData data = RemoteFinder.findTrackName(tr.getName(), art.getName());
//        tr.setName(data.getTitle());
//        art.setName(data.getArtist());
        
        if ((art != null && gen != null) && (!artistExists || !genreExists))
        {
            art.getGenreCollection().add(gen);
            gen.getArtistCollection().add(art);
        }
        if ((art != null && alb != null) && (!artistExists || !albumExists))
        {
            art.getAlbumCollection().add(alb);
            alb.getArtistCollection().add(art);
        }
        if ((alb != null && gen != null) && (!albumExists || !genreExists)){
            alb.getGenreCollection().add(gen);
            gen.getAlbumCollection().add(alb);
        }
        if ((tr != null && alb != null ) && (!albumExists || !trackExists)) {
            alb.getTrackCollection().add(tr);
            tr.getAlbumCollection().add(alb);
        }
        if ((tr != null && art != null) && (!artistExists || !trackExists)) {
            art.getTrackCollection().add(tr);
            tr.getArtistCollection().add(art);
        }
        if ((tr != null && gen != null) && (!genreExists || !trackExists)) {
            gen.getTrackCollection().add(tr);
            tr.getGenreCollection().add(gen);
        }

        log.debug("Track collection size: " + trackCollection.size());
        if (trackCollection.size() == BUFFER_SIZE)
        {
            sendToDB();
        }
    }

    public void close()
    {
        sendToDB();
        em.close();
        emf.close();
    }

    private void sendToDB() {
        em.getTransaction().begin();
        Iterator it = trackCollection.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, Track> entry = (Entry<String, Track>) it.next();
            em.persist(entry.getValue());
        }
        it = genreCollection.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, Genre> entry = (Entry<String, Genre>) it.next();
            em.persist(entry.getValue());
        }
        it = artistCollection.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, Artist> entry = (Entry<String, Artist>) it.next();
            em.persist(entry.getValue());
        }
        it = albumCollection.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, Album> entry = (Entry<String, Album>) it.next();
            em.persist(entry.getValue());
        }
        em.getTransaction().commit();
        trackCollection.clear();
        albumCollection.clear();
        artistCollection.clear();
        genreCollection.clear();
    }
}