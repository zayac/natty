package ru.natty.web.client.iwidget;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.SimplePanel;
import ru.natty.web.client.ElementReceiver;
import ru.natty.web.shared.diffpatchers.DiffPatcher;

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
	
	public final void alterContent (IWidget w)
	{
		if (!w.getId().equals(getId())) throw new RuntimeException("attempt to push an Iwidget with different id!");
		((SimplePanel)getWidget()).clear();
		((SimplePanel)getWidget()).add(w);
	}
	
	@Override
	public void ensureCorrectness (DiffPatcher dp)
	{
		Log.debug("checking correctness: " + getId() + "My hash = " + hashCode() + 
				" target_hash " + dp.getRezHash());
		if (getContent().isAggregating())
		{
			getContent().ensureCorrectness(dp);
			return;
		}
		if (dp.getRezHash() == hashCode())
		{
			Log.debug("they are equals, returning");
			return;
		}
		else
			ElementReceiver.get().queryInit(this);
	}
	
	public IWidget getContent()
	{
		return (IWidget)((SimplePanel)getWidget()).getWidget();
	}

	@Override
	public int hashCode() {
		return getContent().hashCode();
	}

}
