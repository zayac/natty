/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client.iwidget;

import com.google.gwt.user.client.ui.Image;

/**
 *
 * @author necto
 */
public class IImage extends IWidget
{
	public IImage (Integer id, String url)
	{
		super(id, new Image(url));
	}

	public void setUrl (String nUrl)
	{
		((Image)getWidget()).setUrl(nUrl);
	}

	@Override
	public int hashCode() {
		return ((Image)getWidget()).getUrl().hashCode();
	}
}
