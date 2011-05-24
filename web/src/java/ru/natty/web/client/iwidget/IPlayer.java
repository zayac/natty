/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client.iwidget;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import org.miller.gwt.client.sound.Callback;
import org.miller.gwt.client.sound.SoundManager;

/**
 *
 * @author necto
 */
public class IPlayer extends IWidget
{
	private final String SOUND_ID = "soundID";
	private Label url;
	private DecoratorPanel panel;
	private boolean snd_playing;
	
	public IPlayer (Integer id)
	{
		super(id, new DecoratorPanel());
		panel = (DecoratorPanel)getWidget();
		url = new Label();
		panel.setWidget(url);
		tuneSoundManager();
	}

	public IPlayer (Integer id, final String nurl)
	{
		super(id, new DecoratorPanel());
		panel = (DecoratorPanel)getWidget();
		url = new Label();
		panel.setWidget(url);
		tuneSoundManager();
	}

	private void tuneSoundManager()
	{
		final SoundManager sm = SoundManager.getInstance();
				sm.setDebugMode(true);
				sm.getDefaultOptions().autoLoad(true);
				sm.getDefaultOptions().autoPlay(true);
				snd_playing = false;
				sm.getDefaultOptions().onPlay(new Callback() {
			@Override
			public void execute() {
				snd_playing = true;
			}
		});
	}

	public void setNewUrl (final String nUrl)
	{
		url.setText("now playing: " + nUrl);
		final SoundManager sm = SoundManager.getInstance();
		if (snd_playing)
		{
			sm.stop(SOUND_ID);
			sm.destroySound(SOUND_ID);
			snd_playing = false;
		}
		sm.createSound(SOUND_ID, URL.encode(nUrl));
		sm.play(SOUND_ID);
	}

	@Override
	public int hashCode() {
		return url.hashCode();
	}

}
