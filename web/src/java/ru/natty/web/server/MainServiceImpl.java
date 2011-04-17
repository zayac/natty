/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import ru.natty.web.server.wcontent.WContent;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ru.natty.web.client.MainService;
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
	public DiffPatcher getInitialContent (Parameters p)
	{
		return gen.createContentBranch (p).getAllContent();
	}
	@Override
	public DiffPatcher getDifference (Parameters p, Parameters prevP)
    {
		WContent content = gen.createContentBranch (p);
		WContent prev = gen.createContentBranch (prevP);
		
		DiffPatcher ret = content.getDifference (prev, true);
		return ret;
	}
}
