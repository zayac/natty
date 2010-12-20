/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.persist;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author necto
 */
@Entity
@Table(name = "genre")
@NamedQueries(
{
    @NamedQuery(name = "Genre.findAll", query = "SELECT g FROM Genre g"),
    @NamedQuery(name = "Genre.findById", query = "SELECT g FROM Genre g WHERE g.id = :id"),
    @NamedQuery(name = "Genre.findByName", query = "SELECT g FROM Genre g WHERE g.name = :name")
})
public class Genre implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "genre")
    private Collection<Album> albumCollection;
    @OneToMany(mappedBy = "genre")
    private Collection<Track> trackCollection;
    @OneToMany(mappedBy = "genre")
    private Collection<Artist> artistCollection;

    public Genre ()
    {
    }

    public Genre (Integer id)
    {
        this.id = id;
    }

    public Genre (Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Genre (String name)
    {
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

    public Collection<Album> getAlbumCollection ()
    {
        return albumCollection;
    }

    public void setAlbumCollection (Collection<Album> albumCollection)
    {
        this.albumCollection = albumCollection;
    }

    public Collection<Track> getTrackCollection ()
    {
        return trackCollection;
    }

    public void setTrackCollection (Collection<Track> trackCollection)
    {
        this.trackCollection = trackCollection;
    }

    public Collection<Artist> getArtistCollection ()
    {
        return artistCollection;
    }

    public void setArtistCollection (Collection<Artist> artistCollection)
    {
        this.artistCollection = artistCollection;
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
        if (!(object instanceof Genre))
        {
            return false;
        }
        Genre other = (Genre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals (other.id)))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString ()
    {
        return "ru.natty.persist.Genre[id=" + id + "]";
    }

}
