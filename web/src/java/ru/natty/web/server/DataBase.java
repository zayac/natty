/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import ru.natty.web.persist.ContentHeader;
import ru.natty.web.persist.GuiProperties;
import ru.natty.web.persist.Label;
import ru.natty.web.persist.PanelContents;
import ru.natty.web.persist.WidgetType;

/**
 *
 * @author necto
 */
public class DataBase
{
    private EntityManagerFactory emf;
    private static com.google.gwt.logging.client.SystemLogHandler slh =
               new com.google.gwt.logging.client.SystemLogHandler();
    
    private static void log (Level l, String message)
    {
        slh.publish(new LogRecord(l, message));
    }

    DataBase()
    {
        emf = null;
    }

    private EntityManager startTransaction()
    {
        emf = Persistence.createEntityManagerFactory("Natty.webPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        return em;
    }

    private void finishTransaction (EntityManager em)
    {
        em.getTransaction().commit();
        em.close();
        if (null != emf) emf.close();
        emf = null;
    }

    private WidgetType getWidgetType (EntityManager em, Integer id)
    {
        Query getProps = em.createNamedQuery("GuiProperties.findById");
        getProps.setParameter("id", id);
        GuiProperties gp = (GuiProperties)getProps.getSingleResult();
        return gp.getWidgetType();
    }

    private WContent getContent (EntityManager em, Integer id)
    {
        log (Level.SEVERE, "getting content, id = " + id.toString());

        WidgetType wt = getWidgetType(em, id);
        switch (wt.getId())
        {
            case 1://label
                return getLabel (em, id);
            case 2://vertical panel
                return getVPanel (em, id);
            case 3://tab panel
                return getTabPanel (em, id);
            default:
                return new WLabelContent("can't dispatch content named:" + wt.getName());
        }
    }

    private WContent getAggregatingBranch (EntityManager em, Integer id, WContent content)
    {
        log (Level.SEVERE, "getting aggregating branch , id = " + id.toString());
        if (isRoot(id)) return content;
        GuiProperties props = GuiProperties.queryById(id, em);
        GuiProperties parent = props.getParent();
        WContent parentContent = getAggregatingContent (em, parent.getId(), id, content);
        return getAggregatingBranch (em, parent.getId(), parentContent);
    }

    private WContent getAggregatingContent (EntityManager em, Integer id, Integer contentId, WContent view)
    {
        log (Level.SEVERE, "getting custom content, id = " + id.toString());
        WidgetType wt = getWidgetType(em, id);
        switch (wt.getId())
        {
            case 3://tab panel
                return getCustomTabPanel(em, id, contentId, view);
            default:
                return new WLabelContent("Not an aggregating content, named:" + wt.getName());
        }
    }

    private WVoid createVoid()
    {
        return new WVoid();
    }

    private boolean isRoot (Integer id)
    {
        return id == 0;
    }

    private WLabelContent getLabel (EntityManager em, Integer id)
    {
        Query getLab = em.createNamedQuery("Label.findById");
        getLab.setParameter("id", id);
        Label l = (Label)getLab.getSingleResult();
        return new WLabelContent(l.getText());
    }

    private WVerticalPanelContent getVPanel (EntityManager em, Integer id)
    {
        WVerticalPanelContent ret = new WVerticalPanelContent();

        Query getPanel = em.createNamedQuery ("PanelContents.findByPanelId");
        getPanel.setParameter ("panelId", id);
        List contents = getPanel.getResultList();
        for (Object o : contents)
        {
            PanelContents pc = (PanelContents)o;
            ret.addItem (pc.getContentId(), pc.getOrdNumber(),
                         getContent(em, pc.getContentId()));
        }
        return ret;
    }

    private WTabPanelContent getTabPanel (EntityManager em, Integer id)
    {
        WTabPanelContent ret = new WTabPanelContent();

        Query getPanel = em.createNamedQuery ("PanelContents.findByPanelId");
        getPanel.setParameter ("panelId", id);
        Query getTname = em.createNamedQuery ("ContentHeaders.findByContentId");
        List contents = getPanel.getResultList();

        Integer defaultTab = 0;

        for (Object o : contents)
        {
            PanelContents pc = (PanelContents)o;
            getTname.setParameter("contentId", pc.getContentId());
            ContentHeader tabLabel = (ContentHeader)getTname.getSingleResult();

            if (pc.getOrdNumber().equals(defaultTab))
                ret.InsertTab (pc.getContentId(), tabLabel.getHeader(),
                               pc.getOrdNumber(),
                               getContent(em, pc.getContentId()));
            else
                ret.InsertTab (pc.getContentId(), tabLabel.getHeader(),
                               pc.getOrdNumber(), createVoid());
        }
        return ret;
    }

    private WTabPanelContent getCustomTabPanel (EntityManager em, Integer id,
                                                Integer contentId, WContent view)
    {
        WTabPanelContent ret = new WTabPanelContent();

        Query getPanel = em.createNamedQuery ("PanelContents.findByPanelId");
        getPanel.setParameter ("panelId", id);
        Query getTname = em.createNamedQuery ("ContentHeaders.findByContentId");
        List contents = getPanel.getResultList();

        for (Object o : contents)
        {
            PanelContents pc = (PanelContents)o;
            getTname.setParameter("contentId", pc.getContentId());
            ContentHeader tabLabel = (ContentHeader)getTname.getSingleResult();

            if (pc.getContentId().equals(contentId))
                ret.InsertTab (pc.getContentId(), tabLabel.getHeader(),
                               pc.getOrdNumber(), view);
            else
                ret.InsertTab (pc.getContentId(), tabLabel.getHeader(),
                               pc.getOrdNumber(), createVoid());
        }
        return ret;
    }

    public WContent createContent (Integer id)
    {
        EntityManager em = startTransaction();
        WContent ret = getContent(em, id);
        finishTransaction(em);
        return ret;
    }

    public WContent createContentBranch (Integer id)
    {
        EntityManager em = startTransaction();
        WContent leaf = getContent(em, id);
        WContent ret = getAggregatingBranch(em, id, leaf);
        finishTransaction(em);
        return ret;
    }
}
