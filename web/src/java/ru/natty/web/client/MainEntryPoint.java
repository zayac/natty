/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.natty.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import org.miller.gwt.client.sound.Callback;
import org.miller.gwt.client.sound.SoundManager;
import ru.natty.web.client.iwidget.IPlayer;

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
		Label lb = new Label("main entry point entered");

//		RootPanel.get().add(new IPlayer(100500, "music/Alternative/Deepfield/[2007] Achetypes And Repitition/07 - Into The Flood.mp3"));
//		RootPanel.get().add(new IPlayer(100500, "ogn.mp3"));


		RootPanel.get().add(lb);
	}
}
