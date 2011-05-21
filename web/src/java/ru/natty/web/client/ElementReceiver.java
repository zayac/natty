package ru.natty.web.client;

import ru.natty.web.client.iwidget.ILabel;
import ru.natty.web.client.iwidget.IWidget;
import ru.natty.web.client.iwidget.ComplexPanelI;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import ru.natty.web.shared.diffpatchers.DiffPatcher;
import ru.natty.web.shared.Parameters;

public class ElementReceiver
{
	private final MainServiceAsync greetingService = GWT.create(MainService.class);
	private IWidget root;
	private Panel diff;
	private Panel logp;
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
		queryInit (base);
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
	
	private void requestSucceeded()
	{
		pb.requestSucceeded();
		onEnd();
	}
	
	private void requestFailed (String message)
	{
		logp.add(new HTML (message));
		onEnd();
	}
	
	public void queryPage()
	{
		if (onBegin()) return;
		final Parameters param = pb.getCurrent();
		greetingService.getDifference (param, pb.getPrev(),
									   new AsyncCallback<DiffPatcher>() {
			
			@Override
			public void onSuccess(DiffPatcher result) {
				if (null == result)
				{
					//((ComplexPanelI)root).add(new ILabel(324, "diff is null!"));
					requestSucceeded();
					return;
				}
				result.applay(root);
				diff.clear();
				diff.add(new HTML(result.toString()));
                //((ComplexPanelI)root).add(new ILabel(324, "id " + param.getId() + " succeded"));

				requestSucceeded();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				requestFailed ("query " + param.getVal("query") + " failed while being requested<br>" +
								"cause: " + caught.getMessage());
			}
		});
		//((ComplexPanelI)root).add(new ILabel(324, "get difference completed"));
	}
	
	private void queryInit (final ComplexPanel base)
	{
		onBegin();
		greetingService.getInitialContent (pb.getCurrent(),
			new AsyncCallback<DiffPatcher>() {
			
			@Override
			public void onSuccess(DiffPatcher result) {
				root = result.createNew(0);
				base.add (root);
				logp = new VerticalPanel();
				logp.setStylePrimaryName("log-panel");
				base.add(logp);
				base.add (new HTML ("<h2> Received from server: </h2>"));
				diff = new VerticalPanel();
				diff.setStylePrimaryName("received-panel");
				base.add (diff);
				diff.clear();
				diff.add (new HTML (result.toString()));

				requestSucceeded();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				requestFailed ("<h1> Sorry, but there an error occured int page loading," +
								   "with following response:" + caught.getMessage() + "</h1>");
			}
		});
	}

}
