package ru.natty.web.client.iwidget;

public interface ComplexPanelI extends Iterable<IWidget>
{
	public int getWidgetCount();
	public void insert (IWidget w, int beforeIndex);
	public void add (IWidget w);
}
