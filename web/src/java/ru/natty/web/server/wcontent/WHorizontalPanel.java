package ru.natty.web.server.wcontent;

import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.HorizontalPanelDiffPatcher;
import ru.natty.web.shared.Parameters;

public class WHorizontalPanel extends WComplexPanel
{
	public WHorizontalPanel()
	{
		super(HorizontalPanelDiffPatcher.class);
	}

	public static WComplexPanel make (Integer id, Parameters ps,
											 DataBase db, WContentCreator creator)
	{
		return WComplexPanel.make (id, ps,
										  new WHorizontalPanel(),
										  db, creator);
	}

	public static WComplexPanel
			makeCustom (Integer id, Integer contentId,
						WContent view, Parameters ps,
						DataBase db, WContentCreator creator)
	{
		return WComplexPanel.makeCustom (id, contentId, view, ps,
												new WHorizontalPanel(),
												db, creator);
	}
}
