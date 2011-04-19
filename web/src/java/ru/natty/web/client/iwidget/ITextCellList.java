/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client.iwidget;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.List;
import ru.natty.web.client.ElementReceiver;
import ru.natty.web.client.ParamsBuilder;
import ru.natty.web.shared.IText;

/**
 *
 * @author necto
 */
public class ITextCellList extends IWidget
{
	static private class ICell extends AbstractCell<IText>
	{
		@Override
		public void render (Context context, IText value, SafeHtmlBuilder sb)
		{
			sb.appendHtmlConstant (value.getText());
		}
	}

	private List<IText> items;

	public ITextCellList (final Integer id, final String name) //TODO: Add selected current
	{
		super(id, new CellList (new ICell()));

		ListDataProvider<IText> ldp = new ListDataProvider<IText>();
		items = ldp.getList();

		ldp.addDataDisplay (((CellList)getWidget()));

		final SingleSelectionModel<IText> ssm = new SingleSelectionModel<IText>();
		((CellList)getWidget()).setSelectionModel (ssm);
		ssm.addSelectionChangeHandler (new SelectionChangeEvent.Handler()
			{
				@Override
				public void onSelectionChange(SelectionChangeEvent event)
				{
					ParamsBuilder.getCurrent().setVal(name, ssm.getSelectedObject().getId().toString());
					ElementReceiver.get().queryElement();
				}
			});
	}

	public List<IText> getItems()
	{
		return items;
	}
}
