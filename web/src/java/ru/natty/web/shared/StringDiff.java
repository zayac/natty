package ru.natty.web.shared;

import ru.natty.web.client.iwidget.ILabel;
import ru.natty.web.client.iwidget.IWidget;

public class StringDiff extends DiffPatcher
{
	String nStr;
	
	public StringDiff (String nStr)
	{
		this.nStr = nStr;
	}
	
	public void applay (ILabel l)
	{
		l.setText(nStr);
	}
	
	public StringDiff(){}

	protected void applayInt (IWidget w)
	{
		applay((ILabel) w);
	}

	@Override
	public boolean isVoid()
	{
		return false;
	}
	
	@Override
	protected IWidget createNewInt (int id)
	{
		return new ILabel (id, nStr);
	}

	@Override
	public String toString() {
		return "StringDiff [<font color=#ff0000>" + nStr + "</font>]";
	}
}
