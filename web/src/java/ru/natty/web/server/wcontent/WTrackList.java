/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import ru.natty.persist.Track;
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
		DataBase.Query<Track> q = null;
		if (ps.hasParam ("Album"))
			q = db.queryTrackByAlbum (ps.getIntVal ("Album"));
		else if (ps.hasParam("Artist"))
			q = db.queryTrackByArtistAndPattern (ps.getIntVal ("Artist"),
										DataBase.transformWordsToPattern (ps.getVal("query")));
		else if (ps.hasParam("Genre"))
			q = db.queryTrackByGenreAndPattern (ps.getIntVal ("Genre"),
										DataBase.transformWordsToPattern(ps.getVal("query")));
		else
			q = db.queryTrackByPattern (DataBase.transformWordsToPattern(ps.getVal("query")));
		return WTextCellList.make (id, ps, db, creator, "Track", q, new PersistToIText());
	}
}
