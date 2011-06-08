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
		DiffPatcher ret = applayStyle (getDifferenceInt (prev, amputation));
		if (null != ret) ret.setRezHash (hashCode());
		return ret;
	}
	
	public final DiffPatcher getAllContent()
	{
		DiffPatcher ret = applayStyle(getAllContentInt());
		if (null != ret) ret.setRezHash (hashCode());
		return ret;
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
	@Override
	public abstract int hashCode();


	public boolean isNotVoid() {return true;}
	public abstract boolean isAggregating();
}
