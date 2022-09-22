package ca.sait.cprg311.WarAtSea.util;

import java.util.UUID;

public class ControlNetworkMessage extends NetworkMessage
{
	private ControlNetworkMessageCommand command;
	private Object param;
	
	public ControlNetworkMessage()
	{
		super(NetworkMessageType.CONTROL_MESSAGE, NetworkMessage.VoidSenderId);
		command = ControlNetworkMessageCommand.NO_COMMAND;
		param = null;
	}
	public ControlNetworkMessage(UUID senderId, ControlNetworkMessageCommand command, Object param)
	{
		super(NetworkMessageType.CONTROL_MESSAGE, senderId);
		this.command = command;
		this.param = param;
	}
	
	public ControlNetworkMessageCommand getCommand()
	{
		return this.command;
	}
	public Object getParam()
	{
		return this.param;
	}
	public void setCommand(ControlNetworkMessageCommand command)
	{
		this.command = command;
	}
	public void setParam(Object param)
	{
		this.param = param;
	}
}
