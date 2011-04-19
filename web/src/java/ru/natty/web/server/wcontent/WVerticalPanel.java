package ru.natty.web.server.wcontent;

import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.diffpatchers.VerticalPanelDiffPatcher;

public class WVerticalPanel extends WComplexPanel
{
	public WVerticalPanel()
	{
		super (VerticalPanelDiffPatcher.class);
	}

	public static WContent make (Integer id, Parameters ps,
											 DataBase db, WContentCreator creator)
	{
		return WComplexPanel.make (id, ps, new WVerticalPanel(),
										   db, creator).setStyle(id, db);
	}

	public static WContent
			makeCustom (Integer id, Integer contentId,
						WContent view, Parameters ps,
						DataBase db, WContentCreator creator)
	{
		return WComplexPanel.makeCustom (id, contentId, view, ps,
												new WVerticalPanel(),
												db, creator).setStyle(id, db);
	}
}
