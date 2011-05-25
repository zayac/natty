/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.client.iwidget;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
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
	static private final Integer MAXIMUM_ITEMS = 20;

	static private class ICell extends AbstractCell<IText>
	{
		@Override
		public void render (Context context, IText value, SafeHtmlBuilder sb)
		{
			sb.appendHtmlConstant (value.getText());
		}
	}

	private List<IText> items;
	private VerticalPanel panel;
	private FlowPanel batPanel;
	private Button backB;
	private Button nextB;
	private Button unsel;
//	private Label elems;
//	private Button clnB;
	private CellList list;
	private String name;
	private SingleSelectionModel<IText> ssm;
	private boolean reactToSelChange = true;

	public ITextCellList (final Integer id, final String name)
	{
		super(id, new VerticalPanel());
		panel = (VerticalPanel)getWidget();
		batPanel = new FlowPanel();
		backB = new Button("&#8593;");
		list = new CellList (new ICell());
		nextB = new Button("&#8595;");
//		elems = new Label();
//		clnB = new Button ("&#9747;");//X
		unsel = new Button ("unselect");
		panel.add (batPanel);
		batPanel.add (backB);
		batPanel.add (unsel);
//		batPanel.add (elems);
//		batPanel.add (clnB);

		panel.add (batPanel);
		panel.add (list);
		panel.add (nextB);

		this.name = name;

		ListDataProvider<IText> ldp = new ListDataProvider<IText>();
		items = ldp.getList();

		ldp.addDataDisplay (list);

		ssm = new SingleSelectionModel<IText>();
		list.setSelectionModel (ssm);
		ssm.addSelectionChangeHandler (new SelectionChangeEvent.Handler()
			{
				@Override
				public void onSelectionChange(SelectionChangeEvent event)
				{
					if (!reactToSelChange) return;
					ParamsBuilder.getCurrent().setVal (name, ssm.getSelectedObject().getId().toString());
					ElementReceiver.get().queryPage();
				}
			});
		nextB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				setStart (getStart() + list.getRowCount());
				ElementReceiver.get().queryPage();
			}
		});
		backB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				Integer start = getStart() - MAXIMUM_ITEMS;
				if (start < 0) start = 0;
				setStart (start);
				ElementReceiver.get().queryPage();
			}
		});
		
		unsel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event)
			{
				ParamsBuilder.getCurrent().removeParam(name);
				ElementReceiver.get().queryPage();
			}
		});
		
//		Timer tt = new Timer() {
//
//			@Override
//			public void run() {
//				elems.setText ("els:" + items.size());
//			}
//		};
//		tt.scheduleRepeating(800);
//		tt.run();
		
//		clnB.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event)
//			{
//				items.clear();
//			}
//		});

	}

	public void setHasMore (boolean has)
	{
		nextB.setEnabled (has);
	}

	public void setHasMore()
	{
		setHasMore (items.size() >= MAXIMUM_ITEMS);
	}

	public List<IText> getItems()
	{
		return items;
	}

	private Integer getStart()
	{
		if (!ParamsBuilder.getCurrent().hasParam (getStartName())) return 0;
		return ParamsBuilder.getCurrent().getIntVal (getStartName());
	}

	private String getStartName()
	{
		return name + ".start";
	}

	public void setStart (Integer start)
	{
		backB.setEnabled (start > 0);
		if (start > 0)
			ParamsBuilder.getCurrent().setVal(getStartName(), start.toString());
		else if (ParamsBuilder.getCurrent().hasParam(getStartName()))
				ParamsBuilder.getCurrent().removeParam (getStartName());
	}

	public void selectElement (Integer sel)
	{
		reactToSelChange = false;
		ssm.setSelected(ssm.getSelectedObject(), false);
		if (null != sel)
			for (IText item : items)
				if (item.getId().equals(sel))
					ssm.setSelected (item, true);
		reactToSelChange = true;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for (IText item : items)
			hash ^= item.hashCode();
		return hash;
	}
}
