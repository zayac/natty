/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.persist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author zayac
 */
@Entity
@Table(name = "album")
@NamedQueries({
    @NamedQuery(name = "Album.findAll", query = "SELECT a FROM Album a"),
    @NamedQuery(name = "Album.findById", query = "SELECT a FROM Album a WHERE a.id = :id"),
    @NamedQuery(name = "Album.findByName", query = "SELECT a FROM Album a WHERE a.name = :name"),
    @NamedQuery(name = "Album.findByPattern", query = "SELECT a FROM Album a WHERE UPPER(a.name) like UPPER(:name)"),
    @NamedQuery(name = "Album.findByGenre", query = "SELECT a FROM Album a JOIN a.genreCollection g WHERE g.id = :genre"),
    @NamedQuery(name = "Album.findByYear", query = "SELECT a FROM Album a WHERE a.year = :year")})
public class Album implements Serializable, IdNamed {
    private static final long serialVersionUID = 1L;
    @Id
	@SequenceGenerator(name="album_id_seq", sequenceName="album_id_seq")
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
    @ManyToMany
    private Collection<Genre> genreCollection;
    @JoinTable(name = "tracks_albums", joinColumns = {
        @JoinColumn(name = "album_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "track_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<Track> trackCollection;

    public Album() {
        trackCollection = new HashSet<Track>();
        genreCollection = new HashSet<Genre>();
    }

    public Album(String name) {
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

    public Collection<Genre> getGenreCollection() {
        return genreCollection;
    }

    public void setGenreCollection(Collection<Genre> genreCollection) {
        this.genreCollection = genreCollection;
    }

    public Collection<Track> getTrackCollection() {
        return trackCollection;
    }

    public void setTrackCollection(Collection<Track> trackCollection)
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
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
        final Album other = (Album) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
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
        return "ru.natty.persist.Album[id=" + id + "]";
    }

}

