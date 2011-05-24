package ru.natty.web.client;

import com.allen_sauer.gwt.log.client.Log;
import ru.natty.web.client.iwidget.ILabel;
import ru.natty.web.client.iwidget.IWidget;
import ru.natty.web.client.iwidget.ComplexPanelI;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import ru.natty.web.client.iwidget.IVerticalPanel;

import ru.natty.web.shared.diffpatchers.DiffPatcher;
import ru.natty.web.shared.Parameters;

public class ElementReceiver
{
	private final MainServiceAsync greetingService = GWT.create(MainService.class);
	private IWidget root;
	private Panel diff;
	private ParamsBuilder pb = ParamsBuilder.get();
	private boolean inProcess;
	private boolean receiveRequested;

	private static ElementReceiver instance = null;
	private ElementReceiver() {}

	public static ElementReceiver get()
	{
		if (null == instance) instance = new ElementReceiver();
		return instance;
	}
	
	public void init (ComplexPanel base)
	{
		inProcess = false;
		receiveRequested = false;
		ParamsUrl.getInstance().getFromHistory(pb.getCurrent());
		IVerticalPanel container = new IVerticalPanel (0);
		base.add (container);
		base.add (new HTML ("<h2> Received from server: </h2>"));
		diff = new VerticalPanel();
		diff.setStylePrimaryName ("received-panel");
		base.add (diff);
		queryInit (container);
	}
	
	private boolean onBegin()
	{
		if (inProcess)
		{
			receiveRequested = true;
			return false;
		}
		inProcess = true;
		receiveRequested = false;
		return true;
	}
	
	private void onEnd()
	{
		inProcess = false;
		if (receiveRequested)
			queryPage();
	}
	
	private void requestSucceeded (DiffPatcher result, Integer id)
	{
		diff.clear();
		if (null != result)
			diff.add (new HTML (result.toString()));
		else
			diff.add (new HTML ("diff is null"));
		
		Log.debug("id " + id + " succeeded"); 
		pb.requestSucceeded();
		onEnd();
	}
	
	private void requestFailed (Throwable caught, String message)
	{
		Log.error ("query (" + message + ") filed with:" + caught.getMessage());
		onEnd();
	}
	
	public void queryPage()
	{
		Log.trace ("querying page");
		if (onBegin()) return;
		final Parameters param = pb.getCurrent();
		greetingService.getDifference (param, pb.getPrev(),
									   new AsyncCallback<DiffPatcher>() {
			
			@Override
			public void onSuccess(DiffPatcher result) {
				if (null == result)
				{
					requestSucceeded (result, param.getId());
					return;
				}
				result.applay(root);

				requestSucceeded (result, param.getId());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				requestFailed (caught, param.getVal("query") + ", " + param.getId());
			}
		});
		Log.trace ("query for " + param.getVal("query") + "sent");
	}
	
	private void queryInit (final ComplexPanelI base)
	{
		Log.trace ("querying");
		onBegin();
		greetingService.getInitialContent (pb.getCurrent(),
			new AsyncCallback<DiffPatcher>() {
			
			@Override
			public void onSuccess(DiffPatcher result) {
				root = result.createNew(0);
				base.add (root);

				requestSucceeded (result, pb.getCurrent().getId());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				requestFailed (caught, "initial request " + pb.getCurrent().getId());
			}
		});
		Log.trace ("init for " + pb.getCurrent().getId() + " sent");
	}

}
