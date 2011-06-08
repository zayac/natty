package ru.natty.web.server.wcontent;

import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.diffpatchers.HorizontalPanelDiffPatcher;
import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.ServerException;

public class WHorizontalPanel extends WComplexPanel
{
	public WHorizontalPanel()
	{
		super(HorizontalPanelDiffPatcher.class);
	}

	public static WContent make (Integer id, Parameters ps,
											 DataBase db, WContentCreator creator) throws ServerException
	{
		return WComplexPanel.make (id, ps, new WHorizontalPanel(),
										   db, creator).setStyle(id, db);
	}

	public static WContent
			makeCustom (Integer id, Integer contentId,
						WContent view, Parameters ps,
						DataBase db, WContentCreator creator) throws ServerException
	{
		return WComplexPanel.makeCustom (id, contentId, view, ps,
												new WHorizontalPanel(),
												db, creator).setStyle(id, db);
	}
}
