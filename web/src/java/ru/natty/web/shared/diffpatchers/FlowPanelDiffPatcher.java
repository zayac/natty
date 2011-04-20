/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.shared.diffpatchers;

import ru.natty.web.client.iwidget.IFlowPanel;
import ru.natty.web.client.iwidget.IWidget;

/**
 *
 * @author necto
 */
public class FlowPanelDiffPatcher extends ComplexPanelDiffPatcher
{
	@Override
	protected IWidget create(Integer id) {
		return new IFlowPanel (id);
	}
}
