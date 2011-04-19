/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.shared.diffpatchers;

import ru.natty.web.client.iwidget.ITextBox;
import ru.natty.web.client.iwidget.IWidget;

/**
 *
 * @author necto
 */
public class TextBoxDP extends DiffPatcher
{
	String nStr;

	public TextBoxDP (String str)
	{
		nStr = str;
	}

	public TextBoxDP(){}

	@Override
	protected IWidget createNewInt (int id)
	{
		return new ITextBox (id, nStr);
	}

	@Override
	protected void applayInt(IWidget w)
	{
		((ITextBox)w).setText(nStr);
	}

	@Override
	public boolean isVoid()
	{
		return false;
	}
	
	@Override
	public String toString() {
		return "<font color=#aa7777>TextBoxDP[ " + nStr + " ]</font>";
	}

}
