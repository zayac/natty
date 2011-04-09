/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.shared;

import ru.natty.web.client.IBasicButton;
import ru.natty.web.client.IWidget;

/**
 *
 * @author necto
 */
public class BasicButtonDP implements DiffPatcher
{
	String nStr;

	public BasicButtonDP (String str)
	{
		nStr = str;
	}

	public BasicButtonDP(){}

	@Override
	public IWidget createNew (int id)
	{
		return new IBasicButton (id, nStr);
	}

	@Override
	public void applay(IWidget w)
	{
		((IBasicButton)w).setText(nStr);
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
