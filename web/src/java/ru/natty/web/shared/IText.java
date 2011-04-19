/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.web.shared;

import java.io.Serializable;

/**
 *
 * @author necto
 */

public class IText implements Serializable
{
	Integer id;
	String text;

	public void setText(String text) {
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public String getText()
	{
		return text;
	}

	public IText() {}

	public IText (Integer id, String text)
	{
		this.id = id;
		this.text = text;
	}

	@Override
	public int hashCode()
	{
		return id;
	}

	@Override
	public boolean equals (Object o)
	{
		return o instanceof IText && ((IText)o).text.equals(text) &&
									 ((IText)o).id.equals(id);
	}

	public IText copy()
	{
		return new IText (id, text);
	}

	@Override
	public String toString()
	{
		return	"IText@<font color=#008800>" + id +
				"</font>:<font color=#aa0000>" + text +
				"</font>";
	}
}
