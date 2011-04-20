package ru.natty.web.shared.diffpatchers;

import com.google.gwt.user.client.ui.Widget;

import ru.natty.web.client.iwidget.IVerticalPanel;
import ru.natty.web.client.iwidget.IWidget;




public class VerticalPanelDiffPatcher extends ComplexPanelDiffPatcher
{

	@Override
	protected IWidget create(Integer id) {
		return new IVerticalPanel (id);
	}
}
