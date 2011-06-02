/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.persist;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.apache.log4j.Logger;

/**
 *
 * @author zayac
 */
@Entity
@Table(name = "album")
@NamedQueries({
//    @NamedQuery(name = "AlbumArtist.findByName", query = "SELECT Artist.name, Album.name FROM Artist " +
//                                                    "INNER JOIN albums_artists ON Artist.id = albums_artists.artist_id " +
//                                                    "INNER JOIN Album ON albums_artists.album_id = Album.id " + 
//                                                    "WHERE Artist.name = :artist_name AND Album.name = :album_name"),
    @NamedQuery(name = "Album.findAll", query = "SELECT a FROM Album a"),
    @NamedQuery(name = "Album.findById", query = "SELECT a FROM Album a WHERE a.id = :id"),
    @NamedQuery(name = "Album.findByName", query = "SELECT a FROM Album a WHERE a.name = :name"),
    @NamedQuery(name = "Album.findByPattern", query = "SELECT a FROM Album a WHERE UPPER(a.name) like :name"),
    @NamedQuery(name = "Album.findByGenre", query = "SELECT a FROM Album a JOIN a.genreCollection g WHERE g.id = :genre"),
    @NamedQuery(name = "Album.findByNameAndYear", query = "SELECT a FROM Album a WHERE a.name = :name AND a.year = :year"),    
    @NamedQuery(name = "Album.findByNameAndNullYear", query = "SELECT a FROM Album a WHERE a.name = :name AND a.year IS NULL"),
    @NamedQuery(name = "Album.findByYear", query = "SELECT a FROM Album a WHERE a.year = :year")})
public class Album implements Serializable, IdNamed {
    private final static Logger log = Logger.getLogger(Album.class);
    private static final long serialVersionUID = 1L;
    @Id
	@SequenceGenerator(name="album_id_seq", sequenceName="album_id_seq", allocationSize=1)
	@GeneratedValue(generator="album_id_seq",strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "year")
    @Temporal(TemporalType.DATE)
    private Date year;
    @JoinTable(name = "albums_genres", joinColumns = {
        @JoinColumn(name = "album_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "genre_id", referencedColumnName = "id")})  
    @ManyToMany(cascade= CascadeType.REFRESH)
    private Set<Genre> genreCollection;
    @JoinTable(name = "albums_artists", joinColumns = {
        @JoinColumn(name = "album_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "artist_id", referencedColumnName = "id")})  
    @ManyToMany(cascade= CascadeType.REFRESH)
    private Set<Artist> artistCollection;
    @JoinTable(name = "tracks_albums", joinColumns = {
        @JoinColumn(name = "album_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "track_id", referencedColumnName = "id")})
    @ManyToMany
    private Set<Track> trackCollection;
    @Transient 
    private Boolean beanExists = false;
    @Transient 
    private final Integer STRING_LENGTH = 255;
    
//    @Transient
//    private Artist artist = null;
//    
//    public void setArtist(Artist art)
//    {
//        artist = art;
//    }
//    
//    public Artist getArtist()
//    {
//        return artist;
//    }
    
    public Boolean isExists()
    {
        return beanExists;
    }
    
    public void setExistsStatus(Boolean s)
    {
        beanExists = s;
    }
    
    public Album() {
        trackCollection = new HashSet<Track>();
        genreCollection = new HashSet<Genre>();
        artistCollection = new HashSet<Artist>();
    }

    public Album(String name) {
        this();
        this.name = name.replaceAll("\u0000", "");
        if(this.name.length() > STRING_LENGTH)
            this.name = this.name.substring(0, STRING_LENGTH);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replaceAll("\u0000", "");
        if(this.name.length() > STRING_LENGTH)
            this.name = this.name.substring(0, STRING_LENGTH);
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public Set<Genre> getGenreCollection() {
        return genreCollection;
    }

    public void setGenreCollection(Set<Genre> genreCollection) {
        this.genreCollection = genreCollection;
    }

    public Set<Artist> getArtistCollection() {
        return artistCollection;
    }

    public void setArtistCollection(Set<Artist> artistCollection) {
        this.artistCollection = artistCollection;
    }
    
    public Set<Track> getTrackCollection() {
        return trackCollection;
    }

    public void setTrackCollection(Set<Track> trackCollection)
	{
        this.trackCollection = trackCollection;
    }

    public static Query getQueryByPattern (String pattern, EntityManager em)
    {
		Query getAlbums = em.createNamedQuery ("Album.findByPattern");
		getAlbums.setParameter("name", pattern);
		return getAlbums;
    }

    public static Query getQueryByGenre (Integer genre, EntityManager em)
    {
		Query getAlbums = em.createNamedQuery ("Album.findByGenre");
		getAlbums.setParameter("genre", genre);
		return getAlbums;
	}

    public static String generateAlbumString(String name, Date year, Set<Artist> artistCollection) {
        if (year != null)
        {
            SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
            return "Album{" + "name=" + name + ", year=" + simpleDateformat.format(year)  + ", artistCollection=" + artistCollection + '}';
        }
        else
        return "Album{" + "name=" + name + ", year=null, artistCollection=" + artistCollection + '}';
    }
    
    
    @Override
    public int hashCode() {
        SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
        int hash = 5;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 29 * hash + (this.year != null ? simpleDateformat.format(this.year).hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");       
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Album other = (Album) obj;
        if (this.year == null )
            if(other.year != null)
                return false;
        if (this.year != null && other.year != null) 
        {
            String thYear = simpleDateformat.format(this.year);
            String otYear = simpleDateformat.format(other.year); 
            if ((thYear == null) ? (otYear != null) : !thYear.equals(otYear)) {
                //log.debug(thYear + " and " + otYear +" aren't equal.");
                return false;
            }
        }
        else if ((this.year == null && other.year != null) || (this.year != null && other.year == null))
            return false;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            //log.debug(this.name + " and " + other.name +" aren't equal.");
            return false;
        }
        return true;
    }

    public static List<Album> queryByPattern (String pattern, EntityManager em)
    {  
        return QueryList.forQuery(getQueryByPattern(pattern, em)).<Album>getAllResults();
    }

    @Override
    public String toString() {
        if (year != null)
        {
            SimpleDateFormat simpleDateformat=new SimpleDateFormat("yyyy");
            return "Album{" + "name=" + name + ", year=" + simpleDateformat.format(year)  + ", artistCollection=" + artistCollection + '}';
        }
        else
        return "Album{" + "name=" + name + ", year=null, artistCollection=" + artistCollection + '}';
    }
    





}

