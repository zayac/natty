package ru.natty.web.client;

import java.util.Iterator;

import ru.natty.web.shared.DiffPatcher;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class ITabPanel extends IWidget implements Iterable<IWidget>
{
	protected class OnChange implements SelectionHandler<Integer>
	{
		@Override
		public void onSelection(final SelectionEvent<Integer> event)
		{
			if (!queryAllowed) return;

			final IStreak sp = (IStreak)ITabPanel.this.tp.getWidget (event.getSelectedItem());
			final Integer id = sp.getId();
			ElementReceiver.queryElement(id, MainEntryPoint.curPar);
			/*
			
			greetingService.getDifference(id, TestGWT.curPar, new AsyncCallback<DiffPatcher>() {
				
				@Override
				public void onSuccess(DiffPatcher result) {
					sp.alterContent(result.createNew(id));
				}
				
				@Override
				public void onFailure(Throwable caught) {
					sp.alterContent( new ILabel(id, caught.getMessage()));
				}
			});*/
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