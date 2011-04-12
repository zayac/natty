package ru.natty.web.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

public class ILabel extends IWidget
{
	public ILabel (Integer id, String l){super(id, new HTML (l));}

	public void setText(String nStr) {
		((Label)getWidget()).setText(nStr);
	}
}
