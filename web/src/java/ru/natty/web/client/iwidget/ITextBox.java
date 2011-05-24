/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client.iwidget;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;
import ru.natty.web.client.ElementReceiver;
import ru.natty.web.client.ParamsBuilder;

/**
 *
 * @author necto
 */
public class ITextBox extends IWidget
{
	class OnKeyUp implements KeyUpHandler
	{
		private String targetName;

		public OnKeyUp (String target)
		{
			targetName = target;
		}
		
		public void setTarget (String ntarget)
		{
			targetName = ntarget;
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			ParamsBuilder.getCurrent().setVal(targetName, ((TextBox) getWidget()).getText());
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				ElementReceiver.get().queryPage();
		}
		
		public int hashCode()
		{
			return targetName.hashCode();
		}
	}
	
	OnKeyUp myHandler;
	
	public ITextBox (Integer id, final String name, String text)
	{
		super( id, new TextBox());
		setText(text);
		myHandler = new OnKeyUp(name);

		((TextBox)getWidget()).addKeyUpHandler(myHandler);
	}

	final private void setText(String nStr) {
		((TextBox)getWidget()).setText (nStr);
	}
	
	private void setName (String name)
	{
		myHandler.setTarget(name);
	}

	@Override
	public int hashCode() {
		return myHandler.hashCode();
	}
}
