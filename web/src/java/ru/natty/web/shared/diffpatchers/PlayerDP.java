/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.shared.diffpatchers;

import ru.natty.web.client.iwidget.IPlayer;
import ru.natty.web.client.iwidget.IWidget;

/**
 *
 * @author necto
 */
public class PlayerDP extends DiffPatcher
{
	String url;

	public PlayerDP(){}
	public PlayerDP (String url)
	{
		this.url = url;
	}


	@Override
	public boolean isVoid()
	{
		return false;
	}

	@Override
	protected IWidget createNewInt(int id)
	{
		return new IPlayer (id, url);
	}

	@Override
	protected void applayInt(IWidget w)
	{
		((IPlayer)w).setNewUrl(url);
	}

	@Override
	public String toString()
	{
		return "PlayerDP:<font color#0010cc>" + url + "</font>";
	}

}
