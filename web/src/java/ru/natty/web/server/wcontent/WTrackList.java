/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import ru.natty.web.server.DataBase;
import ru.natty.web.server.PersistToIText;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class WTrackList
{
	public static WContent make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
		return WTextCellList.make (id, ps, db, creator, "Track",
								   db.queryTrackByPattern (ps.getVal("query")),
								   new PersistToIText());
	}
}
