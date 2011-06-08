/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.persist;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author necto
 */
@Embeddable
public class PanelContentsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "panel_id")
    private int panelId;
    @Basic(optional = false)
    @Column(name = "content_id")
    private int contentId;

    public PanelContentsPK() {
    }

    public PanelContentsPK(int panelId, int contentId) {
        this.panelId = panelId;
        this.contentId = contentId;
    }

    public int getPanelId() {
        return panelId;
    }

    public void setPanelId(int panelId) {
        this.panelId = panelId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) panelId;
        hash += (int) contentId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PanelContentsPK)) {
            return false;
        }
        PanelContentsPK other = (PanelContentsPK) object;
        if (this.panelId != other.panelId) {
            return false;
        }
        if (this.contentId != other.contentId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.natty.web.persist.PanelContentsPK[panelId=" + panelId + ", contentId=" + contentId + "]";
    }

}
