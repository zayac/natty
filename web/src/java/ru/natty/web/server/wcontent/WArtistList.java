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
//		WTextCellList data = new WTextCellList("Artist");
////		List<Artist> artists = db.queryArtistByPattern ("%" + ps.getVal("query") + "%");
//		List<Artist> artists = db.queryArtistByPattern (ps.getVal("query")).getResults();
//
//		for (Artist a : artists)
//			data.addText(new IText(a.getId(), a.getName()));
//
//        return data.setStyle(id, db);
		return WTextCellList.make (id, ps, db, creator, "Artist",
								   db.queryArtistByPattern (ps.getVal("query")),
								   new PersistToIText());
	}
}
