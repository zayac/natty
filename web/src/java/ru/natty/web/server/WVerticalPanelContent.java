package ru.natty.web.server;

import ru.natty.web.shared.ComplexPanelDiffPatcher;
import ru.natty.web.shared.DiffPatcher;
import ru.natty.web.shared.HorizontalPanelDiffPatcher;
import ru.natty.web.shared.VerticalPanelDiffPatcher;

public class WVerticalPanelContent extends WComplexPanelContent
{
	public WVerticalPanelContent()
	{
		super(VerticalPanelDiffPatcher.class);
	}
}
