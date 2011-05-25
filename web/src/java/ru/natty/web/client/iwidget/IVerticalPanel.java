package ru.natty.web.client.iwidget;

import com.google.gwt.user.client.ui.VerticalPanel;

public class IVerticalPanel extends IComplexPanel
{
	public IVerticalPanel (Integer id)
	{
		super (id, new VerticalPanel());
	}
	
	@Override
	public void insertInt (IWidget w, int beforeIndex) {
		((VerticalPanel)getWidget()).insert(w, beforeIndex);
	}
}
