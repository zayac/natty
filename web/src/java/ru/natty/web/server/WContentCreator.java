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



    public WContent getContent (Integer id, Parameters ps)
    {
        log (Level.SEVERE, "getting content, id = " + id.toString());

        WidgetType wt = db.getWidgetType (id);
        switch (wt.getId())
        {
            case 1://label
                return WLabelContent.make (id, ps, db);
            case 2://vertical panel
                return WVerticalPanelContent.make(id, ps, db, this);
            case 3://tab panel
                return WTabPanelContent.make(id, ps, db, this);
            case 4://genres list
                return getGenresList (id, ps);
			case 5://Text box
				return WTextBoxContent.make(id, ps, db);
			case 6://Basic button
				return WBasicButtonContent.make(id, ps, db);
            case 7://Horizontal panel
                return WHorizontalPanelContent.make(id, ps, db, this);
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
											WContent view, Parameters ps)
    {
        log (Level.SEVERE, "getting custom content, id = " + id.toString());
        WidgetType wt = db.getWidgetType (id);
        switch (wt.getId())
        {
			case 2://vertical panel
				return WVerticalPanelContent.makeCustom(id, contentId, view, ps, db, this);
            case 3://tab panel
                return WTabPanelContent.makeCustom(id, contentId, view, ps, db, this);
			case 7://horizontal panel
				return WHorizontalPanelContent.makeCustom(id, contentId, view, ps, db, this);
			default:// TODO: !!! change exception machanism !!!
                return new WLabelContent ("Not an aggregating content, named:" + wt.getName());
        }
    }

    private boolean isRoot (Integer id)
    {
        return id == 0;
    }

    private WGenresList getGenresList (Integer id, Parameters ps)
    {
        return new WGenresList (db.queryGenreByPattern(ps.getQuery()));
    }

//	private void fillCustomPanel (Integer id, Integer contentId,
//								  WContent view, Parameters ps,
//								  WComplexPanelContent panel)
//	{
//        List<PanelContents> contents = db.queryPanelContentsById(id);
//        for (PanelContents pc : contents)
//        {
//			if (pc.getContentId().equals(contentId))
//				panel.addItem (pc.getContentId(), pc.getOrdNumber(), view);
//			else
//				panel.addItem (pc.getContentId(), pc.getOrdNumber(),
//					         getContent (pc.getContentId(), ps));
//        }
//	}
//
//    private WVerticalPanelContent getCustomVPanel (Integer id, Integer contentId,
//												   WContent view, Parameters ps)
//    {
//        WVerticalPanelContent ret = new WVerticalPanelContent();
//		fillCustomPanel(id, contentId, view, ps, ret);
//        return ret;
//    }
//
//    private WHorizontalPanelContent getCustomHPanel (Integer id, Integer contentId,
//												   WContent view, Parameters ps)
//    {
//        WHorizontalPanelContent ret = new WHorizontalPanelContent();
//		fillCustomPanel(id, contentId, view, ps, ret);
//        return ret;
//    }

    public WContent createContentBranch (Parameters ps)
    {
        db.startTransaction();
        WContent leaf = getContent (ps.getElementId(), ps);
        WContent ret = getAggregatingBranch (ps.getElementId(), leaf, ps);
        db.finishTransaction();
        return ret;
    }
}
