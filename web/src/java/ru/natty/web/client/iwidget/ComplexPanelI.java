package ru.natty.web.client.iwidget;

import com.google.gwt.user.client.ui.Widget;

public interface ComplexPanelI extends Iterable<IWidget>
{
	public int getWidgetCount();
	public void insert (IWidget w, int beforeIndex);
	public void add (IWidget w);
}
