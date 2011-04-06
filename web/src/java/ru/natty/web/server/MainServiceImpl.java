/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
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


	@Override
	public DiffPatcher getInitialContent (Integer id, Parameters p)
	{
		return gen.createBranch(id).getAllContent();
	}
	@Override
	public DiffPatcher getDifference(Integer id, Parameters p, Integer prevId, Parameters prevP)
    {
		WContent content = gen.createBranch(id);
		WContent prev = gen.createBranch(prevId);
		//new com.google.gwt.logging.client.SystemLogHandler().publish(new LogRecord(Level.SEVERE, "prev id: " + prevId.toString()));
		
		DiffPatcher ret = content.getDifference(prev, true);
		return ret;//content.getAllContent();//gen.createBranch(0).getAllContent();//new WLabelContent ("difference reply").getAllContent();//ret;
	}
}
