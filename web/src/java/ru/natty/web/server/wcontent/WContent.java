package ru.natty.web.server.wcontent;

import javax.xml.crypto.Data;
import ru.natty.web.persist.GuiProperties;
import ru.natty.web.server.DataBase;
import ru.natty.web.shared.diffpatchers.DiffPatcher;

public abstract class WContent
{
	String style;
	
	public void setStyle (String st)
	{
		style = st;
	}

	public WContent setStyle (Integer id, DataBase db)
	{
		GuiProperties gp = db.queryGuiPropsById(id);
		if (null != gp.getStyle())
			setStyle(gp.getStyle());
		return this;
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
