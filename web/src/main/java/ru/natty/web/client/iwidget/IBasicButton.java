/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client.iwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import ru.natty.web.client.ElementReceiver;

/**
 *
 * @author necto
 */
public class IBasicButton extends IWidget
{
	public IBasicButton (Integer id, String label)
	{
		super (id, new Button(label));

		((Button)getWidget()).addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ElementReceiver.get().queryPage();
//				((Button)getWidget()).setHTML ("clicked");
			}
		});
	}

	public void setText (String nStr)
	{
		((Button)getWidget()).setHTML (nStr);
	}

	@Override
	public int hashCode() {
		return ((Button)getWidget()).getHTML().hashCode();
	}
}
