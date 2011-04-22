/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import java.util.List;
import ru.natty.persist.Track;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.IText;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class WTrackList
{
	public static WContent make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
		WTextCellList data = new WTextCellList("Track");
		List<Track> tracks = db.queryTrackByPatternWindowed("%" + ps.getVal("query") + "%", 10, 0);

		for (Track t : tracks)
			data.addText(new IText(t.getId(), t.getName()));

        return data.setStyle(id, db);
	}
}
