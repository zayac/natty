/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.persist;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author necto
 */
@Entity
@Table(name = "artist")
@NamedQueries(
{
    @NamedQuery(name = "Artist.findAll", query = "SELECT a FROM Artist a"),
    @NamedQuery(name = "Artist.findById", query = "SELECT a FROM Artist a WHERE a.id = :id"),
    @NamedQuery(name = "Artist.findByName", query = "SELECT a FROM Artist a WHERE a.name = :name")
})
public class Artist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable=false)
    @GeneratedValue
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "artist", cascade = CascadeType.PERSIST)
    private Collection<Track> trackCollection;
    @JoinColumn(name = "genre", referencedColumnName = "id")
    @ManyToOne
    private Genre genre;

    public Artist ()
    {
        trackCollection = new HashSet();
    }

    public Artist (Integer id)
    {
        trackCollection = new HashSet();
        this.id = id;
    }

    public Artist (Integer id, String name)
    {
        trackCollection = new HashSet();
        this.id = id;
        this.name = name;
    }

    public Artist (String name)
    {
        trackCollection = new HashSet();
        this.name = name;
    }

    public Integer getId ()
    {
        return id;
    }

    public void setId (Integer id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Collection<Track> getTrackCollection ()
    {
        return trackCollection;
    }

    public void setTrackCollection (Collection<Track> trackCollection)
    {
        this.trackCollection = trackCollection;
    }

    public void addTrack (Track t)
    {
        this.trackCollection.add (t);
    }

    public Genre getGenre ()
    {
        return genre;
    }

    public void setGenre (Genre genre)
    {
        this.genre = genre;
    }

    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode () : 0);
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Artist))
        {
            return false;
        }
        Artist other = (Artist) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals (other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString ()
    {
        return "ru.natty.persist.Artist[id=" + id + "]";
    }

}
