package ru.natty.web.shared;

import java.io.Serializable;

public class Parameters implements Serializable
{
	private Integer gid;

	public Parameters()
	{
		gid = 0;
	}
	
	public Parameters(Integer gid) {
		super();
		this.gid = gid;
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}
}
