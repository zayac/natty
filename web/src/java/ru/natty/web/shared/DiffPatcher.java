package ru.natty.web.shared;

import java.io.Serializable;
import ru.natty.web.client.IWidget;

public interface DiffPatcher extends Serializable
{
	public IWidget createNew (int id);
	public void applay (IWidget w);
	public boolean isVoid();
	
	public String toString();
}
