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

import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.Parameters;

public class ElementReceiver
{
	private final MainServiceAsync greetingService = GWT.create(MainService.class);
	private IWidget root;
	private Panel diff;
	private ParamsBuilder pb = ParamsBuilder.get();

	private static ElementReceiver instance = null;
	private ElementReceiver() {}

	public static ElementReceiver get()
	{
		if (null == instance) instance = new ElementReceiver();
		return instance;
	}
	
	public void init (ComplexPanel base)
	{
		ParamsUrl.getInstance().getFromHistory(pb.getCurrent());
		queryInit (base);
	}
	
	public void queryElement()
	{
		final Parameters param = pb.getCurrent();
		greetingService.getDifference (param, pb.getPrev(),
									   new AsyncCallback<DiffPatcher>() {
			
			@Override
			public void onSuccess(DiffPatcher result) {
				if (null == result)
				{
					((ComplexPanelI)root).add(new ILabel(324, "diff is null!"));
					pb.requestSucceeded();
					return;
				}
				result.applay(root);
				diff.clear();
				diff.add(new HTML(result.toString()));
                ((ComplexPanelI)root).add(new ILabel(324, "id " + param.getElementId() + " succeded"));

				pb.requestSucceeded();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				((ComplexPanelI)root).add(new ILabel(324, "query " + param.getQuery() + " failed while being requested"));
				((ComplexPanelI)root).add(new ILabel(325, "cause: " + caught.getMessage()));
			}
		});
		//((ComplexPanelI)root).add(new ILabel(324, "get difference completed"));
	}
	
	private void queryInit (final ComplexPanel base)
	{
		greetingService.getInitialContent (pb.getCurrent(),
			new AsyncCallback<DiffPatcher>() {
			
			@Override
			public void onSuccess(DiffPatcher result) {
				root = result.createNew(0);
				base.add (root);
				base.add (new HTML ("<h2> Received from server: </h2>"));
				diff = new VerticalPanel();
				base.add (diff);
				diff.clear();
				diff.add (new HTML (result.toString()));

				pb.requestSucceeded();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				base.add(new HTML ("<h1> Sorry, but there an error occured int page loading," +
								   "with following response:" + caught.getMessage()));
			}
		});
	}

}
