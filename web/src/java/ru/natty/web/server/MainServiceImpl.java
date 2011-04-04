/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import ru.natty.web.client.MainService;
import ru.natty.persist.Genre;
import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class MainServiceImpl extends RemoteServiceServlet implements MainService
{

	private static WContentCreator gen = new WContentCreator();

    public String myMethod(String s)
    {
        // Do something interesting with 's' here on the server.

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Natty.webPU");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Query q = em.createNamedQuery("Genre.findAll");
        List genres = q.getResultList();
        String compilation = "";
        for (Object o : genres)
            compilation += ((Genre)o).getName();
        s += compilation;

//        Genre g = new Genre(s);
//        em.persist(g);

        em.getTransaction().commit();
        em.close();
        emf.close();

        return "Server says: " + s;
    }

	@Override
	public DiffPatcher getInitialContent (Integer id, Parameters p)
	{/*
		if (0 == id)
		{
			if (null == last_content)
			{
				last_content = gen.createAll();
				return last_content.getAllContent();
			}
			WContent content = gen.createAll();
			DiffPatcher ret = content.getDifference(last_content, true);
			last_content = content;
//			Location.
			return ret;
		}
		return gen.createBranch(id).getAllContent();*/
		return gen.createBranch(id).getAllContent();
	}
	@Override
	public DiffPatcher getDifference(Integer id, Parameters p, Integer prevId, Parameters prevP)
    {
		WContent content = gen.createBranch(id);
		WContent prev = gen.createBranch(prevId);
		DiffPatcher ret = content.getDifference(prev, true);
//		last_content = content;
		return new WLabelContent ("difference reply").getAllContent();//ret;
	}
}
