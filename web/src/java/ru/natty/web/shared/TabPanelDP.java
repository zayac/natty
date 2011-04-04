package ru.natty.web.shared;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import ru.natty.web.client.ILabel;
import ru.natty.web.client.IStreak;
import ru.natty.web.client.ITabPanel;
import ru.natty.web.client.IWidget;
import ru.natty.web.client.Identified;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

public class TabPanelDP extends CompositePanelDP
{
	static public class TabUnit extends Unit
	{
		String tabText;
		DiffPatcher diff;
		Integer position;
		Integer id;
		
		public TabUnit()
		{
			tabText = "Wrong tab!!!";
			diff = null;
			position = -1;
			id = -1;
		}

		public TabUnit(String tabText, Integer position, Integer id,
				DiffPatcher diff) {
			this.tabText = tabText;
			this.diff = diff;
			this.position = position;
			this.id = id;
		}
		
		public void insertMe (IWidget panel)
		{
			ITabPanel tp = (ITabPanel)panel;
			if (position.equals(0) && tp.getWidgetCount() == 0)
				tp.add(diff.createNew (id), tabText);
			else
				tp.insert(diff.createNew (id), tabText, position);
		}

		@Override
		public int compareTo(Unit o) {
			return (position.compareTo(((TabUnit)o).position));
		}

		@Override
		public void pushMe(IWidget panel) {
			((ITabPanel)panel).add(diff.createNew(id), tabText);
		}

		@Override
		public String toString() {
			return "<big>TabUnit</big> <b>[</b>tabText=<font color=#DD3300>" + tabText + "</font>, diff=" + diff
					+ ", pos=" + position + ", id=<i>" + id + "</i><b>]</b>";
		}
	}
	
	private Integer activeTab;
	
	public TabPanelDP()
	{
		super();
		activeTab = -1;
	}
	public TabPanelDP (Integer activeTab)
	{
		super();
		this.activeTab = activeTab;
	}
	
	public void addTab (Integer position, Integer id, String tabText, DiffPatcher dp)
	{
		addUnit(new TabUnit (tabText, position, id, dp));
	}
	
	protected void finalInitialization (IWidget w)
	{
		if (activeTab >= 0)
			((ITabPanel)w).setActiveTab(activeTab);
	}
	protected IWidget create (Integer id)
	{
		return new ITabPanel(id);
	}
	@Override
	public String toString() {
		return "TabPanelDP <b><i>[</i></b>activeTab=" + activeTab + ", creations="
				+ creations + ", changes=" + changes + ", deletions="
				+ deletions + "<b><i>]</i></b>";
	}
}
