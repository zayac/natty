/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.natty.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import ru.natty.web.shared.Parameters;

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

	static public Parameters curPar = new Parameters(100500);

	@Override
	public void onModuleLoad()
	{
		RootPanel.get().clear();
		ElementReceiver.init (0, curPar, RootPanel.get());
		//RootPanel.get().add(new Label("working"));

	}
}
