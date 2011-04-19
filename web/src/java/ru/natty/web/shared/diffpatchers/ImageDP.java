/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.shared.diffpatchers;

import ru.natty.web.client.iwidget.IImage;
import ru.natty.web.client.iwidget.IWidget;

/**
 *
 * @author necto
 */
public class ImageDP extends DiffPatcher
{
	private String url;

	public ImageDP(){}
	public ImageDP (String url)
	{
		this.url = url;
	}

	@Override
	public boolean isVoid()
	{
		return false;
	}

	@Override
	protected IWidget createNewInt (int id)
	{
		return new IImage(id, url);
	}

	@Override
	protected void applayInt(IWidget w)
	{
		((IImage)w).setUrl(url);
	}

	@Override
	public String toString()
	{
		return "Image[<font color=#003510>" + url + "</font>]";
	}

}
