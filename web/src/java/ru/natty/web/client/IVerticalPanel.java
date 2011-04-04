package ru.natty.web.client;

import java.util.Iterator;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class IVerticalPanel extends IWidget implements ComplexPanelI, Iterable<IWidget>
{
	public IVerticalPanel (Integer id)
	{
		super (id, new VerticalPanel());
	}
	@Override
	public int getWidgetCount() {
		return ((VerticalPanel)getWidget()).getWidgetCount();
	}

	@Override
	public void insert(IWidget w, int beforeIndex) {
		((VerticalPanel)getWidget()).insert(w, beforeIndex);
	}

	@Override
	public void add(IWidget w) {
		((VerticalPanel)getWidget()).add(w);
	}
	@Override
	public Iterator<IWidget> iterator() {
		return new IWidget.IteratorAdapter<IWidget, Widget> (((VerticalPanel)getWidget()).iterator(),
				new WidgetConverter<IWidget, Widget>() {

					@Override
					public IWidget convert(Widget w) {
						return (IWidget)w;
					}
				});
	}
}
