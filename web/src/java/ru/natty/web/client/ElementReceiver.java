package ru.natty.web.client;

import com.allen_sauer.gwt.log.client.Log;
import ru.natty.web.client.iwidget.IWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import ru.natty.web.client.iwidget.IStreak;
import ru.natty.web.client.iwidget.IVoid;

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
		IStreak container = new IStreak (new IVoid(0));
		base.add (container);
		base.add (new HTML ("<h2> Received from server: </h2>"));
		diff = new VerticalPanel();
		diff.setStylePrimaryName ("received-panel");
		base.add (diff);
		queryInit (container);
	}
	
	private boolean onBegin (boolean update_when_possible)
	{
		if (inProcess)
		{
			receiveRequested = update_when_possible;
			return false;
		}
		pb.holdCurrent();
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
		final Parameters param = pb.getCurrent();
		param.setId(0);
		Log.debug ("querying page " + param.toString());
		if (!onBegin (true)) return;
		greetingService.getDifference (param, pb.getPrev(),
									   new AsyncCallback<DiffPatcher>() {
			
			@Override
			public void onSuccess (DiffPatcher result) {
				if (null != result)
					result.applay (root);
				
				requestSucceeded (result, param.getId());
				
//				if (null != result) root.ensureCorrectness (result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				requestFailed (caught, param.getVal ("query") + ", " + param.getId());
			}
		});
		Log.debug ("query for " + param.getVal("query") + " sent");
	}
	
	public void queryInit (final IStreak base)
	{
		Log.debug ("querying id:" + base.getId());
		final Parameters param = pb.getCurrent();
		param.setId (base.getId());
		
		if (!onBegin (true)) return;
		greetingService.getInitialContent (param,
			new AsyncCallback<DiffPatcher>() {
			
			@Override
			public void onSuccess (DiffPatcher result) {
				//root = ;
				base.alterContent (result.createNew (param.getId()));
				if (param.getId().equals(0)) 
					root = base.getContent();

				requestSucceeded (result, pb.getCurrent().getId());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				requestFailed (caught, "initial request " + pb.getCurrent().getId());
			}
		});
		Log.debug ("init for " + pb.getCurrent().getId() + " sent");
	}

}
