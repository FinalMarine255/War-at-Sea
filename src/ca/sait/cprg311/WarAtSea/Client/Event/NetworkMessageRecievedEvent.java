package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.util.NetworkMessage;
import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class NetworkMessageRecievedEvent extends Event
{
	private NetworkMessage msg;
	
	public NetworkMessageRecievedEvent()
	{
		super(EventType.NETWORK_MESSAGE_RECIEVED);
		msg = null;
	}
	public NetworkMessageRecievedEvent(NetworkMessage msg)
	{
		super(EventType.NETWORK_MESSAGE_RECIEVED);
		this.msg = msg;
	}

	public synchronized NetworkMessage getNetworkMessage()
	{
		return msg;
	}
	public synchronized void setNetworkMessage(NetworkMessage msg)
	{
		this.msg = msg;
	}
}
