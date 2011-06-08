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
public class ServerException extends Exception implements Serializable
{
//	private String message;
//	
	public ServerException(){}
	
	public ServerException (String message)
	{
		super(message);
	}
//
//	/**
//	 * @return the message
//	 */
//	public String getMessage() {
//		return message;
//	}
//
//	/**
//	 * @param message the message to set
//	 */
//	public void setMessage(String message) {
//		this.message = message;
//	}
}
