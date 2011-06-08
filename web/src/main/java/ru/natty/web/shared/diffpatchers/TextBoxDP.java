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
	private String name;
	private String nStr;

	public TextBoxDP (String name, String str)
	{
		this.name = name;
		nStr = str;
	}

	public TextBoxDP(){}

	@Override
	protected IWidget createNewInt (int id)
	{
		return new ITextBox (id, name, nStr);
	}

	@Override
	protected void applayInt(IWidget w)
	{
//		((ITextBox)w).setName(name);
//		((ITextBox)w).setText(nStr);
	}

	@Override
	public boolean isVoid()
	{
		return false;
	}
	
	@Override
	public String toString() {
		return "<font color=#aa7777>TextBoxDP\"" + name + "\"[ " + nStr + " ]</font>";
	}

}
