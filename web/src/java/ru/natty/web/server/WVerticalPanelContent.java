package ru.natty.web.server;

import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.VerticalPanelDiffPatcher;

public class WVerticalPanelContent extends WComplexPanelContent
{
	public WVerticalPanelContent()
	{
		super (VerticalPanelDiffPatcher.class);
	}

	public static WComplexPanelContent make (Integer id, Parameters ps,
											 DataBase db, WContentCreator creator)
	{
		return WComplexPanelContent.make (id, ps,
										  new WVerticalPanelContent(),
										  db, creator);
	}

	public static WComplexPanelContent
			makeCustom (Integer id, Integer contentId,
						WContent view, Parameters ps,
						DataBase db, WContentCreator creator)
	{
		return WComplexPanelContent.makeCustom (id, contentId, view, ps,
												new WVerticalPanelContent(),
												db, creator);
	}
}
