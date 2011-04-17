package ru.natty.web.client.iwidget;

import java.util.Iterator;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import ru.natty.web.client.ElementReceiver;
import ru.natty.web.client.ParamsBuilder;

public class ITabPanel extends IWidget implements Iterable<IWidget>
{
	protected class OnChange implements SelectionHandler<Integer>
	{
		@Override
		public void onSelection(final SelectionEvent<Integer> event)
		{
			if (!queryAllowed) return;

			final IStreak sp = (IStreak)ITabPanel.this.tp.getWidget (event.getSelectedItem());
			
			ParamsBuilder.getCurrent().setElementId(sp.getId());
			ElementReceiver.get().queryElement();
		}
	}
	
	TabPanel tp;
	private boolean queryAllowed = true;
	
	public ITabPanel (Integer id)// TODO: disable call OnChange when it sets default 
	{
		super(id, new TabPanel());
		tp = (TabPanel)getWidget();
		tp.addSelectionHandler(new OnChange());
		queryAllowed = true;
	}
	
	public void setActiveTab (Integer index)
	{
		queryAllowed = false;
		tp.selectTab(index);
		queryAllowed = true;
	}
	
	@Override
	public Iterator<IWidget> iterator() {
		return new IWidget.IteratorAdapter<IWidget, Widget> (((TabPanel)getWidget()).iterator(),
				new WidgetConverter<IWidget, Widget>() {

					@Override
					public IWidget convert(Widget w) {
						return (IWidget)w;
					}
				});
	}

	public int getWidgetCount() {
		return ((TabPanel)getWidget()).getWidgetCount();
	}

	public void add(IWidget w, String tabText) {
		((TabPanel)getWidget()).add(new IStreak(w), tabText);		
	}

	public void insert(IWidget w, String tabText, Integer beforeIndex) {
		((TabPanel)getWidget()).insert(new IStreak(w), tabText, beforeIndex);
	}
}
