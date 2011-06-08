/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.ServerException;
import ru.natty.web.shared.diffpatchers.FlowPanelDiffPatcher;

/**
 *
 * @author necto
 */
public class WFlowPanel extends WComplexPanel
{
	public WFlowPanel()
	{
		super (FlowPanelDiffPatcher.class);
	}

	public static WContent make (Integer id, Parameters ps,
											 DataBase db, WContentCreator creator) throws ServerException
	{
		return WComplexPanel.make (id, ps, new WFlowPanel(),
										   db, creator).setStyle(id, db);
	}

	public static WContent
			makeCustom (Integer id, Integer contentId,
						WContent view, Parameters ps,
						DataBase db, WContentCreator creator) throws ServerException
	{
		return WComplexPanel.makeCustom (id, contentId, view, ps,
												new WFlowPanel(),
												db, creator).setStyle(id, db);
	}
}
