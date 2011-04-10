/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.persist;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;

/**
 *
 * @author necto
 */
@Entity
@Table(name = "label")
@NamedQueries({
    @NamedQuery(name = "Label.findAll", query = "SELECT l FROM Label l"),
    @NamedQuery(name = "Label.findById", query = "SELECT l FROM Label l WHERE l.id = :id"),
    @NamedQuery(name = "Label.findByText", query = "SELECT l FROM Label l WHERE l.text = :text")})
public class Label implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "text")
    private String text;

    public Label() {
    }

    public Label(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Label)) {
            return false;
        }
        Label other = (Label) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


    public static Label queryById (Integer id, EntityManager em)
    {
        Query query = em.createNamedQuery("Label.findById");
        query.setParameter("id", id);
        return (Label)query.getSingleResult();
    }

    @Override
    public String toString() {
        return "ru.natty.web.persist.Label[id=" + id + "]";
    }

}
