package ru.natty.web.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import ru.natty.web.persist.ContentHeader;
import ru.natty.web.persist.GuiProperties;
import ru.natty.web.persist.PanelContents;
import ru.natty.web.persist.Label;
import ru.natty.web.persist.WidgetType;
import ru.natty.web.shared.Parameters;

public class WContentCreator
{
	DataBase db;

    private static com.google.gwt.logging.client.SystemLogHandler slh =
               new com.google.gwt.logging.client.SystemLogHandler();

    private static void log (Level l, String message)
    {
        slh.publish(new LogRecord(l, message));
    }

	public WContentCreator()
	{
		db = new DataBase();
	}



    private WContent getContent (Integer id, Parameters ps)
    {
        log (Level.SEVERE, "getting content, id = " + id.toString());

        WidgetType wt = db.getWidgetType (id);
        switch (wt.getId())
        {
            case 1://label
                return getLabel (id, ps);
            case 2://vertical panel
                return getVPanel (id, ps);
            case 3://tab panel
                return getTabPanel (id, ps);
            case 4://genres list
                return getGenresList (id, ps);
			case 5://Text box
				return getTextBox (id, ps);
			case 6://Basic button
				return getButton (id, ps);
            case 7://Horizontal panel
                return getHPanel (id, ps);
            default:
                return new WLabelContent("can't dispatch content named:" + wt.getName());
        }
    }

    private WContent getAggregatingBranch (Integer id, WContent content,
										   Parameters p)
    {
        log (Level.SEVERE, "getting aggregating branch , id = " + id.toString());
        if (isRoot(id)) return content;

        GuiProperties parent = db.queryGuiPropsById(id).getParent();
        WContent parentContent = getAggregatingContent (parent.getId(),
														id, content, p);
        return getAggregatingBranch (parent.getId(), parentContent, p);
    }

    private WContent getAggregatingContent (Integer id, Integer contentId,
											WContent view, Parameters p)
    {
        log (Level.SEVERE, "getting custom content, id = " + id.toString());
        WidgetType wt = db.getWidgetType (id);
        switch (wt.getId())
        {
			case 2://vertical panel
				return getCustomVPanel (id, contentId, view, p);
            case 3://tab panel
                return getCustomTabPanel (id, contentId, view, p);
			case 7://horizontal panel
				return getCustomHPanel (id, contentId, view, p);
			default:// TODO: !!! change exception macanism !!!
                return new WLabelContent ("Not an aggregating content, named:" + wt.getName());
        }
    }

    private WVoid createVoid()
    {
        return new WVoid();
    }

    private boolean isRoot (Integer id)
    {
        return id == 0;
    }

    private WLabelContent getLabel (Integer id, Parameters ps)
    {
        Label l = db.queryLabelById(id);
        return new WLabelContent(l.getText());
    }

    private WTextBoxContent getTextBox (Integer id, Parameters ps)
    {
        Label l = db.queryLabelById(id);
        return new WTextBoxContent (l.getText());
    }

    private WBasicButtonContent getButton (Integer id, Parameters ps)
    {
        Label l = db.queryLabelById(id);
        return new WBasicButtonContent (l.getText());
    }

    private WGenresList getGenresList (Integer id, Parameters ps)
    {
        return new WGenresList (db.queryGenreByPattern(ps.getQuery()));
    }

	private void fillPanel (Integer id, WComplexPanelContent ret, Parameters ps)
	{
		List<PanelContents> pcs = db.queryPanelContentsById(id);
        for (PanelContents pc : pcs)
            ret.addItem (pc.getContentId(), pc.getOrdNumber(),
                         getContent (pc.getContentId(), ps));
	}

    private WVerticalPanelContent getVPanel (Integer id, Parameters ps)
    {
        WVerticalPanelContent ret = new WVerticalPanelContent();
		fillPanel (id, ret, ps);
		return ret;
    }
    private WHorizontalPanelContent getHPanel (Integer id, Parameters ps)
    {
        WHorizontalPanelContent ret = new WHorizontalPanelContent();
		fillPanel (id, ret, ps);
		return ret;
    }

    private WTabPanelContent getTabPanel (Integer id, Parameters ps)
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
                               getContent (pc.getContentId(), ps));
            else
                ret.InsertTab (pc.getContentId(), tabLabel.getHeader(),
                               pc.getOrdNumber(), createVoid());
        }
        return ret;
    }

	private void fillCustomPanel (Integer id, Integer contentId,
								  WContent view, Parameters ps,
								  WComplexPanelContent panel)
	{
        List<PanelContents> contents = db.queryPanelContentsById(id);
        for (PanelContents pc : contents)
        {
			if (pc.getContentId().equals(contentId))
				panel.addItem (pc.getContentId(), pc.getOrdNumber(), view);
			else
				panel.addItem (pc.getContentId(), pc.getOrdNumber(),
					         getContent (pc.getContentId(), ps));
        }
	}

    private WVerticalPanelContent getCustomVPanel (Integer id, Integer contentId,
												   WContent view, Parameters ps)
    {
        WVerticalPanelContent ret = new WVerticalPanelContent();
		fillCustomPanel(id, contentId, view, ps, ret);
        return ret;
    }

    private WHorizontalPanelContent getCustomHPanel (Integer id, Integer contentId,
												   WContent view, Parameters ps)
    {
        WHorizontalPanelContent ret = new WHorizontalPanelContent();
		fillCustomPanel(id, contentId, view, ps, ret);
        return ret;
    }

    private WTabPanelContent getCustomTabPanel (Integer id, Integer contentId,
												WContent view, Parameters ps)
    {
        WTabPanelContent ret = new WTabPanelContent();
		ContentHeader.ByIdFinder getTname = db.getContentHeaderFinder();
        List<PanelContents> contents = db.queryPanelContentsById(id);

        for (PanelContents pc : contents)
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
                               pc.getOrdNumber(), createVoid());
        }
        return ret;
    }

    public WContent createContentBranch (Parameters ps)
    {
        db.startTransaction();
        WContent leaf = getContent (ps.getElementId(), ps);
        WContent ret = getAggregatingBranch (ps.getElementId(), leaf, ps);
        db.finishTransaction();
        return ret;
    }
}
