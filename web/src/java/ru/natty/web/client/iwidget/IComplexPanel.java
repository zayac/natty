/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.natty.web.client.iwidget;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Iterator;

/**
 *
 * @author necto
 */
public abstract class IComplexPanel extends IAggregatingWidget implements ComplexPanelI
{
	public IComplexPanel (Integer id, Widget w) {
		super(id, w);
	}
	@Override
	public int getWidgetCount() {
		return ((ComplexPanel)getWidget()).getWidgetCount();
	}

	@Override
	public abstract void insert(IWidget w, int beforeIndex);

	@Override
	public void add(IWidget w) {
		((ComplexPanel)getWidget()).add(w);
	}
//	@Override
//	public Iterator<IWidget> iterator() {
//		return new IWidget.IteratorAdapter<IWidget, Widget> (((ComplexPanel)getWidget()).iterator(),
//				new WidgetConverter<IWidget, Widget>() {
//
//					@Override
//					public IWidget convert(Widget w) {
//						return (IWidget)w;
//					}
//				});
//	}
//
//	@Override
//	public int hashCode() {
//		int hash = 0;
//		Iterator<IWidget> iter = iterator();
//		while (iter.hasNext())
//			hash ^= iter.next().hashCode();
//		return hash;
//	}
}
