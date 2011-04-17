package ru.natty.web.server.wcontent;

import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.VoidDP;

public class WVoid extends WContent {

	@Override
	public DiffPatcher getDifferenceInt(WContent prev, boolean amputation) {
		if (amputation)
			return new VoidDP(); 
		return null;
	}

	@Override
	public DiffPatcher getAllContentInt() {
		return new VoidDP();
	}

	public boolean isNotVoid() {return false;}
	
	@Override
	public WContent copyInt() {
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
