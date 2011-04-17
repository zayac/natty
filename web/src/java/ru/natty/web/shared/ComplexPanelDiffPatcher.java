package ru.natty.web.shared;

import ru.natty.web.client.iwidget.ComplexPanelI;
import ru.natty.web.client.iwidget.IWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

public abstract class ComplexPanelDiffPatcher extends CompositePanelDP//implements DiffPatcher
{
	static public class Creation extends Unit
	{
		DiffPatcher diff;
		Integer position;
		Integer id;
		
		public Creation()
		{
			diff = null;
			position = -1;
			id = -1;
		}

		public Creation (Integer position, Integer id,	DiffPatcher diff) {
			this.diff = diff;
			this.position = position;
			this.id = id;
		}
		
		public void insertMe (IWidget panel)
		{
			ComplexPanelI tp = (ComplexPanelI)panel;
			if (position.equals(0) && tp.getWidgetCount() == 0)
				tp.add(diff.createNew (id));
			else
				tp.insert(diff.createNew (id), position);
		}

		@Override
		public int compareTo(Unit o) {
			return (position.compareTo(((Creation)o).position));
		}

		@Override
		public void pushMe(IWidget panel) {
			((ComplexPanelI)panel).add(diff.createNew (id));
		}

		@Override
		public String toString() {
			return "<br><big>Creation</big> [" + diff + ", position=" + position
					+ ", id=<i>" + id + "</i>]";
		}
	}
	
	public void addCreature (Integer pos, Integer id, DiffPatcher diff)
	{
		addUnit(new Creation(pos, id, diff));
	}

	@Override
	public String toString() {
		return "<big>ComplexPanelDiffPatcher</big> [creations=" + creations + ", changes="
				+ changes + ", <br>deletions=" + deletions + "]";
	}
}
