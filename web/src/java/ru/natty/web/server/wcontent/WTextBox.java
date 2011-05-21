/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server.wcontent;

import ru.natty.web.shared.diffpatchers.DiffPatcher;
import ru.natty.web.shared.diffpatchers.TextBoxDP;
import ru.natty.web.persist.Label;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class WTextBox extends WContent
{
	private String name;
	private String text;

	public WTextBox (String name, String text)
	{
		this.text = text;
		this.name = name;
	}
	
	public String getText()
	{
		return text;
	}
	
	public String getName()
	{
		return name;
	}

	@Override
	public DiffPatcher getDifferenceInt(WContent prev, boolean amputation)
	{
		assert prev instanceof WTextBox;
		if (text.equals(((WTextBox)prev).getText()) &&
			name.equals(((WTextBox)prev).getName())) return null;
		return new TextBoxDP (name, text);
	}

	@Override
	public DiffPatcher getAllContentInt()
	{
		return new TextBoxDP (name, text);
	}

	@Override
	public WContent copyInt()
	{
		return new WTextBox (name, text);
	}

	@Override
	public boolean isAggregating() {
		return false;
	}

	@Override
	public String toString() {
		return "WTextBoxContent\"" + text + "\"";
	}

	public static WContent make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
		String mname = "query";
        //Label l = db.queryLabelById(id);
        return new WTextBox (mname, ps.getVal(mname)).setStyle(id, db);
	}
}
