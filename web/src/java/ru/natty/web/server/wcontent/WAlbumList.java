/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;
import java.util.List;
import ru.natty.persist.Album;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.PersistToIText;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class WAlbumList
{
	public static WContent make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
		DataBase.Query<Album> q = null;
		if (ps.hasParam ("Artist"))
			q = db.queryAlbumByArtist (ps.getIntVal ("Artist"));
		else if (ps.hasParam ("Genre"))
			q = db.queryAlbumByGenreAndPattern (ps.getIntVal ("Genre"),
					DataBase.transformWordsToPattern(ps.getVal("query")));
		else
			q = db.queryAlbumByPattern (DataBase.transformWordsToPattern(ps.getVal("query")));
		

		return WTextCellList.make (id, ps, db, creator, "Album", q,
								   new PersistToIText());
	}

}
