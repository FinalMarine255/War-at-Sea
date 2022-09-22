package ca.sait.cprg311.WarAtSea.Client;

import java.util.Observable;

import ca.sait.cprg311.WarAtSea.Client.Event.ShipRepositionedEvent;

public class Ship extends Observable
{
	private ShipOrientation orientation;
	private int x;
	private int y;
	private ShipType type;
	private int code;
	
	public Ship(int x, int y, ShipOrientation orientation, ShipType shipType, int shipCode)
	{
		this.x = x;
		this.y = y;
		this.orientation = orientation;
		this.type = shipType;
		this.code = shipCode;
	}
	
	public ShipOrientation getOrientation()
	{
		return orientation;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public ShipType getShipType()
	{
		return type;
	}
	public int getShipCode()
	{
		return this.code;
	}
	
	public void setOrientation(ShipOrientation orientation)
	{
		ShipRepositionedEvent event = new ShipRepositionedEvent(this.x, this.y, this.orientation, this.x, this.y, null);
		this.orientation = orientation;
		event.setNewOrientation(orientation);
		setChanged();
		notifyObservers(event);
	}
	public void setX(int x)
	{
		ShipRepositionedEvent event = new ShipRepositionedEvent(this.x, this.y, this.orientation, -1, this.y, this.orientation);
		this.x = x;
		event.setNewX(x);
		setChanged();
		notifyObservers(event);
	}
	public void setY(int y)
	{
		ShipRepositionedEvent event = new ShipRepositionedEvent(this.x, this.y, this.orientation, this.x, -1, this.orientation);
		this.y = y;
		event.setNewY(y);
		setChanged();
		notifyObservers(event);
	}
}
