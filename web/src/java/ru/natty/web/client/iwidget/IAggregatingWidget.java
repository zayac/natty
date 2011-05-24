/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.natty.web.client.iwidget;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import java.util.Iterator;

/**
 *
 * @author necto
 */
public abstract class IAggregatingWidget extends IWidget implements Iterable<IWidget>
{
	public IAggregatingWidget (Integer id, Widget w)
	{
		super (id, w);
	}
	
	@Override
	public Iterator<IWidget> iterator() {
		return new IWidget.IteratorAdapter<IWidget, Widget> (((HasWidgets)getWidget()).iterator(),
				new WidgetConverter<IWidget, Widget>() {

					@Override
					public IWidget convert(Widget w) {
						return (IWidget)w;
					}
				});
	}

	@Override
	public int hashCode() {
		int hash = 0;
		Iterator<IWidget> iter = iterator();
		while (iter.hasNext())
			hash ^= iter.next().hashCode();
		return hash;
	}
	
}
