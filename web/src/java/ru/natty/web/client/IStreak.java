package ru.natty.web.client;

import com.google.gwt.user.client.ui.SimplePanel;

public class IStreak extends IWidget {

	public IStreak(IWidget w) {
		super(w.getId(), new SimplePanel());
		alterContent(w);
	}

	public boolean isStreak()
	{
		return true;
	}
	
	public boolean containsVoid()
	{
		return getContent().isVoid();
	}
	
	public void alterContent (IWidget w)
	{
		if (!w.getId().equals(getId())) throw new RuntimeException("attempt to push an Iwidget with different id!");
		((SimplePanel)getWidget()).clear();
		((SimplePanel)getWidget()).add(w);
	}
	
	public IWidget getContent()
	{
		return (IWidget)((SimplePanel)getWidget()).getWidget();
	}

}
