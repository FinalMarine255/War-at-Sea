package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class AttributeChangedEvent extends Event
{
	private Object prevValue;
	private Object newValue;
	public AttributeChangedEvent()
	{
		super(EventType.ATTRIBUTE_CHANGED);
		prevValue = null;
		newValue = null;
	}
	public AttributeChangedEvent(Object prevValue, Object newValue)
	{
		super(EventType.ATTRIBUTE_CHANGED);
		this.prevValue = prevValue;
		this.newValue = newValue;
	}
	
	public Object getPreviousValue()
	{
		return prevValue;
	}
	public Object getNewValue()
	{
		return newValue;
	}
	public void setPreviousValue(Object obj)
	{
		prevValue = obj;
	}
	public void setNewValue(Object obj)
	{
		newValue = obj;
	}
}
