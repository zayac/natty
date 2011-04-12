package ru.natty.web.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javassist.tools.reflect.Reflection;
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
		try
		{
			log(Level.SEVERE, "getting content, id = " + id.toString());
			WidgetType wt = db.getWidgetType(id);
			Class wclass = Class.forName("ru.natty.web.server." + wt.getClassName());
			Method make = wclass.getMethod("make", Integer.class, Parameters.class,
											DataBase.class, WContentCreator.class);
			return (WContent)make.invoke (null, id, ps, db, this);
		}
		catch (Exception ex) {
			Logger.getLogger(WContentCreator.class.getName()).log(Level.SEVERE, null, ex);
			return new WLabel(ex.getMessage());
		}
    }


    private boolean isRoot (Integer id)
    {
        return id == 0;
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
//        log (Level.SEVERE, "getting custom content, id = " + id.toString());
//        WidgetType wt = db.getWidgetType (id);
//        switch (wt.getId())
//        {
//			case 2://vertical panel
//				return WVerticalPanel.makeCustom(id, contentId, view, ps, db, this);
//            case 3://tab panel
//                return WTabPanel.makeCustom(id, contentId, view, ps, db, this);
//			case 7://horizontal panel
//				return WHorizontalPanel.makeCustom(id, contentId, view, ps, db, this);
//			default:// TODO: !!! change exception machanism !!!
//                return new WLabel ("Not an aggregating content, named:" + wt.getName());
//        }//VVV Not tested yet VVV
		try
		{
			log (Level.SEVERE, "getting custom content, id = " + id.toString());
			WidgetType wt = db.getWidgetType (id);
			Class wclass = Class.forName("ru.natty.web.server." + wt.getClassName());
			Method make = wclass.getMethod("makeCustom", Integer.class, Integer.class,
											WContent.class, Parameters.class,
											DataBase.class, WContentCreator.class);
			return (WContent)make.invoke (null, id, contentId, view, ps, db, this);
		}
		catch (Exception ex) {
			Logger.getLogger(WContentCreator.class.getName()).log(Level.SEVERE, null, ex);
			return new WLabel(ex.getMessage());
		}
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
