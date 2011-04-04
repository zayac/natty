/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
@RemoteServiceRelativePath("mainservice")
public interface MainService extends RemoteService {
    public String myMethod(String s);

	DiffPatcher getInitialContent (Integer id, Parameters p);
	DiffPatcher getDifference(Integer id, Parameters p, Integer prevId,
			Parameters prevP);
}
