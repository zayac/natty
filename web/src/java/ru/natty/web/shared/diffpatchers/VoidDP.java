package ru.natty.web.shared.diffpatchers;

import ru.natty.web.client.iwidget.IVoid;
import ru.natty.web.client.iwidget.IWidget;

public class VoidDP extends DiffPatcher {

	@Override
	public boolean isVoid()
	{
		return true;
	}
	
	@Override
	protected IWidget createNewInt (int id) {
		return new IVoid (id);
	}

	@Override
	protected void applayInt (IWidget w) {
		assert w.isVoid();
	}

	@Override
	public String toString() {
		return "<font color=#0000ff>VOID</font>";
	}

	
}
