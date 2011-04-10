package ru.natty.web.server;

import java.util.List;
import ru.natty.web.persist.ContentHeader;
import ru.natty.web.persist.PanelContents;

import ru.natty.web.server.WCompositePanelContent.UnitContent;
import ru.natty.web.shared.CompositePanelDP;
import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.TabPanelDP;

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

	public static WTabPanelContent make (Integer id, Parameters ps,
										 DataBase db, WContentCreator creator)
	{
        WTabPanelContent ret = new WTabPanelContent();

		ContentHeader.ByIdFinder getTname = db.getContentHeaderFinder();
        List<PanelContents> contents = db.queryPanelContentsById(id);

        Integer defaultTab = 0;

		ret.setActiveTab (defaultTab);

        for (PanelContents pc : contents)
        {
            ContentHeader tabLabel = getTname.find(pc.getContentId());

            if (pc.getOrdNumber().equals(defaultTab))
                ret.InsertTab (pc.getContentId(), tabLabel.getHeader(),
                               pc.getOrdNumber(),
                               creator.getContent (pc.getContentId(), ps));
            else
                ret.InsertTab (pc.getContentId(), tabLabel.getHeader(),
                               pc.getOrdNumber(), WVoid.make());
        }
        return ret;
	}

    public static WTabPanelContent makeCustom (Integer id, Integer contentId,
											   WContent view, Parameters ps,
											   DataBase db, WContentCreator creator)
    {
        WTabPanelContent ret = new WTabPanelContent();
		ContentHeader.ByIdFinder getTname = db.getContentHeaderFinder();
        List<PanelContents> pcs = db.queryPanelContentsById(id);

        for (PanelContents pc : pcs)
        {
            ContentHeader tabLabel = getTname.find(pc.getContentId());

            if (pc.getContentId().equals(contentId))
			{
                ret.InsertTab (pc.getContentId(), tabLabel.getHeader(),
                               pc.getOrdNumber(), view);
				ret.setActiveTab (pc.getOrdNumber());
			}
            else
                ret.InsertTab (pc.getContentId(), tabLabel.getHeader(),
                               pc.getOrdNumber(), WVoid.make());
        }
        return ret;
    }
}
