package ca.sait.cprg311.WarAtSea.Client.Event;

import java.io.Serializable;

import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class GameActionRequestEvent extends Event implements Serializable
{
	private int x, y;
	
	public GameActionRequestEvent(int x, int y)
	{
		super(EventType.GAME_ACTION_REQUEST);
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return this.x;
	}
	public int getY()
	{
		return this.y;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public void setY(int y)
	{
		this.y = y;
	}
}