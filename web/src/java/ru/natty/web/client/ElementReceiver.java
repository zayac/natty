package ru.natty.web.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.Parameters;

public class ElementReceiver
{
	private static final MainServiceAsync greetingService = GWT.create(MainService.class);
	private static IWidget root;
	private static Panel diff;
	private static Integer lastLeaf;
	private static Parameters lastParam;
	
	static public void init (Integer id, Parameters param, ComplexPanel base)
	{
		queryInit (id, param, base);
		lastLeaf = id;
		lastParam = param;
	}
	
	static public void queryElement (final Integer id, Parameters param)
	{
		greetingService.getDifference(id, param, lastLeaf, lastParam,
				new AsyncCallback<DiffPatcher>() {
			
			@Override
			public void onSuccess(DiffPatcher result) {
				result.applay(root);
				diff.clear();
				diff.add(new HTML(result.toString()));
                ((ComplexPanelI)root).add(new ILabel(324, "id " + id + " succeded"));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				((ComplexPanelI)root).add(new ILabel(324, "id " + id + " failed while being requested"));
			}
		});
//				diff.clear();
//                ((ComplexPanelI)root).add(new ILabel(324, "id " + id + " finished"));
//                diff.add(new ILabel(324, "id " + id + " finished"));
		lastLeaf = id;
		lastParam = param;
	}
	
	static private void queryInit (final Integer id, Parameters param, final ComplexPanel base)
	{
		greetingService.getInitialContent (id, param,
			new AsyncCallback<DiffPatcher>() {
			
			@Override
			public void onSuccess(DiffPatcher result) {
				root = result.createNew(0);
				base.add(root);
				base.add(new HTML("<h2> Received from server: </h2>"));
				diff = new VerticalPanel();
				base.add(diff);
				diff.clear();
				diff.add(new HTML(result.toString()));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				base.add(new HTML("<h1> Sorry, but there an error occured int page loading," + 
								  "with following response:" + caught.getMessage()));
			}
		});
	}

}
