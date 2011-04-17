package ru.natty.web.client.iwidget;

import com.google.gwt.user.client.ui.Label;

public class IVoid extends IWidget {

	public IVoid(Integer id) {
		super(id, new Label("VOID"));
	}

	public boolean isVoid()
	{
		return true;
	}
}
