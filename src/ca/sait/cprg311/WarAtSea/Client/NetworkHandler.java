package ca.sait.cprg311.WarAtSea.Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import ca.sait.cprg311.WarAtSea.Client.Event.ChatMessageRecievedEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.GameActionRequestEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.GameActionResponseEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.GameStateChangeEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.GameStateParam;
import ca.sait.cprg311.WarAtSea.Client.Event.NetworkMessageRecievedEvent;
import ca.sait.cprg311.WarAtSea.util.ChatNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessageCommand;
import ca.sait.cprg311.WarAtSea.util.GameActionNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.NetworkMessage;
import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class NetworkHandler extends Observable implements Observer
{
	private NetworkConnection conn;
	private UUID clientId;
	
	public NetworkHandler(String host, int port) throws UnknownHostException, IOException
	{
		conn = new NetworkConnection(host, port);
		//TODO
		conn.addObserver(this);
		conn.connect();
		Thread networkThread = new Thread(conn, "Network Thread");
		networkThread.start();
	}
	public NetworkHandler(NetworkConnection connection)
	{
		conn = connection;
		conn.addObserver(this);
	}
	
	public void sendMessage(NetworkMessage msg)
	{
		msg.setSenderId(clientId);
		conn.sendMessage(msg);
	}
	
	@Override
	public void update(Observable arg0, Object arg1)
	{
		// TODO Auto-generated method stub
		Event event = (Event)arg1;
		if(event.getEventType() == EventType.NETWORK_MESSAGE_RECIEVED)
		{
			NetworkMessage msg = ((NetworkMessageRecievedEvent)event).getNetworkMessage();
			switch(msg.getNetworkMessageType())
			{
			case CONTROL_MESSAGE:
				ControlNetworkMessage controlMsg = (ControlNetworkMessage)msg;
				switch(controlMsg.getCommand())
				{
				case CLIENT_ID_RESPONSE:
					clientId = (UUID)controlMsg.getParam();
					break;
				case MATCHMAKING_RESTARTING:
					setChanged();
					notifyObservers(new GameStateChangeEvent(GameStateParam.MATCHMAKING_STATUS, null));
					break;
				case MATCHMAKING_FOUND:
					setChanged();
					notifyObservers(new GameStateChangeEvent(GameStateParam.MATCHMAKING_STATUS, MatchmakingStatus.IN_MATCH));
					break;
				case MATCHMAKING_QUIT:
					if(controlMsg.getSenderId().compareTo(NetworkMessage.ServerSenderId) == 0)
					{
						setChanged();
						notifyObservers(new GameStateChangeEvent(GameStateParam.MATCHMAKING_STATUS, MatchmakingStatus.SERVER_ENDED_MATCH));
					}
					else
					{
						setChanged();
						notifyObservers(new GameStateChangeEvent(GameStateParam.MATCHMAKING_STATUS, MatchmakingStatus.OPONENT_LEFT));
					}
					break;
				case GAME_SYNC:
					setChanged();
					notifyObservers(new Event(EventType.GAME_SYNC));
					break;
				default:
					System.out.println("Unknown message, ignoring");
				}
				break;
			case CHAT_MESSAGE:
				ChatNetworkMessage chatMsg = (ChatNetworkMessage)msg;
				setChanged();
				notifyObservers(new ChatMessageRecievedEvent(chatMsg.getSenderName(), chatMsg.getMessage()));
				break;
			case GAME_ACTION_MESSAGE:
				GameActionNetworkMessage gameActionMsg = (GameActionNetworkMessage)msg;
				switch(gameActionMsg.getActionObject().getEventType())
				{
				case GAME_ACTION_RESPONSE:
					setChanged();
					notifyObservers((GameActionResponseEvent)gameActionMsg.getActionObject());
					break;
				case GAME_ACTION_REQUEST:
					setChanged();
					notifyObservers((GameActionRequestEvent)gameActionMsg.getActionObject());
					break;
				case GAME_ACTION_PLAYER_READY:
					setChanged();
					notifyObservers(new GameStateChangeEvent(GameStateParam.OPONENT_READY, null));
					break;
				case GAME_ACTION_PLAYER_TURN:
					setChanged();
					notifyObservers(new GameStateChangeEvent(GameStateParam.PLAYER_TURN, true));
					break;
				case STATE_CHANGE:
					setChanged();
					notifyObservers((GameStateChangeEvent)gameActionMsg.getActionObject());
				}
				break;
			default:
				System.out.println("Unkown message, ignoring");
			}
		}
	}
}
