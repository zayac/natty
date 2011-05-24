/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import ru.natty.web.persist.Label;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.Parameters;
import ru.natty.web.shared.diffpatchers.DiffPatcher;
import ru.natty.web.shared.diffpatchers.ImageDP;

/**
 *
 * @author necto
 */
public class WImage extends WContent
{
	private String url;

	public WImage (String url)
	{
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	protected DiffPatcher getDifferenceInt(WContent prev, boolean amputation)
	{
		if (url.equals(((WImage)prev).getUrl())) return null;
		return new ImageDP (url);
	}

	@Override
	protected DiffPatcher getAllContentInt()
	{
		return new ImageDP (url);
	}

	@Override
	protected WContent copyInt()
	{
		return new WImage(url);
	}

	@Override
	public boolean isAggregating()
	{
		return false;
	}
	
	@Override
	public int hashCode() {
		return url.hashCode();
	}

	public static WContent make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
        Label l = db.queryLabelById(id);
        return new WImage(l.getText()).setStyle(id, db);
	}
}
