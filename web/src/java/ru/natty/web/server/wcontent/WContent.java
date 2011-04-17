package ru.natty.web.server.wcontent;

import ru.natty.web.shared.DiffPatcher;

public abstract class WContent
{
	String style;
	
	public void setStyle (String st)
	{
		style = st;
	}

	public DiffPatcher applayStyle (DiffPatcher dp)
	{
		if (null == dp) return null;
		dp.setStyle(style);
		return dp;
	}

	public final DiffPatcher getDifference (WContent prev, boolean amputation)
	{
		return applayStyle(getDifferenceInt(prev, amputation));
	}
	public final DiffPatcher getAllContent()
	{
		return applayStyle(getAllContentInt());
	}
	public final WContent copy()
	{
		WContent inst = copyInt();
		inst.setStyle(style);
		return inst;
	}

	protected abstract DiffPatcher getDifferenceInt (WContent prev, boolean amputation);
	protected abstract DiffPatcher getAllContentInt();
	protected abstract WContent copyInt();


	public boolean isNotVoid() {return true;}
	public abstract boolean isAggregating();
}
