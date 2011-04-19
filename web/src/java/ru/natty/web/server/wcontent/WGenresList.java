/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import java.util.List;
import ru.natty.persist.Genre;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.IText;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class WGenresList
{
	public static WContent make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
		WTextCellList data = new WTextCellList("Genre");
		List<Genre> genres = db.queryGenreByPattern(ps.getVal("query"));

		for (Genre g : genres)
			data.addText(new IText(g.getId(), g.getName()));

        return data.setStyle(id, db);
	}
}
