package ru.natty.web.server;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import ru.natty.web.server.wcontent.WContent;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import ru.natty.web.persist.GuiProperties;
import ru.natty.web.persist.WidgetType;
import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.ServerException;

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

	private Class getContentClass (WidgetType wt) throws ClassNotFoundException
	{
		return Class.forName("ru.natty.web.server.wcontent." + wt.getClassName());
	}

    public WContent getContent (Integer id, Parameters ps) throws ServerException//, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException
    {
		try
		{
			WidgetType wt = db.getWidgetType (id);
			Class wclass = getContentClass(wt);
			Method make = wclass.getMethod("make", Integer.class, Parameters.class,
											DataBase.class, WContentCreator.class);
			return (WContent)make.invoke (null, id, ps, db, this);
		}
		catch (InvocationTargetException ex) {
			throw new ServerException (ex.toString() + "{" + ex.getCause().toString() + "} on " + id);
		}
		catch (Exception ex) {
			throw new ServerException (ex.toString() + " on " + id);
		}
    }


    private boolean isRoot (Integer id)
    {
        return id == 0;
    }

    private WContent getAggregatingBranch (Integer id, WContent content,
										   Parameters p) throws IllegalArgumentException, ServerException, ClassNotFoundException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        log (Level.SEVERE, "getting aggregating branch , id = " + id.toString());
        if (isRoot(id)) return content;

        GuiProperties parent = db.queryGuiPropsById(id).getParent();
        WContent parentContent = getAggregatingContent (parent.getId(),
														id, content, p);
        return getAggregatingBranch (parent.getId(), parentContent, p);
    }

    private WContent getAggregatingContent (Integer id, Integer contentId,
											WContent view, Parameters ps) throws ServerException//IllegalArgumentException, ServerException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
		try {
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
						
						WidgetType wt = db.getWidgetType (id);
						Class wclass = getContentClass(wt);
						Method make = wclass.getMethod("makeCustom", Integer.class, Integer.class,
														WContent.class, Parameters.class,
														DataBase.class, WContentCreator.class);
						return (WContent)make.invoke (null, id, contentId, view, ps, db, this);
			}
		catch (Exception ex) {
			throw new ServerException (ex.toString() + " on " + id);
		}
    }

    public WContent createContentBranch (Parameters ps) throws IllegalArgumentException, ServerException
    {
		try {
			db.startTransaction();
			WContent leaf = getContent (ps.getId(), ps);
			WContent ret = getAggregatingBranch (ps.getId(), leaf, ps);
			db.finishTransaction();
			return ret;
		} catch (IllegalAccessException ex) {
			throw new ServerException ("illeg access ex: " + ex);
		} catch (NoSuchMethodException ex) {
			throw new ServerException ("no such method ex: " + ex);
		} catch (ClassNotFoundException ex) {
			throw new ServerException ("class not found ex: " + ex);
		} catch (InvocationTargetException ex) {
			throw new ServerException ("invoc targ ex: " + ex.getCause().getMessage());
		}
    }
	
	public WContent createContent (Parameters ps) throws ServerException
	{
			db.startTransaction();
			WContent cont = getContent (ps.getId(), ps);
			db.finishTransaction();
			return cont;
	}
}
