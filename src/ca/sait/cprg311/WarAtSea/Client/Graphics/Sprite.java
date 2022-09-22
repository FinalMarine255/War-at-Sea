package ca.sait.cprg311.WarAtSea.Client.Graphics;

import java.awt.Image;

import ca.sait.cprg311.WarAtSea.Client.Event.MouseClickEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.MouseClickType;
import ca.sait.cprg311.WarAtSea.util.Event.EventHandler;

public class Sprite
{
	protected Image image;
	private int x;
	private int y;
	private EventHandler eventHandler;
	
	public Sprite()
	{
		eventHandler = null;
	}
	public Sprite(Image img)
	{
		setImage(img);
		setX(0);
		setY(0);
		eventHandler = null;
	}
	public Sprite(Image img, int x, int y)
	{
		setImage(img);
		setX(x);
		setY(y);
		eventHandler = null;
	}
	
	public Image getImage()
	{
		return image;
	}
	public int getX()
	{
		return this.x;
	}
	public int getY()
	{
		return this.y;
	}
	public int getSpriteWidth()
	{
		return image.getWidth(null);
	}
	public int getSpriteHeight()
	{
		return image.getHeight(null);
	}
	public EventHandler getEventHandler()
	{
		return eventHandler;
	}
	
	private void setImage(Image img)
	{
		this.image = img;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public void setEventHandler(EventHandler handler)
	{
		eventHandler = handler;
	}
	
	public void onClick(int x, int y, MouseClickType type)
	{
		if(eventHandler != null)
		{
			//TODO should this be a call to an observer.  PROBABLY
			eventHandler.handleEvent(new MouseClickEvent(x, y, type));
		}
	}
}
