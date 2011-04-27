/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.shared.diffpatchers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ru.natty.web.client.iwidget.ITextCellList;
import ru.natty.web.client.iwidget.IWidget;
import ru.natty.web.shared.IText;

/**
 *
 * @author necto
 */
public class TextCellListDP extends DiffPatcher
{
	Set<Integer> deletions;
	Map<Integer, IText> changes;
	Set<IText> creations;
	String name;
	Integer selected = null;

	public TextCellListDP()
	{
		deletions = new HashSet<Integer>();
		changes = new HashMap<Integer, IText>();
		creations = new HashSet<IText>();
	}

	public TextCellListDP (String name)
	{
		deletions = new HashSet<Integer>();
		changes = new HashMap<Integer, IText>();
		creations = new HashSet<IText>();

		this.name = name;
	}

	public void addCreation (IText text)
	{
		creations.add(text);
	}

	public void addCreations (Collection<? extends IText> text)
	{
		creations.addAll(text);
	}

	public void addChange (IText newText)
	{
		changes.put(newText.getId(), newText);
	}

	public void addDeletion (Integer id)
	{
		deletions.add(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSelected (Integer sel)
	{
		selected = sel;
	}


	@Override
	public boolean isVoid()
	{
		return false;
	}

	@Override
	protected IWidget createNewInt(int id)
	{
		ITextCellList tcl = new ITextCellList(id, name);
		tcl.getItems().addAll(creations);
		tcl.selectElement(selected);
		return tcl;
	}

	@Override
	protected void applayInt(IWidget w)
	{
		List<IText> items = ((ITextCellList)w).getItems();

		for (Iterator<IText> i = items.iterator(); i.hasNext();)
		{
			IText item = i.next();
			Integer id = item.getId();
			if (deletions.contains(id))
				i.remove();
			else
			{
				IText change = changes.get(id);
				if (null != change)
					item.setText(change.getText());
			}
		}
		items.addAll(creations);
		((ITextCellList)w).selectElement(selected);
	}

	public boolean vital()
	{
		return !(creations.isEmpty() && changes.isEmpty() && deletions.isEmpty() &&
				null == selected);
	}

	@Override
	public String toString()
	{
		return "TextCellList(: name=<font color = #aaca0c>" + name +
				"</font>, creations=" + creations + ", changes=" + changes +
				", deletions = " + deletions + ":)<" +
				((null != selected) ? selected : "n/s") + ">";
	}

}
