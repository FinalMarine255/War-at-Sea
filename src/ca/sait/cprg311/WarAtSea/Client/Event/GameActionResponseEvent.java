package ca.sait.cprg311.WarAtSea.Client.Event;

import java.io.Serializable;

import ca.sait.cprg311.WarAtSea.Client.GameGridBlockType;
import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class GameActionResponseEvent extends Event implements Serializable
{
	private GameGridBlockType blockType;
	private int x, y;

	public GameActionResponseEvent(int x, int y, GameGridBlockType blockType)
	{
		super(EventType.GAME_ACTION_RESPONSE);
		this.x = x;
		this.y = y;
		this.blockType = blockType;
	}
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public GameGridBlockType getBlockType()
	{
		return blockType;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public void setBlockType(GameGridBlockType blockType)
	{
		this.blockType = blockType;
	}
}