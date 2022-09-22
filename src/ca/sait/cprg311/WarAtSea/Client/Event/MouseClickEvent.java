package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class MouseClickEvent extends Event
{
	private int clickedX;
	private int clickedY;
	private MouseClickType clickType;
	
	public MouseClickEvent()
	{
		super(EventType.MOUSE_CLICK);
		clickedX = -1;
		clickedY = -1;
		clickType = MouseClickType.LEFT_CLICK;
	}
	public MouseClickEvent(int x, int y, MouseClickType clickType)
	{
		super(EventType.MOUSE_CLICK);
		clickedX = x;
		clickedY = y;
		this.clickType = clickType;
	}
	
	public int getClickedX()
	{
		return clickedX;
	}
	public int getClickedY()
	{
		return clickedY;
	}
	public MouseClickType getClickType()
	{
		return clickType;
	}
	public void setClickedX(int x)
	{
		clickedX = x;
	}
	public void setClickedY(int y)
	{
		clickedY = y;
	}
	public void setClickType(MouseClickType clickType)
	{
		this.clickType = clickType;
	}
}
