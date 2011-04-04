package ru.natty.web.server;

import java.util.Map;
import java.util.TreeMap;

import ru.natty.web.server.WCompositePanelContent.UnitContent;
import ru.natty.web.shared.CompositePanelDP;
import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.TabPanelDP;
import ru.natty.web.shared.TabPanelDP.TabUnit;

public class WTabPanelContent extends WCompositePanelContent
{	
	static public class Tab extends UnitContent
	{
		String label;
		Integer pos;
		
		public Tab(String label, Integer pos, WContent content) {
			super (content);
			this.label = label;
			this.pos = pos;
		}
		
		public void bringContentTo (CompositePanelDP cpdp, Integer id)
		{
			((TabPanelDP)cpdp).addTab(pos, id, label, content.getAllContent());
		}

		public UnitContent copy() {
			return new Tab(label, pos, content.copy());
		}

		@Override
		public String toString() {
			return "Tab [label=" + label + ", pos=" + pos + ", content="
					+ content + "]";
		}
		
	}
	
	Integer activeTab;
	
	public WTabPanelContent() {
		super();
		activeTab = -1;// no active tab;
	}
	
	public void InsertTab (Integer id, String label, Integer pos, WContent content)
	{
		contents.put(id, new Tab(label, pos, content));
	}
	
	public void setActiveTab (Integer num)
	{
		activeTab = num;
	}
	
	@Override
	protected CompositePanelDP create() {
		return new TabPanelDP (activeTab);
	}

	@Override
	protected WCompositePanelContent makeMe()
	{
		return new WTabPanelContent();
	}

	@Override
	public String toString() {
		return "WTabPanelContent [activeTab=" + activeTab + ", contents="
				+ contents + "]";
	}
	
}
