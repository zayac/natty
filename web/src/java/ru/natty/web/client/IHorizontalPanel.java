package ru.natty.web.client;

import java.util.Iterator;

import ru.natty.web.client.IWidget.WidgetConverter;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class IHorizontalPanel extends IWidget implements ComplexPanelI, Iterable<IWidget>
{
	public IHorizontalPanel (Integer id) {
		super(id, new HorizontalPanel());
	}

	@Override
	public int getWidgetCount() {
		return ((HorizontalPanel)getWidget()).getWidgetCount();
	}

	@Override
	public void insert(IWidget w, int beforeIndex) {
		((HorizontalPanel)getWidget()).insert(w, beforeIndex);
	}

	@Override
	public void add(IWidget w) {
		((HorizontalPanel)getWidget()).add(w);
	}
	@Override
	public Iterator<IWidget> iterator() {
		return new IWidget.IteratorAdapter<IWidget, Widget> (((HorizontalPanel)getWidget()).iterator(),
				new WidgetConverter<IWidget, Widget>() {

					@Override
					public IWidget convert(Widget w) {
						return (IWidget)w;
					}
				});
	}
}
