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
		String item = "";
		item += "elem.id=" + p.getElementId();
		item += "&query=" + p.getQuery();
		History.newItem(item);
	}
	
	public void getFromHistory (Parameters p)
	{
		String url = History.getToken();
		if (url.isEmpty()) return;
		
		String[] table = url.split("&");
		assert table.length >= 0 : "There must be at least 2 arguments";
		assert table[0].matches("elem\\.id=[0-9]+");
		assert table[1].matches("query=.*");

		p.setElementId (Integer.parseInt(table[0].split("=")[1]));
		p.setQuery(table[1].split("=")[1]);
	}
}
