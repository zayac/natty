package ru.natty.web.server;

import ru.natty.web.shared.HorizontalPanelDiffPatcher;
import ru.natty.web.shared.Parameters;

public class WHorizontalPanelContent extends WComplexPanelContent
{
	public WHorizontalPanelContent()
	{
		super(HorizontalPanelDiffPatcher.class);
	}

	public static WComplexPanelContent make (Integer id, Parameters ps,
											 DataBase db, WContentCreator creator)
	{
		return WComplexPanelContent.make (id, ps,
										  new WHorizontalPanelContent(),
										  db, creator);
	}

	public static WComplexPanelContent
			makeCustom (Integer id, Integer contentId,
						WContent view, Parameters ps,
						DataBase db, WContentCreator creator)
	{
		return WComplexPanelContent.makeCustom (id, contentId, view, ps,
												new WHorizontalPanelContent(),
												db, creator);
	}
}
