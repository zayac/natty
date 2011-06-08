/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.natty.web.server;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author necto
 */
public class MyLog
{
	private static Logger inst = Logger.getLogger("default");
	
	static {
		inst.setLevel(Level.ALL);
	}
	
	public Logger getInstance()
	{
		return inst;
	}
	
	public static void severe (String mess)
	{
		inst.log(new LogRecord(Level.SEVERE, mess));
	}
	
	public static void warning (String mess)
	{
		inst.log(new LogRecord(Level.WARNING, mess));
	}
	
	public static void config (String mess)
	{
		inst.log(new LogRecord(Level.CONFIG, mess));
	}
	
	public static void info (String mess)
	{
		inst.log(new LogRecord(Level.INFO, mess));
	}
	
	public static void fine (String mess)
	{
		inst.log(new LogRecord(Level.FINE, mess));
	}
	
	public static void finer (String mess)
	{
		inst.log(new LogRecord(Level.FINER, mess));
	}
	
	public static void finest (String mess)
	{
		inst.log(new LogRecord(Level.FINEST, mess));
	}
}
