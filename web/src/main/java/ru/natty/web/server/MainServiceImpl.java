/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import ru.natty.web.server.wcontent.WContent;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ru.natty.web.client.MainService;
import ru.natty.web.shared.diffpatchers.DiffPatcher;
import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.ServerException;

/**
 *
 * @author necto
 */
public class MainServiceImpl extends RemoteServiceServlet implements MainService
{
	private static WContentCreator gen = new WContentCreator();


	@Override
	public DiffPatcher getInitialContent (Parameters p) throws IllegalArgumentException, ServerException
	{
		MyLog.info ("try to get content: " + p.getId());
		return gen.createContent (p).getAllContent();
	}
	
	@Override
	public DiffPatcher getDifference (Parameters p, Parameters prevP) throws IllegalArgumentException, ServerException
    {
		WContent content = gen.createContentBranch (p);
		WContent prev = gen.createContentBranch (prevP);
		DiffPatcher ret = content.getDifference (prev, true);
		return ret;
	}
}
