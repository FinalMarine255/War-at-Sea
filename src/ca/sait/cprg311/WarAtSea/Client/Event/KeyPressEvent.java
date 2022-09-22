package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class KeyPressEvent extends Event
{
	private char key;
	
	public KeyPressEvent()
	{
		super(EventType.KEY_PRESS);
		key = '\0';
	}
	public KeyPressEvent(char key)
	{
		super(EventType.KEY_PRESS);
		this.key = key;
	}
	
	public char getKey()
	{
		return key;
	}
	public void setKey(char key)
	{
		this.key = key;
	}
}
