package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class ChatMessageRecievedEvent extends Event
{
	private String senderName;
	private String msg;
	
	public ChatMessageRecievedEvent(String senderName, String msg)
	{
		super(EventType.CHAT_MESSAGE_RECIEVED);
		this.senderName = senderName;
		this.msg = msg;
	}
	public String getSenderName()
	{
		return senderName;
	}
	public String getMessage()
	{
		return msg;
	}
}
