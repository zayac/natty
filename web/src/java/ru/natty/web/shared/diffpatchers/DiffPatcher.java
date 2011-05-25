package ru.natty.web.shared.diffpatchers;

import java.io.Serializable;
import ru.natty.web.client.iwidget.IWidget;

public abstract class DiffPatcher implements Serializable
{
	private String style;
	private int rezHash;
	
	private IWidget applayStyle (IWidget w)
	{
		if (null != style)
			w.setStylePrimaryName(style);
		return w;
	}

	final public IWidget createNew (int id)
	{
		return applayStyle(createNewInt(id));
	}
	final public void applay (IWidget w)
	{
		applayInt(applayStyle(w));
	}

	abstract public boolean isVoid();
	abstract protected IWidget createNewInt (int id);
	abstract protected void applayInt (IWidget w);
	
	public void setStyle (String st)
	{
		style = st;
	}

	public int getRezHash()
	{
		return rezHash;
	}

	public DiffPatcher setRezHash (int rezHash)
	{
		this.rezHash = rezHash;
		return this;
	}
	
	abstract public String toString();
}
