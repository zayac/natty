/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;
import java.util.List;
import ru.natty.persist.Artist;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.PersistToIText;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.IText;
import ru.natty.web.shared.Parameters;
/**
 *
 * @author necto
 */
public class WArtistList
{
	public static WContent make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
		DataBase.Query<Artist> q = null;
		if (ps.hasParam ("Genre"))
			q = db.queryArtistByGenreAndPattern (ps.getIntVal ("Genre"),
												DataBase.transformWordsToPattern(ps.getVal("query")));
		else
			q = db.queryArtistByPattern
								   (DataBase.transformWordsToPattern(ps.getVal("query")));

		return WTextCellList.make (id, ps, db, creator, "Artist", q,
								   new PersistToIText());
	}
}
