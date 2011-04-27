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
import ru.natty.persist.QueryList;
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
	static public class Query<T>
	{
		javax.persistence.Query q;

		public Query (javax.persistence.Query q)	{this.q = q;}

		public T getResult()
		{
			return (T)q.getSingleResult();
		}

		public Query setWindow (int offset, int limit)
		{
			q.setFirstResult(offset);
			q.setMaxResults(limit);

			return this;
		}

		public List<T> getResults()
		{
			return QueryList.forQuery(q).<T>getAllResults();
		}

	}

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
        //if (null != emf) emf.close();
        //emf = null;
		em = null;
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

	public Query<Genre> queryGenreByPattern (String pattern)
	{
		return new Query<Genre> (Genre.getQueryByPattern (pattern, em));
	}

	public Query<Artist> queryArtistByPattern (String pattern)
	{
		return new Query<Artist> (Artist.getQueryByPattern (pattern, em));
	}

	public Query<Album> queryAlbumByPattern (String pattern)
	{
		return new Query<Album> (Album.getQueryByPattern (pattern, em));
	}

	public Query<Track> queryTrackByPattern (String pattern)
	{
		return new Query<Track> (Track.getQueryByPattern (pattern, em));
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
