/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;
import java.util.List;
import ru.natty.persist.Album;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.IText;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class WAlbumList
{
	public static WContent make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
		WTextCellList data = new WTextCellList("Album");
		List<Album> albums = db.queryAlbumByPattern (ps.getVal("query"));

		for (Album a : albums)
			data.addText(new IText(a.getId(), a.getName()));

        return data.setStyle(id, db);
	}

}
