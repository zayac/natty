/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.natty.web.shared.diffpatchers.DiffPatcher;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public interface MainServiceAsync {
	public abstract void getInitialContent(Parameters p, AsyncCallback<DiffPatcher> callback);
	public abstract void getDifference (Parameters p,
			Parameters prevP, AsyncCallback<DiffPatcher> callback);
}
