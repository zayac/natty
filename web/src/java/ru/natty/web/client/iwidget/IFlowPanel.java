/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client.iwidget;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 *
 * @author necto
 */
public class IFlowPanel extends IComplexPanel
{
	public IFlowPanel (Integer id)
	{
		super (id, new FlowPanel());
	}

	@Override
	public void insert(IWidget w, int beforeIndex) {
		((FlowPanel)getWidget()).insert(w, beforeIndex);
	}
}
