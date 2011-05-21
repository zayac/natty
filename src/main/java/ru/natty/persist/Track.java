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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author zayac
 */
@Entity
@Table(name = "track")
@NamedQueries({
    @NamedQuery(name = "Track.findAll", query = "SELECT t FROM Track t"),
    @NamedQuery(name = "Track.findById", query = "SELECT t FROM Track t WHERE t.id = :id"),
    @NamedQuery(name = "Track.findByName", query = "SELECT t FROM Track t WHERE t.name = :name"),
    @NamedQuery(name = "Track.findByPattern", query = "SELECT t FROM Track t WHERE UPPER(t.name) LIKE :name"),
    @NamedQuery(name = "Track.findByYear", query = "SELECT t FROM Track t WHERE t.year = :year"),
    @NamedQuery(name = "Track.findByUrl", query = "SELECT t FROM Track t WHERE t.url = :url")})
public class Track implements Serializable, IdNamed
{
    private static final long serialVersionUID = 1L;
    @Id
	@SequenceGenerator(name="track_id_seq", sequenceName="track_id_seq", allocationSize=1)
	@GeneratedValue(generator="track_id_seq",strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "year")
    @Temporal(TemporalType.DATE)
    private Date year;
    @Column(name = "url")
    private String url;
    @ManyToMany(mappedBy="trackCollection")
    private Set<Album> albumCollection;
    @ManyToMany(mappedBy="trackCollection")
    private Set<Genre> genreCollection;
    @ManyToMany(mappedBy="trackCollection")
    private Set<Artist> artistCollection;
    @Transient 
    private Boolean beanExists = false;
    
    public Boolean isExists()
    {
        return beanExists;
    }
    
    public void setExistsStatus(Boolean s)
    {
        beanExists = s;
    }
    public Track() {
        albumCollection = new HashSet<Album>();
        artistCollection = new HashSet<Artist>();
        genreCollection = new HashSet<Genre>();
    }

    public Track(String name) {
        this();
        this.name = name.replaceAll("\u0000", "");
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replaceAll("\u0000", "");
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url.replaceAll("\u0000", "");
    }

    public Set<Album> getAlbumCollection() {
        return albumCollection;
    }

    public void setAlbumCollection(Set<Album> albumCollection) {
        this.albumCollection = albumCollection;
    }

    public Collection<Genre> getGenreCollection() {
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




	public static Query getQueryByPattern (String pattern, EntityManager em)
	{
		Query getTracks = em.createNamedQuery ("Track.findByPattern");
		getTracks.setParameter("name", pattern);
		
		return getTracks;
	}
	
    public static List<Track> queryByPattern (String pattern, EntityManager em)
    {
		return QueryList.forQuery(getQueryByPattern (pattern, em)).
										<Track>getAllResults();
    }

    public static Track queryById (Integer id, EntityManager em)
    {
		Query getTrack = em.createNamedQuery ("Track.findById");
		getTrack.setParameter("id", id);
		return (Track)getTrack.getSingleResult();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Track other = (Track) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.year != other.year && (this.year == null || !this.year.equals(other.year))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 37 * hash + (this.year != null ? this.year.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Track{" + "name=" + name + ", year=" + year + '}';
    }





}

