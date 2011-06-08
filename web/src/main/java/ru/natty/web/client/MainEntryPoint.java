/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.natty.web.client;

import com.allen_sauer.gwt.log.client.ConsoleLogger;
import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
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
		ParamsBuilder.getCurrent().setId(0);
		ElementReceiver.get().init (RootPanel.get());
		Log.debug("llog mess");
	}
}
