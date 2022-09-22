package ca.sait.cprg311.WarAtSea.util;

import java.io.Serializable;
import java.util.UUID;

public class NetworkMessage implements Serializable
{
	public static final UUID VoidSenderId = new UUID(0, 0);
	public static final UUID ServerSenderId = new UUID(0, 1);
	
	private NetworkMessageType msgType;
	private UUID senderId;
	
	public NetworkMessage()
	{
		msgType = NetworkMessageType.UNKNOWN_MESSAGE;
		senderId = NetworkMessage.VoidSenderId;
	}
	public NetworkMessage(NetworkMessageType msgType, UUID senderId)
	{
		this.msgType = msgType;
		this.senderId = senderId;
	}
	
	public UUID getSenderId()
	{
		return this.senderId;
	}
	public NetworkMessageType getNetworkMessageType()
	{
		return this.msgType;
	}
	
	public void setSenderId(UUID senderId)
	{
		this.senderId = senderId;
	}
}
