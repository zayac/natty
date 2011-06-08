package ru.natty.web.server.wcontent;

import ru.natty.web.shared.diffpatchers.DiffPatcher;
import ru.natty.web.shared.diffpatchers.StringDiff;
import ru.natty.web.persist.Label;
import ru.natty.web.server.DataBase;
import ru.natty.web.server.WContentCreator;
import ru.natty.web.shared.Parameters;

public class WLabel extends WContent
{

	public String text;
	
	public WLabel (String label)
	{
		text = label;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public String getText()
	{
		return text;
	}

	@Override
	public DiffPatcher getDifferenceInt(WContent prev, boolean amputation)
	{
		if (null == text) return new StringDiff("my text is null! It is an error!");
		if (text.equals(((WLabel)prev).getText())) return null;
		return new StringDiff (text);
	}

	@Override
	public DiffPatcher getAllContentInt()
	{
		return new StringDiff (text);
	}

	@Override
	public WContent copyInt()
	{
		return new WLabel(text);
	}

	@Override
	public boolean isAggregating() {
		return false;
	}

	@Override
	public String toString() {
		return "WLabelContent [text=" + text + "]";
	}
	
	@Override
	public int hashCode() {
		return text.hashCode();
	}

	public static WContent make (Integer id, Parameters ps, DataBase db, WContentCreator creator)
	{
        Label l = db.queryLabelById(id);
        return new WLabel(l.getText()).setStyle(id, db);
	}
}
