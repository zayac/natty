/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.persist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

/**
 *
 * @author zayac
 */
@Entity
@Table(name = "genre")
@NamedQueries({
    @NamedQuery(name = "Genre.findAll", query = "SELECT g FROM Genre g"),
    @NamedQuery(name = "Genre.findById", query = "SELECT g FROM Genre g WHERE g.id = :id"),
    @NamedQuery(name = "Genre.findByName", query = "SELECT g FROM Genre g WHERE g.name = :name"),
    @NamedQuery(name = "Genre.findByPattern", query = "SELECT g FROM Genre g WHERE UPPER(g.name) like UPPER(:name)")})
public class Genre implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
	@SequenceGenerator(name="genre_id_seq", sequenceName="genre_id_seq")
	@GeneratedValue(generator="genre_id_seq",strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy="genreCollection")
    private Collection<Album> albumCollection;
    @JoinTable(name = "tracks_genres", joinColumns = {
        @JoinColumn(name = "genre_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "track_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Track> trackCollection;
    @ManyToMany(mappedBy="genreCollection")
    private Collection<Artist> artistCollection;

    public Genre() {
        trackCollection = new HashSet<Track>();
        albumCollection = new HashSet<Album>();
        artistCollection = new HashSet<Artist>();
    }

    public Genre(String name) {
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

    public Collection<Album> getAlbumCollection() {
        return albumCollection;
    }

    public void setAlbumCollection(Collection<Album> albumCollection) {
        this.albumCollection = albumCollection;
    }

    public Collection<Track> getTrackCollection() {
        return trackCollection;
    }

    public void setTrackCollection(Collection<Track> trackCollection) {
        this.trackCollection = trackCollection;
    }

    public Collection<Artist> getArtistCollection() {
        return artistCollection;
    }

    public void setArtistCollection(Collection<Artist> artistCollection) {
        this.artistCollection = artistCollection;
    }



    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Genre other = (Genre) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
    public static List<Genre> queryByPattern (String pattern, EntityManager em)
    {
		Query getGenres = em.createNamedQuery ("Genre.findByPattern");
		getGenres.setParameter("name", pattern);
		List rez = getGenres.getResultList();
		List<Genre> gens = new ArrayList<Genre>();

		for (Object o : rez)
			gens.add ((Genre)o);
		return gens;
    }

    @Override
    public String toString() {
        return "ru.natty.persist.Genre[id=" + id + "]"+getName()+" "+super.hashCode();
    }

}

