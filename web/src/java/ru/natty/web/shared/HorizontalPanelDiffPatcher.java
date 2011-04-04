package ru.natty.web.shared;

import ru.natty.web.client.IHorizontalPanel;
import ru.natty.web.client.IWidget;

import com.google.gwt.user.client.ui.Widget;

public class HorizontalPanelDiffPatcher extends ComplexPanelDiffPatcher
{
	@Override
	protected IWidget create(Integer id) {
		return new IHorizontalPanel (id);
	}
}
