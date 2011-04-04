package ru.natty.web.shared;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import ru.natty.web.client.IStreak;
import ru.natty.web.client.IVoid;
import ru.natty.web.client.IWidget;

public abstract class CompositePanelDP implements DiffPatcher {

	static public abstract class Unit implements Serializable, Comparable<Unit>
	{
		public abstract void insertMe (IWidget  panel);
		public abstract void pushMe (IWidget tp);
		public Unit(){}
		public abstract String toString();
	}
	
	SortedSet<Unit> creations;
	Map<Integer, DiffPatcher> changes;
	Set<Integer> deletions;
	
	public CompositePanelDP()
	{
		creations = new TreeSet<Unit>();
		changes = new TreeMap<Integer, DiffPatcher>();
		deletions = new TreeSet<Integer>();
	}
	
	protected void addUnit (Unit act)
	{
		creations.add (act);
	}
	
	public void addChange (Integer id, DiffPatcher dp)
	{
		changes.put(id, dp);
	}
	
	public void addDeletion (Integer id)
	{
		deletions.add(id);
	}
	
	protected abstract IWidget create (Integer id);
	protected void finalInitialization (IWidget w) {/*do nothing*/}

	@Override
	public boolean isVoid()
	{
		return false;
	}
	
	@Override
	public IWidget createNew(int id) {
		IWidget panel = create(id);
		for (Unit u: creations)
			u.pushMe(panel);
		finalInitialization(panel);
			
		return panel;
	}

	@Override
	public void applay(IWidget w) {
		Iterable<IWidget> panel = (Iterable<IWidget>)w;
		
		for (Iterator<IWidget> i = panel.iterator(); i.hasNext();)
		{
			IWidget wid = i.next();
			int id = wid.getId();
			if (deletions.contains (id))
				i.remove();
			else
			{
				DiffPatcher dp = changes.get (id);
				if (null != dp)
					if (wid.isStreak())
					{
						IStreak cover = ((IStreak)wid); 
						if (cover.containsVoid())
							cover.alterContent(dp.createNew(cover.getContent().getId()));
						else
							if (dp.isVoid())
								cover.alterContent(new IVoid(cover.getContent().getId()));
							else
							dp.applay(cover.getContent());
					}
					else
						dp.applay(wid);
			}
		}
		
		for (Unit u: creations)
			u.insertMe(w);
		
		finalInitialization(w);
	}
	
	public boolean vital()
	{
		return !(creations.isEmpty() && changes.isEmpty() && deletions.isEmpty());
	}
}
