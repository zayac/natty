package ru.natty.web.server;

import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.VerticalPanelDiffPatcher;

public class WVerticalPanel extends WComplexPanel
{
	public WVerticalPanel()
	{
		super (VerticalPanelDiffPatcher.class);
	}

	public static WComplexPanel make (Integer id, Parameters ps,
											 DataBase db, WContentCreator creator)
	{
		return WComplexPanel.make (id, ps,
										  new WVerticalPanel(),
										  db, creator);
	}

	public static WComplexPanel
			makeCustom (Integer id, Integer contentId,
						WContent view, Parameters ps,
						DataBase db, WContentCreator creator)
	{
		return WComplexPanel.makeCustom (id, contentId, view, ps,
												new WVerticalPanel(),
												db, creator);
	}
}
