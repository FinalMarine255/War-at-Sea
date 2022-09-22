package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class GameStateChangeEvent extends Event
{
	private GameStateParam param;
	private Object value;
	
	public GameStateChangeEvent(GameStateParam param, Object value)
	{
		super(EventType.STATE_CHANGE);
		this.param = param;
		this.value = value;
	}
	
	public GameStateParam getParam()
	{
		return param;
	}
	public Object getValue()
	{
		return value;
	}
	public void setParam(GameStateParam param)
	{
		this.param = param;
	}
	public void setValue(Object value)
	{
		this.value = value;
	}
}
