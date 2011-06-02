/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.tags;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.*;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24FieldKey;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyTextInfo;
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
            List<Track> list = em.createNamedQuery("Track.findByNameAndYear")
                    .setParameter("name", trName.replaceAll("\u0000", ""))
                    .setParameter("year", tr.getYear())
                    .getResultList();
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

    public Track findTrack(Track tr, HashSet<Album> alb, HashSet<Artist> art)
    {
        log.debug("Trying to find a track in the database...");
        String trName = tr.getName();
        Date trYear = tr.getYear();
        Iterator<Album> albums = alb.iterator();        
        try{
            Boolean allAlbums = false;
            Boolean allArtists = false;
            Query q;
            if(trYear != null)
            {
                q = em.createNamedQuery("Track.findByNameAndYear")
                    .setParameter("name", trName.replaceAll("\u0000", ""))
                    .setParameter("year", trYear);
            }
            else
            {
                q = em.createNamedQuery("Track.findByNameAndNullYear")
                    .setParameter("name", trName.replaceAll("\u0000", ""));
            }
            List<Track> list = q.getResultList();
            log.info("Tracks number: " + list.size());
            Iterator<Track> tr_it = list.iterator();
            while(tr_it.hasNext())
            {
                Track currentTr = tr_it.next();
                Set<Album> currentCol = currentTr.getAlbumCollection();
                log.debug("Album size: " + alb.size());
                log.debug("Current Col size: " + currentCol.size());
                if(alb.size() == currentCol.size())
                {
                    Iterator<Album> alb_it = alb.iterator();
                    allAlbums = true;
                    while(alb_it.hasNext())
                    {
                        if(!currentCol.contains(alb_it.next()))
                        {
                            allAlbums = false;
                            break;
                        }
                    }
                }
                allArtists = true;
                Iterator<Artist> art_it = art.iterator();
                Set<Artist> currentColArt = currentTr.getArtistCollection();
                if(art.size() == currentColArt.size())
                {
                    while(art_it.hasNext())
                    {
                        if(!currentColArt.contains(art_it.next()))
                        {
                            allArtists = false;
                            break;
                        }
                    }
                }
                log.debug(allAlbums + " " + allArtists);
                if(allAlbums && allArtists)
                    return currentTr;
            }
            return null;
        }catch(NoResultException ex)
        {
            return null;
        }
    }
    
    public Album findAlbum(Album alb, HashSet<Artist> art)
    {
        String albName = alb.getName();
        Date albYear = alb.getYear();
        try{
            Boolean allArtists = false;
            Query q;
            if (albYear != null)
            {
                q = em.createNamedQuery("Album.findByNameAndYear")
                    .setParameter("name", albName.replaceAll("\u0000", ""))
                    .setParameter("year", albYear);
            }
            else
            {
                q = em.createNamedQuery("Album.findByNameAndNullYear")
                    .setParameter("name", albName.replaceAll("\u0000", ""));
            }
            List<Album> list = q.getResultList();
            Iterator<Album> alb_it = list.iterator();
            while(alb_it.hasNext())
            {
                Album currentAlb = alb_it.next();
                Set<Artist> currentCol = currentAlb.getArtistCollection();
                if(art.size() == currentCol.size())
                {
                    Iterator<Artist> art_it = art.iterator();
                    allArtists = true;
                    while(art_it.hasNext())
                    {
                        if(!currentCol.contains(art_it.next()))
                        {
                            allArtists = false;
                            break;
                        }
                    }
                    if(allArtists)
                    {
                        return currentAlb;
                    }
                }
            }
            return null;
        }catch(NoResultException ex)
        {
            return null;
        }
    }
    
    public Album findAlbum(Album alb)
    {
        String albName = alb.getName();
        try{
            List<Album> list = em.createNamedQuery("Album.findByNameAndYear")
                    .setParameter("name", albName.replaceAll("\u0000", ""))
                    .setParameter("year", alb.getYear())
                    .getResultList();
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
    
    private Date getYear(String path, MP3File f)
    {
        Pattern yearPattern = Pattern.compile("(19|20)\\d\\d");
        Pattern yearPathPattern = Pattern.compile("\\[(19|20)\\d\\d\\]");
        Calendar calendar = Calendar.getInstance();
        String year = null;
        if(f.hasID3v1Tag())
        {
            ID3v1Tag tag = f.getID3v1Tag();
            if(tag != null && !tag.isEmpty())
            {
                if(tag.getFirst(FieldKey.YEAR) != null)
                {
                    Matcher m = yearPattern.matcher(tag.getFirst(FieldKey.YEAR) );
                    if(m.find())
                    {
                        calendar.set(Integer.parseInt(m.group()), 0, 1);
                        return calendar.getTime();
                    }
                }
            }
            return null;
        }
        Matcher m = yearPathPattern.matcher(path);
        if(m.find())
        {
            calendar.set(Integer.parseInt(m.group().substring(1, 5)), 0, 1);
            return calendar.getTime(); 
        }
        return null;
    }   
    
    private String formatArtist(String source)
    {
        source.replaceAll("\\(\\d+\\)", "");
        try
        {
            Integer.parseInt(source);
        }catch(NumberFormatException ex)
        {
            source = source.trim();
            return source;
        }
        return null; 
    }
    
    private ArrayList<String> getTrackName(String path, MP3File f)
    {
        ArrayList<String> ret = new ArrayList<String>();
        Tag tag = f.getTag();
        if(tag != null && !tag.isEmpty())
        {
            Integer fieldsNumber = tag.getFields(FieldKey.TITLE).size();
            for(int i = 0; i < fieldsNumber; i++)
            {
                ret.add(tag.getValue(FieldKey.TITLE, i));
            }
        }
        return ret;
    }
    
    
    private ArrayList<Track> getTrack(String path, MP3File f)
    {
        ArrayList<Track> ret = new ArrayList<Track>();
        Tag tag  = f.getTag();
        if(tag != null && !tag.isEmpty())
        {
            Integer fieldsNumber = tag.getFields(FieldKey.TITLE).size();
            for(int i = 0; i < fieldsNumber; i++)
            {
                String formatedTrack = formatTrack(tag.getValue(FieldKey.TITLE, i));
                log.debug(formatedTrack);
                ArrayList<Artist> artists = getArtist(path, f);
                ArrayList<Album> albums = getAlbum(path, f);
                if(formatedTrack != null)
                {
                    HashSet<Artist> artCol = new HashSet<Artist>();
                    HashSet<Album> albCol = new HashSet<Album>();
                    for(int j = 0; j < artists.size(); j++)
                        artCol.add(artists.get(j));
                    for(int j = 0; j < albums.size(); j++)
                        albCol.add(albums.get(j));
                    String hashString = Track.generateTrackString(formatedTrack, getYear(path, f), albCol); 
                    log.debug("Hashstring: " + hashString);
                    Track tmp = new Track();
                    Track tr = new Track();
                    tr.setName(formatedTrack);
                    tr.setYear(getYear(path, f));
                    tr.setUrl(path);
                    if(trackCollection.containsKey(hashString))
                    {
                        log.debug("Track is found in collection");
                        tr = trackCollection.get(hashString); 
                        log.debug("Track is found in the collection");
                        //tr.setExistsStatus(true);
                    }
                    else if ((tmp = findTrack(tr, albCol, artCol)) != null)
                    {
                        log.debug("Track is found in database");
                        tr = tmp;            
                        log.debug("Track is found in the database");
                        //tr.setExistsStatus(true);
                    }
                    else
                        log.debug("Track is not found.");
                    log.debug("Track added: " + tr.toString());
                    ret.add(tr);
                }
            }
        }
        return ret;
    }
    
    private ArrayList<Artist> getArtist(String path, MP3File f)
    {
        ArrayList<Artist> ret = new ArrayList<Artist>();
        Tag tag = f.getTag();
        if(tag != null && !tag.isEmpty())
        {
            Integer fieldsNumber = tag.getFields(FieldKey.ARTIST).size();
            for(int i = 0; i < fieldsNumber; i++)
            {
                String[] names = tag.getValue(FieldKey.ARTIST, i).split("/|( (?i)Feat.?)|( (?i)Ft.?)|( (?i)Vs.?)|( \\& )");
                HashSet<String> parsedNames = new HashSet<String>();
                for(int j = 0; j < names.length; j++)
                {
                    if(!parsedNames.contains(names[j]))
                    {
                        String formated = formatArtist(names[j]);
                        if (formated != null)
                        {
                            Artist art = new Artist();
                            art.setName(formated);
                            Artist tmp;
                            if(artistCollection.containsKey(art.toString()))
                            {
                                art = artistCollection.get(art.toString());
                                art.setExistsStatus(true);
                            }
                            else if ((tmp = findArtist(art)) != null)
                            {
                                art = tmp;
                                art.setExistsStatus(true);
                            }
                            ret.add(art);
                        }
                        parsedNames.add(names[j]);
                    }
                }
            }
        }
        return ret;
    }
    
    private String formatGenre(String source)
    {
        source = source.replaceAll("\\(\\d+\\)", "");
        try
        {
            Integer.parseInt(source);
        }catch(NumberFormatException ex)
        {
            source = source.trim();
            return WordUtils.capitalize(source.toLowerCase()).trim();
        }
        return null;
    }
    
    private ArrayList<Genre> getGenre(String path, MP3File f)
    {
        ArrayList<Genre> ret = new ArrayList<Genre>();
        Tag tag = f.getTag();
        if(tag != null && !tag.isEmpty())
        {
            Integer fieldsNumber = tag.getFields(FieldKey.GENRE).size();
            for(int i = 0; i < fieldsNumber; i++)
            {
                String[] genres = tag.getValue(FieldKey.GENRE, i).split("/");
                HashSet<String> parsedGenres = new HashSet<String>();
                for(int j = 0; j < genres.length; j++)
                {
                    if(!parsedGenres.contains(genres[j]))
                    {
                        Genre gen = new Genre();
                        String formated = formatGenre(genres[j]);
                        if(formated != null)
                        {
                            gen.setName(formated);
                            Genre tmp;
                            if(genreCollection.containsKey(gen.toString()))
                            {
                                gen = genreCollection.get(gen.toString());
                                gen.setExistsStatus(true);
                            }
                            else if ((tmp = findGenre(gen)) != null)
                            {
                                gen = tmp;
                                gen.setExistsStatus(true);
                            }
                            ret.add(gen);
                        }
                        parsedGenres.add(genres[j]);
                    }
                }
            }
        }
        return ret;
    }
    
    private String formatTrack(String source)
    {
        source = source.replaceAll(" *\\d?\\d *- *", "");
        try
        {
            Integer.parseInt(source);
        }catch(NumberFormatException ex)
        {
            source = source.trim();
            return WordUtils.capitalize(source.toLowerCase());
        }
        return null;
    }
    
    private String formatAlbum(String source)
    {
        source = source.replaceAll("\\(\\d+\\)", "");
        try
        {
            Integer.parseInt(source);
        }catch(NumberFormatException ex)
        {
            source = source.trim();
            return WordUtils.capitalize(source.toLowerCase());
        }
        return null;
    }
    
    

    
    private ArrayList<Album> getAlbum(String path, MP3File f)
    {
        ArrayList<Album> ret = new ArrayList<Album>();
        Tag tag = f.getTag();
        if(tag != null && !tag.isEmpty())
        {
            Integer fieldNumber = tag.getFields(FieldKey.ALBUM).size();
            for(int i = 0; i < fieldNumber; i++)
            {
                String formatedAlbum = formatAlbum(tag.getValue(FieldKey.ALBUM, i));
                ArrayList<Artist> artists = getArtist(path, f);
                if(formatedAlbum != null)
                {
                    HashSet<Artist> artCol = new HashSet<Artist>();
                    for(int j = 0; j < artists.size(); j++)
                        artCol.add(artists.get(j));
                    String hashString = Album.generateAlbumString(formatedAlbum, getYear(path, f), artCol); 
                    Album tmp = new Album();
                    Album alb = new Album();
                    alb.setName(formatedAlbum);
                    alb.setYear(getYear(path, f));
                    if(albumCollection.containsKey(hashString))
                    {
                        log.debug("Album is found in collection");
                        alb = albumCollection.get(hashString);                            
                        //alb.setExistsStatus(true);
                    }
                    else if ((tmp = findAlbum(alb, artCol)) != null)
                    {
                        log.debug("Album is found in database");
                        alb = tmp;            
                        //alb.setExistsStatus(true);
                    }
                    ret.add(alb);
                }
            }
        }
        return ret;
    }
    
    
    public void commit(String path, MP3File f)
    {
        ArrayList<Track> tracks = getTrack(path, f);
        ArrayList<Genre> genres = getGenre(path, f);
        ArrayList<Album> albums = getAlbum(path, f);
        ArrayList<Artist> artists = getArtist(path, f);

//        for(int i = 0; i < tracks.size(); i++)
//        {
//            log.info("Track: " + tracks.get(i).getName());
//        }
//        for(int i = 0; i < albums.size(); i++)
//        {
//            log.info("Album: " + albums.get(i).getName());
//        }
//        for(int i = 0; i < artists.size(); i++)
//        {
//            log.info("Artist: " + artists.get(i).getName());
//        }
        
        if (!artists.isEmpty() && !genres.isEmpty())
        {
            for(int i = 0; i < artists.size(); i++)
                for(int j = 0; j < genres.size(); j++)
                {
                    if (!artists.get(i).isExists()) artists.get(i).getGenreCollection().add(genres.get(j));
                    if (!genres.get(j).isExists()) genres.get(j).getArtistCollection().add(artists.get(i));
                }
        }

        if (!artists.isEmpty() && !albums.isEmpty())
        {
            for(int i = 0; i < artists.size(); i++)
                for(int j = 0; j < albums.size(); j++)
                {
                    if (!artists.get(i).isExists()) artists.get(i).getAlbumCollection().add(albums.get(j));
                    if (!albums.get(j).isExists()) albums.get(j).getArtistCollection().add(artists.get(i));
                }
        }
    
        if (!genres.isEmpty() && !albums.isEmpty())
        {
            for(int i = 0; i < genres.size(); i++)
                for(int j = 0; j < albums.size(); j++)
                {
                    if (!genres.get(i).isExists()) genres.get(i).getAlbumCollection().add(albums.get(j));
                    if (!albums.get(j).isExists()) albums.get(j).getGenreCollection().add(genres.get(i));
                }
        } 
        if (!tracks.isEmpty() && !albums.isEmpty())
        {
            for(int i = 0; i < tracks.size(); i++)
                for(int j = 0; j < albums.size(); j++)
                {
                    log.debug(tracks.get(i) + " <-> " + albums.get(j));
                    if (!tracks.get(i).isExists()) tracks.get(i).getAlbumCollection().add(albums.get(j));
                    if (!albums.get(j).isExists()) albums.get(j).getTrackCollection().add(tracks.get(i));

                }
        }

        if (!tracks.isEmpty() && !genres.isEmpty())
        {
            for(int i = 0; i < tracks.size(); i++)
                for(int j = 0; j < genres.size(); j++)
                {       
                    if (!tracks.get(i).isExists()) tracks.get(i).getGenreCollection().add(genres.get(j));
                    if (!genres.get(j).isExists()) genres.get(j).getTrackCollection().add(tracks.get(i));
                }
        }
        if (!tracks.isEmpty() && !artists.isEmpty())
        {
            for(int i = 0; i < tracks.size(); i++)
                for(int j = 0; j < artists.size(); j++)
                {                     
                    if (!tracks.get(i).isExists()) tracks.get(i).getArtistCollection().add(artists.get(j));
                    if (!artists.get(j).isExists()) artists.get(j).getTrackCollection().add(tracks.get(i));
                }
        }
        
        for (int i = 0; i < tracks.size(); i++) 
        {
            log.debug("Track " + i + ": " + tracks.get(i).toString());
            trackCollection.put(tracks.get(i).toString(), tracks.get(i));
        }
        for (int i = 0; i < genres.size(); i++) genreCollection.put(genres.get(i).toString(), genres.get(i));
        for (int i = 0; i < artists.size(); i++) artistCollection.put(artists.get(i).toString(), artists.get(i));
        for (int i = 0; i < albums.size(); i++) albumCollection.put(albums.get(i).toString(), albums.get(i));
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
        log.debug("Persisting...");
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