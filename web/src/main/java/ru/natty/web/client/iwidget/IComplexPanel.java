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
	public final void insert(IWidget w, int beforeIndex)
	{
		insertInt(new IStreak(w), beforeIndex);
	}
	
	protected abstract void insertInt(IWidget w, int beforeIndex);

	@Override
	public void add(IWidget w) {
		((ComplexPanel)getWidget()).add (new IStreak(w));
	}
}
