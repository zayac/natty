/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.persist;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "widget_type")
@NamedQueries({
    @NamedQuery(name = "WidgetType.findAll", query = "SELECT w FROM WidgetType w"),
    @NamedQuery(name = "WidgetType.findById", query = "SELECT w FROM WidgetType w WHERE w.id = :id"),
    @NamedQuery(name = "WidgetType.findByName", query = "SELECT w FROM WidgetType w WHERE w.name = :name")})
public class WidgetType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "widgetType")
    private Collection<GuiProperties> guiPropertiesCollection;

    public WidgetType() {
    }

    public WidgetType(Integer id) {
        this.id = id;
    }

    public WidgetType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<GuiProperties> getGuiPropertiesCollection() {
        return guiPropertiesCollection;
    }

    public void setGuiPropertiesCollection(Collection<GuiProperties> guiPropertiesCollection) {
        this.guiPropertiesCollection = guiPropertiesCollection;
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
        if (!(object instanceof WidgetType)) {
            return false;
        }
        WidgetType other = (WidgetType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.natty.web.persist.WidgetType[id=" + id + "]";
    }

}
