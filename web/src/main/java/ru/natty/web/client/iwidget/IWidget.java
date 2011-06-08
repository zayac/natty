package ru.natty.web.client.iwidget;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import ru.natty.web.shared.diffpatchers.DiffPatcher;

public abstract class IWidget extends Composite implements Identified 
{
	private Integer id;
	
	public IWidget (Integer id, Widget w)
	{
		this.id = id;
		initWidget (w);
	}
	
	@Override
	final public Integer getId()
	{
		return id;
	}

	@Override
	abstract public int hashCode();
	
	public void ensureCorrectness (DiffPatcher dp)
	{
		//do nothing in general case
	}

	@Override
	public void setStylePrimaryName(String style)
	{
		getWidget().setStylePrimaryName (style);
	}
	
	public boolean isAggregating()
	{
		return false;
	}
	
	public boolean isStreak()
	{
		return false;
	}
	
	public boolean isVoid()
	{
		return false;
	}


	interface WidgetConverter<ReturnType, TakenType>
	{
		public ReturnType convert (TakenType w);
	}
	class IteratorAdapter<GoalType, TakenType> implements Iterator<GoalType>
	{
		Iterator<TakenType> iter;
		WidgetConverter<GoalType, TakenType> trans;
		public IteratorAdapter (Iterator<TakenType> i, WidgetConverter<GoalType, TakenType> transformator)
		{
			this.iter = i;
			trans = transformator;
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public GoalType next() {
			return trans.convert (iter.next());//(IWidget)iter.next();
		}

		@Override
		public void remove() {
			iter.remove();
		}
	};
}
