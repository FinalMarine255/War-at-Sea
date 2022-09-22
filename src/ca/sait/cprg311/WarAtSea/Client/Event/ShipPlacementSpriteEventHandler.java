package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventHandler;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class ShipPlacementSpriteEventHandler implements EventHandler
{
	@Override
	public void handleEvent(Event event)
	{
		if(event.getEventType() == EventType.MOUSE_CLICK)
		{
			
		}
	}
}
