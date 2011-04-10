package ru.natty.web.server;

import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.VoidDP;

public class WVoid extends WContent {

	@Override
	public DiffPatcher getDifference(WContent prev, boolean amputation) {
		if (amputation)
			return new VoidDP(); 
		return null;
	}

	@Override
	public DiffPatcher getAllContent() {
		return new VoidDP();
	}

	public boolean isNotVoid() {return false;}
	
	@Override
	public WContent copy() {
		return new WVoid();
	}

	@Override
	public boolean isAggregating() {
		return false;
	}

	public static WVoid make()
	{
		return new WVoid();
	}
}
