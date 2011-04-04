package ru.natty.web.shared;

import ru.natty.web.client.IVoid;
import ru.natty.web.client.IWidget;

public class VoidDP implements DiffPatcher {

	@Override
	public boolean isVoid()
	{
		return true;
	}
	
	@Override
	public IWidget createNew(int id) {
		return new IVoid (id);
	}

	@Override
	public void applay(IWidget w) {
		assert w.isVoid();
	}

	@Override
	public String toString() {
		return "<font color=#0000ff>VOID</font>";
	}

	
}
