package ca.sait.cprg311.WarAtSea.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Observable;
import java.util.UUID;

import ca.sait.cprg311.WarAtSea.util.ChatNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessageCommand;
import ca.sait.cprg311.WarAtSea.util.NetworkMessage;
import ca.sait.cprg311.WarAtSea.util.NetworkMessageType;

public class ClientHandler extends Observable implements Runnable
{
	private Client client;
	
	public ClientHandler(Client client)
	{
		this.client = client;
		
		sendMessage(new ControlNetworkMessage(NetworkMessage.ServerSenderId, ControlNetworkMessageCommand.CLIENT_ID_RESPONSE, client.getId()));
	}
	
	public void sendMessage(NetworkMessage msg)
	{
		try {
			client.getOutputStream().writeObject(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean clientConnected()
	{
		return client.isConnected();
	}
	public UUID getClientId()
	{
		return client.getId();
	}
	public void closeClient()
	{
		client.close();
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			NetworkMessage msg = null;
			try {
				msg = (NetworkMessage)client.getInputStream().readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block

				setChanged();
				notifyObservers(new ControlNetworkMessage(client.getId(), ControlNetworkMessageCommand.MATCHMAKING_QUIT, null));
				break;
			}
			if(msg.getNetworkMessageType() == NetworkMessageType.GAME_ACTION_MESSAGE || msg.getNetworkMessageType() == NetworkMessageType.CHAT_MESSAGE)
			{
				setChanged();
				notifyObservers(msg);
			}
			else if(msg.getNetworkMessageType() == NetworkMessageType.CONTROL_MESSAGE)
			{
				ControlNetworkMessage command = (ControlNetworkMessage)msg;
				switch(command.getCommand())
				{
				case CLIENT_ID_REQUEST:
					ControlNetworkMessage response = new ControlNetworkMessage(NetworkMessage.ServerSenderId, ControlNetworkMessageCommand.CLIENT_ID_RESPONSE, ClientIdManager.getClientIdManager().generateId());
					sendMessage(response);
					break;
				default:
					setChanged();
					notifyObservers(command);
				}
			}
			else
			{
				
			}
		}
	}
}
