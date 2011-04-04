/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.persist;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;

/**
 *
 * @author necto
 */
@Entity
@Table(name = "gui_properties")
@NamedQueries({
    @NamedQuery(name = "GuiProperties.findAll", query = "SELECT g FROM GuiProperties g"),
    @NamedQuery(name = "GuiProperties.findById", query = "SELECT g FROM GuiProperties g WHERE g.id = :id")})
public class GuiProperties implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "wtype", referencedColumnName = "id")
    @ManyToOne
    private WidgetType widgetType;
//    @OneToMany(mappedBy = "guiProperties")
//    private Collection<GuiProperties> guiPropertiesCollection;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private GuiProperties parent;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "guiProperties")
    private ContentHeader contentHeaders;

    public GuiProperties() {
    }

    public GuiProperties(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WidgetType getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(WidgetType widgetType) {
        this.widgetType = widgetType;
    }

//    public Collection<GuiProperties> getGuiPropertiesCollection() {
//        return guiPropertiesCollection;
//    }
//
//    public void setGuiPropertiesCollection(Collection<GuiProperties> guiPropertiesCollection) {
//        this.guiPropertiesCollection = guiPropertiesCollection;
//    }

    public GuiProperties getParent() {
        return parent;
    }

    public void setParent(GuiProperties guiProperties) {
        this.parent = guiProperties;
    }

    public ContentHeader getContentHeaders() {
        return contentHeaders;
    }

    public void setContentHeaders(ContentHeader contentHeaders) {
        this.contentHeaders = contentHeaders;
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
        if (!(object instanceof GuiProperties)) {
            return false;
        }
        GuiProperties other = (GuiProperties) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.natty.web.persist.GuiProperties[id=" + id + "]";
    }

    public static GuiProperties queryById (Integer id, EntityManager em)
    {
        Query query = em.createNamedQuery("GuiProperties.findById");
        query.setParameter("id", id);
        return (GuiProperties)query.getSingleResult();
    }

}
