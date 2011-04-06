/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import java.util.List;
import ru.natty.persist.Genre;
import ru.natty.web.shared.DiffPatcher;

/**
 *
 * @author necto
 */
public class WGenresList extends WContent
{
	private WVerticalPanelContent data;

	public WGenresList (List<Genre> genres)
	{
		data = new WVerticalPanelContent();
		
		int i = 0;
		for (Genre g : genres)
			data.addItem(100500 + i/*TODO: !!! completely wrong*/, i++, new WLabelContent(g.getName()));
//		data.addItem(100500, 0, new WLabelContent("Hi it's a genres list"));
	}

	@Override
	public DiffPatcher getDifference(WContent prev, boolean amputation)
	{
		//TODO: assert prev instance of WGenresList
		return data.getDifference (((WGenresList)prev).data, amputation);
	}

	@Override
	public DiffPatcher getAllContent() {
		return data.getAllContent();
	}

	@Override
	public WContent copy() {
		return data.copy();
	}

}
