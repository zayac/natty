package ru.natty.web.shared;

import java.io.Serializable;

public class Parameters implements Serializable
{
	private String query;
	private Integer elementId;

	public Parameters()
	{
		query = "";
		elementId = 0;
	}
	
	public Parameters (String query, Integer elementId)
	{
		super();
		this.query = query;
		this.elementId = elementId;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery (String query)
	{
		this.query = query;
	}

	public Integer getElementId()
	{
		return elementId;
	}

	public void setElementId (Integer elementId)
	{
		this.elementId = elementId;
	}

	public Parameters copy()
	{
		return new Parameters (query, elementId);
	}
}
