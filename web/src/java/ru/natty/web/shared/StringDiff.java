package ru.natty.web.shared;

import ru.natty.web.client.ILabel;
import ru.natty.web.client.IWidget;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class StringDiff implements DiffPatcher
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

	public void applay(IWidget w)
	{
		applay((ILabel) w);
	}

	@Override
	public boolean isVoid()
	{
		return false;
	}
	
	@Override
	public IWidget createNew (int id)
	{
		return new ILabel (id, nStr);
	}

	@Override
	public String toString() {
		return "StringDiff [<font color=#ff0000>" + nStr + "</font>]";
	}
}
