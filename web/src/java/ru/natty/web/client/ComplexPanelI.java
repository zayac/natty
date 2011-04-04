package ru.natty.web.client;

import com.google.gwt.user.client.ui.Widget;

public interface ComplexPanelI {
	public int getWidgetCount();
	public void insert (IWidget w, int beforeIndex);
	public void add (IWidget w);
}
