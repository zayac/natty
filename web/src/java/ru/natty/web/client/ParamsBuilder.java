/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client;

import com.allen_sauer.gwt.log.client.Log;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class ParamsBuilder
{
	static private ParamsBuilder instance = null;
	private ParamsBuilder () {}

	private Parameters last = new Parameters();
	private Parameters current = new Parameters();
	private Parameters sent = new Parameters();

	public static ParamsBuilder get()
	{
		if (null == instance) instance = new ParamsBuilder();
		return instance;
	}

	static public Parameters getCurrent()
	{
		return get().current;
	}

	static public Parameters getPrev ()
	{
		return get().last;
	}
	
	public void holdCurrent()
	{
		sent = current.copy();
	}

	public void requestSucceeded()
	{
		
		Log.debug ("id  succeeded, with params - " + sent);
		ParamsUrl.getInstance().putIntoHistory(sent);
		last = sent.copy();
	}
}
