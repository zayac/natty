/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.shared;

import ru.natty.web.client.ITextBox;
import ru.natty.web.client.IWidget;

/**
 *
 * @author necto
 */
public class TextBoxDP implements DiffPatcher
{
	String nStr;

	public TextBoxDP (String str)
	{
		nStr = str;
	}

	public TextBoxDP(){}

	@Override
	public IWidget createNew (int id)
	{
		return new ITextBox (id, nStr);
	}

	@Override
	public void applay(IWidget w)
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
