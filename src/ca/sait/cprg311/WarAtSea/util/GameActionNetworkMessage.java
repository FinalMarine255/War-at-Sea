package ca.sait.cprg311.WarAtSea.util;

import java.util.UUID;

import ca.sait.cprg311.WarAtSea.util.Event.Event;

public class GameActionNetworkMessage extends NetworkMessage
{
	private Event actionObj;
	private boolean fromServer;

	public GameActionNetworkMessage(UUID senderId, boolean fromServer, Event obj)
	{
		super(NetworkMessageType.GAME_ACTION_MESSAGE, senderId);
		this.actionObj = obj;
		this.fromServer = fromServer;
	}
	
	public Event getActionObject()
	{
		return actionObj;
	}
	public void setActionObject(Event obj)
	{
		this.actionObj = obj;
	}
	
	public boolean isFromServer()
	{
		return fromServer;
	}
	public void setFromServer(boolean fromServer)
	{
		this.fromServer = fromServer;
	}
}
