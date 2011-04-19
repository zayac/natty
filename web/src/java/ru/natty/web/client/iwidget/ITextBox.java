/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client.iwidget;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;
import ru.natty.web.client.ParamsBuilder;

/**
 *
 * @author necto
 */
public class ITextBox extends IWidget
{
	public ITextBox (Integer id, String text)
	{
		super( id, new TextBox());
		setText(text);

		((TextBox)getWidget()).addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				ParamsBuilder.getCurrent().setVal("query", ((TextBox)getWidget()).getText());
			}
		});
	}

	final public void setText(String nStr) {
		((TextBox)getWidget()).setText (nStr);
	}
}
