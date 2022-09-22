package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class MissileTargetReachedEvent extends Event
{
	public MissileTargetReachedEvent()
	{
		super(EventType.MISSILE_TARGET_REACHED);
	}
}
