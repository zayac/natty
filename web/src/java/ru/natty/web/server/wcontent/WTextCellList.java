/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.IText;
import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.diffpatchers.DiffPatcher;
import ru.natty.web.shared.diffpatchers.TextCellListDP;

/**
 *
 * @author necto
 */
public class WTextCellList extends WContent
{
	static private final Integer MAXIMUM_ITEMS = 20;

	public static interface Translator<T>
	{
		List<IText> translate (List <? extends T> list);
	}

	String name;
	Map<Integer, IText> items;
	Integer selected = null;
	Integer start = 0;

	public WTextCellList (String name)
	{
		this.name = name;
		this.items = new HashMap<Integer, IText>();
	}

	public WTextCellList (String name, Map<Integer, IText> items)
	{
		this.name = name;
		this.items = new HashMap<Integer, IText>();
		this.items.putAll(items);
	}

	public void addText (IText t)
	{
		items.put(t.getId(), t);
	}

	public void setSelection (Integer sel)
	{
		this.selected = sel;
	}

	public void setStart (Integer start)
	{
		this.start = start;
	}

	public void addTexts (Collection<? extends IText> col)
	{
		for (IText t : col)
			addText (t);
	}

	@Override
	protected DiffPatcher getDifferenceInt (WContent prev, boolean amputation)
	{
		TextCellListDP dp = new TextCellListDP (name);
		WTextCellList tcl = ((WTextCellList)prev);
		Map<Integer, IText> that = ((WTextCellList)prev).items;

		for (Integer id : that.keySet())
			if (!items.containsKey(id))
				dp.addDeletion(id);

		for (Map.Entry<Integer, IText> entry: items.entrySet())
			if (that.containsKey (entry.getKey()))
			{
				if (!that.get(entry.getKey()).equals(entry.getValue()))
					dp.addChange(entry.getValue());
			}
			else
				dp.addCreation(entry.getValue());
		if (null != tcl.selected)
		{
			if (!tcl.selected.equals(selected))
				dp.setSelected (selected);
		}
		else
			if (selected != null)
				dp.setSelected (selected);

		dp.setStart(start);//Even if starts differ, but all rest is the same
							//assume it as the same
			
		if (dp.vital())
			return dp;
		return null;
	}

	@Override
	protected DiffPatcher getAllContentInt()
	{
		TextCellListDP dp = new TextCellListDP (name);
		for (Map.Entry<Integer, IText> entry: items.entrySet())
			dp.addCreation(entry.getValue());
		dp.setSelected (selected);
		dp.setStart(start);
		return dp;
	}

	@Override
	protected WContent copyInt()
	{
		return new WTextCellList(name, items);
	}

	@Override
	public boolean isAggregating()
	{
		return false;
	}

	public static WContent make (Integer id, Parameters ps,
								 DataBase db, WContentCreator creator)
	{
		WTextCellList tcl = new WTextCellList("bugaga");
		tcl.addText(new IText(10, "First label"));
		tcl.addText(new IText(14, "Sec on label"));
		tcl.addText(new IText(45, "And 45 more label"));
        return tcl.setStyle(id, db);
	}

	public static <T> WContent make (Integer id, Parameters ps,
								 DataBase db, WContentCreator creator,
								 String name, DataBase.Query<T> query,
								 Translator<? super T> trans)
	{
		Integer start = 0;
		if (ps.hasParam(name + ".start"))
			start = ps.getIntVal(name + ".start");

		WTextCellList tcl = new WTextCellList (name);
		query.setWindow (start, MAXIMUM_ITEMS);
		List<IText> items = trans.translate (query.getResults());
		tcl.addTexts (items);

		if (ps.hasParam (name))
			tcl.setSelection (ps.getIntVal (name));
		
		tcl.setStart(start);


        return tcl.setStyle (id, db);
	}


	@Override
	public int hashCode() {
		int hash = 0;
		for (Map.Entry<Integer, IText> item : items.entrySet())
			hash ^= item.getValue().hashCode();
		return hash;
	}
}
