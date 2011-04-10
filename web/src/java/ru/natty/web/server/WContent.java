package ru.natty.web.server;

import ru.natty.web.shared.DiffPatcher;

public abstract class WContent
{
	public abstract DiffPatcher getDifference (WContent prev, boolean amputation);	
	public abstract DiffPatcher getAllContent();
	public abstract WContent copy();
	public boolean isNotVoid() {return true;}
	public abstract boolean isAggregating();
}
