/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import java.util.List;
import ru.natty.persist.Track;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class WTrackList extends WContent
{
	private WVerticalPanel data;

	public WTrackList (List<Track> tracks)
	{
		data = new WVerticalPanel();

		int i = 0;
		for (Track t : tracks)
			data.addItem(100500 + i/*TODO: !!! completely wrong*/, i++, new WLabel(t.getName()));
	}

	@Override
	public DiffPatcher getDifferenceInt(WContent prev, boolean amputation)
	{
		//TODO: assert prev to be instance of WTracksList
		return data.getDifference (((WTrackList)prev).data, amputation);
	}

	@Override
	public DiffPatcher getAllContentInt() {
		return data.getAllContent();
	}

	@Override
	public WContent copyInt() {
		return data.copy();
	}

	@Override
	public boolean isAggregating() {
		return false;//!!! TODO: or true?
	}

	public static WTrackList make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
        return new WTrackList (db.queryTrackByPattern(ps.getQuery()));
	}
}
