/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.persist;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author necto
 */
@Entity
@Table(name = "content_header")
@NamedQueries({
    @NamedQuery(name = "ContentHeaders.findAll", query = "SELECT c FROM ContentHeader c"),
    @NamedQuery(name = "ContentHeaders.findByContentId", query = "SELECT c FROM ContentHeader c WHERE c.contentId = :contentId"),
    @NamedQuery(name = "ContentHeaders.findByHeader", query = "SELECT c FROM ContentHeader c WHERE c.header = :header")})
public class ContentHeader implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "content_id")
    private Integer contentId;
    @Basic(optional = false)
    @Column(name = "header")
    private String header;
    @JoinColumn(name = "content_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private GuiProperties guiProperties;

    public ContentHeader() {
    }

    public ContentHeader(Integer contentId) {
        this.contentId = contentId;
    }

    public ContentHeader(Integer contentId, String header) {
        this.contentId = contentId;
        this.header = header;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public GuiProperties getGuiProperties() {
        return guiProperties;
    }

    public void setGuiProperties(GuiProperties guiProperties) {
        this.guiProperties = guiProperties;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contentId != null ? contentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContentHeader)) {
            return false;
        }
        ContentHeader other = (ContentHeader) object;
        if ((this.contentId == null && other.contentId != null) || (this.contentId != null && !this.contentId.equals(other.contentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.natty.web.persist.ContentHeaders[contentId=" + contentId + "]";
    }

}
