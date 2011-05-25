package ru.natty.web.client.iwidget;

import com.google.gwt.user.client.ui.HorizontalPanel;

public class IHorizontalPanel extends IComplexPanel
{
	public IHorizontalPanel (Integer id) {
		super(id, new HorizontalPanel());
	}
	
	@Override
	public void insertInt (IWidget w, int beforeIndex) {
		((HorizontalPanel)getWidget()).insert(w, beforeIndex);
	}
}
