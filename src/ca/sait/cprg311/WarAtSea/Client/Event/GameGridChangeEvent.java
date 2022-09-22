package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.Client.GameGridBlockType;

public class GameGridChangeEvent extends AttributeChangedEvent
{
	private boolean affectsAll;
	private int x;
	private int y;
	public GameGridChangeEvent()
	{
		super();
		affectsAll = false;
		x = 0;
		y = 0;
	}
	public GameGridChangeEvent(GameGridBlockType prev, GameGridBlockType newType, int x, int y)
	{
		super(prev, newType);
		affectsAll = false;
		this.x = x;
		this.y = y;
	}
	public GameGridChangeEvent(GameGridBlockType newType)
	{
		super(null, newType);
		affectsAll = true;
	}
	
	public boolean affectsAll()
	{
		return this.affectsAll;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
}
