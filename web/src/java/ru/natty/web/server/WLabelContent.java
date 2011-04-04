package ru.natty.web.server;

import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.StringDiff;

public class WLabelContent extends WContent
{

	public String text;
	
	public WLabelContent (String label)
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
	public DiffPatcher getDifference(WContent prev, boolean amputation)
	{
		if (text.equals(((WLabelContent)prev).getText())) return null;
		return new StringDiff (text);
	}

	@Override
	public DiffPatcher getAllContent()
	{
		return new StringDiff (text);
	}

	@Override
	public WContent copy()
	{
		return new WLabelContent(text);
	}

	@Override
	public String toString() {
		return "WLabelContent [text=" + text + "]";
	}
}
