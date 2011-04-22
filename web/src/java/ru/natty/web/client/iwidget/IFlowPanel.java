/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client.iwidget;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Iterator;

/**
 *
 * @author necto
 */
public class IFlowPanel extends IWidget implements ComplexPanelI, Iterable<IWidget>
{
	public IFlowPanel (Integer id)
	{
		super (id, new FlowPanel());
	}
	@Override
	public int getWidgetCount()
	{
		return ((FlowPanel)getWidget()).getWidgetCount();
	}

	@Override
	public void insert(IWidget w, int beforeIndex) {
		((FlowPanel)getWidget()).insert(w, beforeIndex);
	}

	@Override
	public void add(IWidget w) {
		((FlowPanel)getWidget()).add(w);
	}
	@Override
	public Iterator<IWidget> iterator() {
		return new IWidget.IteratorAdapter<IWidget, Widget> (((FlowPanel)getWidget()).iterator(),
				new WidgetConverter<IWidget, Widget>() {

					@Override
					public IWidget convert(Widget w) {
						return (IWidget)w;
					}
				});
	}
}
