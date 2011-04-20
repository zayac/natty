/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import ru.natty.persist.Album;
import ru.natty.persist.Artist;
import ru.natty.persist.Genre;
import ru.natty.persist.Track;
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

	public List<Artist> queryArtistByPattern (String pattern)
	{
		return Artist.queryByPattern (pattern, em);
	}

	public List<Album> queryAlbumByPattern (String pattern)
	{
		return Album.queryByPattern (pattern, em);
	}

	public List<Track> queryTrackByPattern (String pattern)
	{
		return Track.queryByPattern (pattern, em);
	}

	public List<Track> queryTrackByPatternWindowed (String pattern, Integer lim, Integer offset)
	{
		return Track.queryByPatternWindowed (pattern, em, lim, offset);
	}

	public Track queryTrackById (Integer id)
	{
		return Track.queryById (id, em);
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
