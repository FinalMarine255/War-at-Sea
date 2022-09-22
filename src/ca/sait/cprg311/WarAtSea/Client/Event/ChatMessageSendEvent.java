package ca.sait.cprg311.WarAtSea.Client.Event;

import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class ChatMessageSendEvent extends Event
{
	private String msg;
	
	public ChatMessageSendEvent(String msg)
	{
		super(EventType.CHAT_MESSAGE_SEND);
		this.msg = msg;
	}
	public String getMessage()
	{
		return msg;
	}
}
