/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client;

import com.google.gwt.user.client.History;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class ParamsUrl
{
	static private ParamsUrl instance = new ParamsUrl();
	private ParamsUrl() {}
	
	static public ParamsUrl getInstance() {return instance;}
	
	public void putIntoHistory (Parameters p)
	{
		History.newItem(p.toString());
	}
	
	public void getFromHistory (Parameters p)
	{
		String url = History.getToken();
		p.byString(url);
	}
}
