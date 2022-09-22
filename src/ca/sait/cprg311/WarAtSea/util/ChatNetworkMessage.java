package ca.sait.cprg311.WarAtSea.util;

import java.util.UUID;

public class ChatNetworkMessage extends NetworkMessage
{
	private String msg;
	private String senderName;
	
	public ChatNetworkMessage(UUID senderId, String senderName, String msg)
	{
		super(NetworkMessageType.CHAT_MESSAGE, senderId);
		this.msg = msg;
		this.senderName = senderName;
	}
	
	public String getMessage()
	{
		return msg;
	}
	public void setMessage(String msg)
	{
		this.msg = msg;
	}
	
	public String getSenderName()
	{
		return senderName;
	}
	public void setSenderName(String senderName)
	{
		this.senderName = senderName;
	}
}
