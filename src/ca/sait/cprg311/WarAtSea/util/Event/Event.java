package ca.sait.cprg311.WarAtSea.util.Event;

import java.io.Serializable;

public class Event implements Serializable
{
	private EventType eventType;
	
	public Event(EventType type)
	{
		eventType = type;
	}
	public EventType getEventType()
	{
		return eventType;
	}
	public void setEventType(EventType type)
	{
		eventType = type;
	}
}
