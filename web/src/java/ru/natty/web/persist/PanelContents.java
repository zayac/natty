/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.persist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;

/**
 *
 * @author necto
 */
@Entity
@Table(name = "panel_content")
@NamedQueries({
    @NamedQuery(name = "PanelContents.findAll", query = "SELECT p FROM PanelContents p"),
    @NamedQuery(name = "PanelContents.findByPanelId", query = "SELECT p FROM PanelContents p WHERE p.panelContentsPK.panelId = :panelId"),
    @NamedQuery(name = "PanelContents.findByOrdNumber", query = "SELECT p FROM PanelContents p WHERE p.ordNumber = :ordNumber"),
    @NamedQuery(name = "PanelContents.findByContentId", query = "SELECT p FROM PanelContents p WHERE p.panelContentsPK.contentId = :contentId")})
public class PanelContents implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PanelContentsPK panelContentsPK;
    @Column(name = "ord_number")
    private Integer ordNumber;
    @JoinColumn(name = "content_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GuiProperties guiProperties;
    @JoinColumn(name = "panel_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private GuiProperties guiProperties1;

    public PanelContents() {
    }

    public PanelContents(PanelContentsPK panelContentsPK) {
        this.panelContentsPK = panelContentsPK;
    }

    public PanelContents(int panelId, int contentId) {
        this.panelContentsPK = new PanelContentsPK(panelId, contentId);
    }

    public PanelContentsPK getPanelContentsPK() {
        return panelContentsPK;
    }

    public Integer getContentId() {
        return panelContentsPK.getContentId();
    }


    public void setPanelContentsPK(PanelContentsPK panelContentsPK) {
        this.panelContentsPK = panelContentsPK;
    }

    public Integer getOrdNumber() {
        return ordNumber;
    }

    public void setOrdNumber(Integer ordNumber) {
        this.ordNumber = ordNumber;
    }

    public GuiProperties getGuiProperties() {
        return guiProperties;
    }

    public void setGuiProperties(GuiProperties guiProperties) {
        this.guiProperties = guiProperties;
    }

    public GuiProperties getGuiProperties1() {
        return guiProperties1;
    }

    public void setGuiProperties1(GuiProperties guiProperties1) {
        this.guiProperties1 = guiProperties1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (panelContentsPK != null ? panelContentsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PanelContents)) {
            return false;
        }
        PanelContents other = (PanelContents) object;
        if ((this.panelContentsPK == null && other.panelContentsPK != null) || (this.panelContentsPK != null && !this.panelContentsPK.equals(other.panelContentsPK))) {
            return false;
        }
        return true;
    }

    public static List<PanelContents> queryById (Integer id, EntityManager em)
    {
        Query getPanel = em.createNamedQuery ("PanelContents.findByPanelId");
        getPanel.setParameter ("panelId", id);
        List contents = getPanel.getResultList();
		List<PanelContents> pcs = new ArrayList<PanelContents>();

		for (Object o : contents)
			pcs.add ((PanelContents)o);
		return pcs;
    }

    @Override
    public String toString() {
        return "ru.natty.web.persist.PanelContents[panelContentsPK=" + panelContentsPK + "]";
    }

}
