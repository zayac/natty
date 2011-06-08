/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.natty.web.shared.diffpatchers.DiffPatcher;
import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.ServerException;

/**
 *
 * @author necto
 */
@RemoteServiceRelativePath("mainservice")
public interface MainService extends RemoteService
{
	public abstract DiffPatcher getInitialContent (Parameters p) throws IllegalArgumentException, ServerException;
	public abstract DiffPatcher getDifference(Parameters p, Parameters prevP) throws IllegalArgumentException, ServerException;
}
