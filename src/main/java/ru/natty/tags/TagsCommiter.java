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
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.ID3v24Tag;
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
                if(tag.getFirstYear() != null)
                {
                    Matcher m = yearPattern.matcher(tag.getFirstYear());
                    if(m.find())
                    {
                        log.debug("'" + m.group() + "'");
                        calendar.set(Integer.parseInt(m.group()), 0, 1);
                        log.debug("Year: " + calendar.getTime());
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
        if(f.hasID3v1Tag())
        {
            ID3v1Tag tag = f.getID3v1Tag();
            if(tag != null && !tag.isEmpty())
            {
                List<TagField> fields = tag.getFields(FieldKey.TITLE);
                for(int i = 0; i < fields.size(); i++)
                {
                    ret.add(fields.get(i).toString());
                }
            }
        }
//        if(f.hasID3v2Tag())
//        {
//            ID3v24Tag v24tag = f.getID3v2TagAsv24();
//            if(v24tag != null && !v24tag.isEmpty())
//            {
//                List<TagField> fields = v24tag.getFields(FieldKey.TITLE);
//                for(int i = 0; i < fields.size(); i++)
//                {
//                    log.debug("Title: " + fields.get(i).toString());
//                    ret.add(fields.get(i).toString());
//                }
//            }
//        }
        return ret;
    }
    
    
    private ArrayList<Track> getTrack(String path, MP3File f)
    {
        ArrayList<String> trackNames = getTrackName(path, f);
        ArrayList<Track> tracks = new ArrayList<Track>();
        Date year = getYear(path, f);
        for(int i = 0; i < trackNames.size(); i++)
        {
            Track tr = new Track();
            tr.setName(trackNames.get(i));
            //tr.setUrl(URLEncoder.encode(path, "UTF-8"));
            tr.setUrl(path);
            if (year != null)
            {
                tr.setYear(year);
            }
            Track tmp;
            if(trackCollection.containsKey(tr.toString()))
            {
                tr = trackCollection.get(tr.toString());
                tr.setExistsStatus(true);
            }else if((tmp = findTrack(tr)) != null)
            {
                tr = tmp;
                tr.setExistsStatus(true);
            }
            tracks.add(tr);
        }
        return tracks;
    }
    
    private ArrayList<Artist> getArtist(String path, MP3File f)
    {
        ArrayList<Artist> ret = new ArrayList<Artist>();
        if(f.hasID3v1Tag())
        {
            ID3v1Tag tag = f.getID3v1Tag();
            if(tag != null && !tag.isEmpty())
            {
                List<TagField> fields = tag.getFields(FieldKey.ARTIST);
                for(int i = 0; i < fields.size(); i++)
                {
                    String[] names = fields.get(i).toString().split("/|( (?i)Feat.?)|( (?i)Ft.?)|( (?i)Vs.?)|( \\& )");
                    for(int j = 0; j < names.length; j++)
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
                    }
                }
            }
        }
//        if(f.hasID3v2Tag())
//        {
//            ID3v24Tag v24tag = f.getID3v2TagAsv24();
//            if(v24tag != null && !v24tag.isEmpty())
//            {
//                List<TagField> fields = v24tag.getFields(FieldKey.ARTIST);
//                for(int i = 0; i < fields.size(); i++)
//                {
//                    Artist art = new Artist();
//                    art.setName(fields.get(i).toString());
//                    log.debug("Artist: " + fields.get(i).toString());
//                    ret.add(art);
//                }
//            }
//        }
        return ret;
    }
    
    private String formatGenre(String source)
    {
        source.replaceAll("\\(\\d+\\)", "");
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
        if(f.hasID3v1Tag())
        {
            ID3v1Tag tag = f.getID3v1Tag();
            if(tag != null && !tag.isEmpty())
            {
                List<TagField> fields = tag.getFields(FieldKey.GENRE);
                for(int i = 0; i < fields.size(); i++)
                {
                    String[] genres = fields.get(i).toString().split("/");
                    for(int j = 0; j < genres.length; j++)
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
                    }
                }
            }
        }
//        if(f.hasID3v2Tag())
//        {
//            ID3v24Tag v24tag = f.getID3v2TagAsv24();
//            if(v24tag != null && !v24tag.isEmpty())
//            {
//                List<TagField> fields = v24tag.getFields(FieldKey.GENRE);
//                for(int i = 0; i < fields.size(); i++)
//                {
//                    Genre gen = new Genre();
//                    gen.setName(fields.get(i).toString());
//                    ret.add(gen);
//                }
//            }
//        } 
        return ret;
    }
    
    private String formatAlbum(String source)
    {
        source.replaceAll("\\(\\d+\\)", "");
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
    
    public String generateAlbumString(String name, Date year, Set<Artist> artistCollection) {
        if (year != null)
        {
            SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
            return "Album{" + "name=" + name + ", year=" + simpleDateformat.format(year)  + ", artistCollection=" + artistCollection + '}';
        }
        else
        return "Album{" + "name=" + name + ", year=null, artistCollection=" + artistCollection + '}';
    }
    
    private ArrayList<Album> getAlbum(String path, MP3File f)
    {
        ArrayList<Album> ret = new ArrayList<Album>();
        if(f.hasID3v1Tag())
        {
            ID3v1Tag tag = f.getID3v1Tag();
            if(tag != null && !tag.isEmpty())
            {
                List<TagField> fields = tag.getFields(FieldKey.ALBUM);
                for(int i = 0; i < fields.size(); i++)
                {
                    String formatedAlbum = formatAlbum(fields.get(i).toString());
                    ArrayList<Artist> artists = getArtist(path, f);
                    if(formatedAlbum != null)
                    {
                        HashSet<Artist> artCol = new HashSet<Artist>();
                        for(int j = 0; j < artists.size(); j++)
                            artCol.add(artists.get(j));
                        String hashString = generateAlbumString(formatedAlbum, getYear(path, f), artCol); 
                        log.debug("Hashstring: " + hashString);
                        Album tmp = new Album();
                        Album alb = new Album();
                        alb.setName(formatedAlbum);
                        alb.setYear(getYear(path, f));
                        if(albumCollection.containsKey(hashString))
                        {
                            log.debug("Album is found is collection");
                            alb = albumCollection.get(hashString);                            
                            alb.setExistsStatus(true);
                        }
                        else if ((tmp = findAlbum(alb)) != null)
                        {
                            log.debug("Album is found is database");
                            if(tmp.toString().equals(hashString))
                            {
                                alb = tmp;            
                                alb.setExistsStatus(true);
                            }
                        }
                        log.debug("Album added: " + alb.toString());
                        ret.add(alb);
                    }
                }
            }
        }
//        if(f.hasID3v2Tag())
//        {
//            ID3v24Tag v24tag = f.getID3v2TagAsv24();
//            if(v24tag != null && !v24tag.isEmpty())
//            {
//                List<TagField> fields = v24tag.getFields(FieldKey.ALBUM);
//                for(int i = 0; i < fields.size(); i++)
//                {
//                    Album alb = new Album();
//                    alb.setName(fields.get(i).toString());
//                    log.debug("Album: " + fields.get(i).toString());
//                    ret.add(alb);
//                }
//            }
//        }
        return ret;
    }
    
    
    public void commit(String path, MP3File f)
    {
        ArrayList<Track> tracks = getTrack(path, f);
        ArrayList<Genre> genres = getGenre(path, f);
        ArrayList<Album> albums = getAlbum(path, f);
        ArrayList<Artist> artists = getArtist(path, f);
        
        if (!artists.isEmpty() && !genres.isEmpty())
        {
            for(int i = 0; i < artists.size(); i++)
                for(int j = 0; j < genres.size(); j++)
                {
                    if(!artists.get(i).getGenreCollection().contains(genres.get(j)))
                    {
                        if (!artists.get(i).isExists()) artists.get(i).getGenreCollection().add(genres.get(j));
                        if (!genres.get(j).isExists()) genres.get(j).getArtistCollection().add(artists.get(i));
                    }
                }
        }
        if (!artists.isEmpty() && !albums.isEmpty())
        {
            for(int i = 0; i < artists.size(); i++)
                for(int j = 0; j < albums.size(); j++)
                {
                    if(!artists.get(i).getAlbumCollection().contains(albums.get(j)) || !albums.get(j).getArtistCollection().contains(artists.get(i)))
                    {
                        if (!artists.get(i).isExists()) artists.get(i).getAlbumCollection().add(albums.get(j));
                        if (!albums.get(j).isExists()) albums.get(j).getArtistCollection().add(artists.get(i));
                    }
                }
        }
        if (!genres.isEmpty() && !albums.isEmpty())
        {
            for(int i = 0; i < genres.size(); i++)
                for(int j = 0; j < albums.size(); j++)
                {
                    if(!genres.get(i).getAlbumCollection().contains(albums.get(j)))
                    {
                        if (!genres.get(i).isExists()) genres.get(i).getAlbumCollection().add(albums.get(j));
                        if (!albums.get(j).isExists()) albums.get(j).getGenreCollection().add(genres.get(i));
                    }
                }
        }
        if (!tracks.isEmpty() && !albums.isEmpty())
        {
            for(int i = 0; i < tracks.size(); i++)
                for(int j = 0; j < albums.size(); j++)
                {
                    if(!tracks.get(i).getAlbumCollection().contains(albums.get(j)))
                    {
                        if (!tracks.get(i).isExists()) tracks.get(i).getAlbumCollection().add(albums.get(j));
                        if (!albums.get(j).isExists()) albums.get(j).getTrackCollection().add(tracks.get(i));
                    }
                }
        }
        
        if (!tracks.isEmpty() && !genres.isEmpty())
        {
            for(int i = 0; i < tracks.size(); i++)
                for(int j = 0; j < genres.size(); j++)
                {
                   if(!tracks.get(i).getGenreCollection().contains(genres.get(j)))
                   {            
                        if (!tracks.get(i).isExists()) tracks.get(i).getGenreCollection().add(genres.get(j));
                        if (!genres.get(j).isExists()) genres.get(j).getTrackCollection().add(tracks.get(i));
                   }
                }
        }
        if (!tracks.isEmpty() && !artists.isEmpty())
        {
            for(int i = 0; i < tracks.size(); i++)
                for(int j = 0; j < artists.size(); j++)
                {
                   if(!tracks.get(i).getArtistCollection().contains(artists.get(j)))
                   {                       
                        if (!tracks.get(i).isExists()) tracks.get(i).getArtistCollection().add(artists.get(j));
                        if (!artists.get(j).isExists()) artists.get(j).getTrackCollection().add(tracks.get(i));
                   }
                }
        }
        
        for (int i = 0; i < tracks.size(); i++) trackCollection.put(tracks.get(i).toString(), tracks.get(i));
        for (int i = 0; i < genres.size(); i++) genreCollection.put(genres.get(i).toString(), genres.get(i));
        for (int i = 0; i < artists.size(); i++) artistCollection.put(artists.get(i).toString(), artists.get(i));
        for (int i = 0; i < albums.size(); i++) albumCollection.put(albums.get(i).toString(), albums.get(i));
        
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