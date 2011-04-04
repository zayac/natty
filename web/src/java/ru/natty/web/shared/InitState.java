package ru.natty.web.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InitState implements Serializable {
	private Integer id;
	private String names;
//	Object oo = new Object();
	
	public InitState (String names, Integer id){
		this.id = id;
		this.names = names;
	}

	public Integer getId() {
		return id;
	}

	public String getNames() {
		return names;
	}
	public InitState ()
	{
		id = 0;
		names = "uninitialized!!";
	}

}
