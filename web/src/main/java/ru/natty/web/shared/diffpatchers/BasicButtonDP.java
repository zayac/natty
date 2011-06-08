/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.shared.diffpatchers;

import ru.natty.web.client.iwidget.IBasicButton;
import ru.natty.web.client.iwidget.IWidget;

/**
 *
 * @author necto
 */
public class BasicButtonDP extends DiffPatcher
{
	String nStr;

	public BasicButtonDP (String str)
	{
		nStr = str;
	}

	public BasicButtonDP(){}

	@Override
	protected IWidget createNewInt (int id)
	{
		return new IBasicButton (id, nStr);
	}

	@Override
	protected void applayInt (IWidget w)
	{
		((IBasicButton)w).setText (nStr);
	}

	@Override
	public boolean isVoid()
	{
		return false;
	}

	@Override
	public String toString()
	{
		return "<font color=#aaaaaa>BasicButtonDP[ " + nStr + " ]</font>";
	}
}
