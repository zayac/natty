/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.natty.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Main entry point.
 *
 * @author necto
 */
public class MainEntryPoint implements EntryPoint {
    /** 
     * Creates a new instance of MainEntryPoint
     */
    public MainEntryPoint() {
    }

	@Override
	public void onModuleLoad()
	{
		RootPanel.get().clear();
		ParamsBuilder.getCurrent().setElementId(0);
		ElementReceiver.get().init (RootPanel.get());
		Label lb = new Label("main entry point entered");
		RootPanel.get().add(lb);
	}
}
