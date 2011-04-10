/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import java.util.List;
import ru.natty.persist.Track;
import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class WTracksList extends WContent
{
	private WVerticalPanelContent data;

	public WTracksList (List<Track> tracks)
	{
		data = new WVerticalPanelContent();

		int i = 0;
		for (Track t : tracks)
			data.addItem(100500 + i/*TODO: !!! completely wrong*/, i++, new WLabelContent(t.getName()));
	}

	@Override
	public DiffPatcher getDifference(WContent prev, boolean amputation)
	{
		//TODO: assert prev to be instance of WTracksList
		return data.getDifference (((WTracksList)prev).data, amputation);
	}

	@Override
	public DiffPatcher getAllContent() {
		return data.getAllContent();
	}

	@Override
	public WContent copy() {
		return data.copy();
	}

	@Override
	public boolean isAggregating() {
		return false;//!!! TODO: or true?
	}

	public static WTracksList make (Integer id, Parameters ps, DataBase db)
	{
        return new WTracksList (db.queryTrackByPattern(ps.getQuery()));
	}
}
