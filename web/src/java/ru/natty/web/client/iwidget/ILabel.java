package ru.natty.web.client.iwidget;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

public class ILabel extends IWidget
{
	public ILabel (Integer id, String l){super(id, new HTML (l));}

	public void setText(String nStr) {
		((Label)getWidget()).setText(nStr);
	}

	@Override
	public int hashCode() {
		return ((Label)getWidget()).getText().hashCode();
	}
}
