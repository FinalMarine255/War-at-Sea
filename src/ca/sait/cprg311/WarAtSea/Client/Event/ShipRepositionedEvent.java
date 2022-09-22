package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.Client.ShipOrientation;
import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class ShipRepositionedEvent extends Event
{
	private int prevX;
	private int newX;
	private int prevY;
	private int newY;
	private ShipOrientation prevOrientation;
	private ShipOrientation newOrientation;

	public ShipRepositionedEvent(int prevX, int prevY, ShipOrientation prevOrientation, int newX, int newY, ShipOrientation newOrientation)
	{
		super(EventType.SHIP_REPOSITIONED);

		this.prevX = prevX;
		this.prevY = prevY;
		this.prevOrientation = prevOrientation;
		this.newX = newX;
		this.newY = newY;
		this.newOrientation = newOrientation;
	}

	public int getPreviousX()
	{
		return prevX;
	}
	public int getPreviousY()
	{
		return prevY;
	}
	public ShipOrientation getPreviousOrientation()
	{
		return prevOrientation;
	}
	public int getNewX()
	{
		return newX;
	}
	public int getNewY()
	{
		return newY;
	}
	public ShipOrientation getNewOrientation()
	{
		return newOrientation;
	}
	public void setPreviousX(int prevX)
	{
		this.prevX = prevX;
	}
	public void setPreviousY(int prevY)
	{
		this.prevY = prevY;
	}
	public void setPreviousOrientation(ShipOrientation prevOrientation)
	{
		this.prevOrientation = prevOrientation;
	}
	public void setNewX(int newX)
	{
		this.newX = newX;
	}
	public void setNewY(int newY)
	{
		this.newY = newY;
	}
	public void setNewOrientation(ShipOrientation newOrientation)
	{
		this.newOrientation = newOrientation;
	}
}