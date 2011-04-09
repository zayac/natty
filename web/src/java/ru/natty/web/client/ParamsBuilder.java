/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client;

import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class ParamsBuilder
{
	static private ParamsBuilder instance = null;
	private ParamsBuilder () {}

	private Parameters last = new Parameters("", 0);
	private Parameters current = new Parameters("", 0);;

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

	public void requestSucceeded()
	{
		last = current.copy();
	}
}
