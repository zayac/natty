package ru.natty.web.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

public class Parameters implements Serializable
{
	private Integer id;
	private HashMap<String, String> map;

	public Parameters()
	{
		super();
		map = new HashMap<String, String>();
		id = 0;
	}

	public Integer getId()
	{
		return id;
	}

	public boolean hasParam (String name)
	{
		return map.containsKey(name);
	}

	public String removeParam (String name)
	{
		return map.remove(name);
	}

	public void setId (Integer elementId)
	{
		this.id = elementId;
	}

	public String getVal (String name)
	{
		String val = map.get(name);
		if (null == val) val = "";
		return val;
	}

	public Integer getIntVal (String name)
	{
		return Integer.parseInt (getVal(name));
	}

	public String setVal (String name, String val)
	{
		return map.put(name, val);
	}

	public Parameters copy()
	{
		Parameters ret = new Parameters();
		ret.id = id;
		ret.map.putAll(map);
		return ret;
	}
	
	public void copy (Parameters from)
	{
		id = from.id;
		map.clear();
		map.putAll (from.map);
	}

	@Override
	public String toString()
	{
		String ret = "id=" + id.toString();

		for(Entry<String, String> entry :map.entrySet())
			if (!entry.getKey().equals("")) //There is always unnamed parameter appears here 
				ret += "&" + entry.getKey() + "=" + entry.getValue();
		return ret;
	}

	public void byString (String str)
	{
		for (String param : str.split("&"))
		{
			String[] arr = param.split("=");
			assert arr.length > 1 : "each parameter must contain a '=' character";
			String name = arr[0];
			String val = arr[1];
			if (name.equals("id"))
			{
				id = Integer.parseInt(val);
				continue;
			}
			map.put(name, val);
		}
	}
}
