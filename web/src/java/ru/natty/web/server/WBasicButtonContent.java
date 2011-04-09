/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.server;

import ru.natty.web.shared.BasicButtonDP;
import ru.natty.web.shared.DiffPatcher;

/**
 *
 * @author necto
 */
public class WBasicButtonContent extends WContent
{
	private String text;

	public WBasicButtonContent (String text)
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
		assert prev instanceof WTextBoxContent;
		if (text.equals(((WBasicButtonContent)prev).getText())) return null;
		return new BasicButtonDP(text);
	}

	@Override
	public DiffPatcher getAllContent()
	{
		return new BasicButtonDP (text);
	}

	@Override
	public WContent copy()
	{
		return new WTextBoxContent (text);
	}

	@Override
	public String toString() {
		return "WBasicButtonContent\"" + text + "\"";
	}
}
