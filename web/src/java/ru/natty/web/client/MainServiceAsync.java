/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public interface MainServiceAsync {
    public void myMethod(String s, AsyncCallback<String> callback);
	void getInitialContent(Integer id, Parameters p, AsyncCallback<DiffPatcher> callback);
	void getDifference (Integer id, Parameters p, Integer prevId,
			Parameters prevP, AsyncCallback<DiffPatcher> callback);
}
