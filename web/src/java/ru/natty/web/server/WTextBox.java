/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.TextBoxDP;
import ru.natty.web.persist.Label;
import ru.natty.web.shared.Parameters;

/**
 *
 * @author necto
 */
public class WTextBox extends WContent
{
	private String text;

	public WTextBox (String text)
	{
		this.text = text;
	}
	
	public String getText()
	{
		return text;
	}

	@Override
	public DiffPatcher getDifference(WContent prev, boolean amputation)
	{
		assert prev instanceof WTextBox;
		if (text.equals(((WTextBox)prev).getText())) return null;
		return new TextBoxDP(text);
	}

	@Override
	public DiffPatcher getAllContent()
	{
		return new TextBoxDP (text);
	}

	@Override
	public WContent copy()
	{
		return new WTextBox (text);
	}

	@Override
	public boolean isAggregating() {
		return false;
	}

	@Override
	public String toString() {
		return "WTextBoxContent\"" + text + "\"";
	}

	public static WTextBox make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
        Label l = db.queryLabelById(id);
        return new WTextBox(l.getText());
	}
}
