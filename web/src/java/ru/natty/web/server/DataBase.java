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
import ru.natty.persist.Genre;
import ru.natty.web.persist.ContentHeader;
import ru.natty.web.persist.GuiProperties;
import ru.natty.web.persist.Label;
import ru.natty.web.persist.PanelContents;
import ru.natty.web.persist.WidgetType;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class DataBase
{
    private EntityManagerFactory emf;
    private EntityManager em;

    DataBase()
    {
        emf = null;
		em = null;
    }

    public void startTransaction()
    {
		finishTransaction();
        emf = Persistence.createEntityManagerFactory("Natty.webPU");
        em = emf.createEntityManager();

        em.getTransaction().begin();
    }

    public void finishTransaction()
    {
		if (null != em)
		{
			em.getTransaction().commit();
			em.close();
		}
        if (null != emf) emf.close();
		em = null;
        emf = null;
    }

	public GuiProperties queryGuiPropsById (Integer id)
	{
		return GuiProperties.queryById (id, em);
	}

    public WidgetType getWidgetType (Integer id)
    {
        GuiProperties gp = GuiProperties.queryById (id, em);
        return gp.getWidgetType();
    }

	public Label queryLabelById (Integer id)
	{
		return Label.queryById (id, em);
	}

	public List<Genre> queryGenreByPattern (String pattern)
	{
		return Genre.queryByPattern (pattern, em);
	}

	public ContentHeader.ByIdFinder getContentHeaderFinder()
	{
		return new ContentHeader.ByIdFinder(em);
	}

	public List<PanelContents> queryPanelContentsById (Integer id)
	{
		return PanelContents.queryById(id, em);
	}
}
