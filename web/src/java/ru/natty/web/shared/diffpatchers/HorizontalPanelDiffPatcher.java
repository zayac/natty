package ru.natty.web.shared.diffpatchers;

import ru.natty.web.client.iwidget.IHorizontalPanel;
import ru.natty.web.client.iwidget.IWidget;

import com.google.gwt.user.client.ui.Widget;

public class HorizontalPanelDiffPatcher extends ComplexPanelDiffPatcher
{
	@Override
	protected IWidget create(Integer id) {
		return new IHorizontalPanel (id);
	}
}
