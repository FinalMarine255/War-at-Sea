package ca.sait.cprg311.WarAtSea.Server;

import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.UUID;

import ca.sait.cprg311.WarAtSea.util.ChatNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessageCommand;
import ca.sait.cprg311.WarAtSea.util.GameActionNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.NetworkMessage;
import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;

public class Match implements Runnable, Observer
{
	private ClientHandler client1;
	private ClientHandler client2;
	
	private boolean client1Ready;
	private boolean client2Ready;
	private boolean matchStarted;
	private boolean matchProposed;
	
	public Match(ClientHandler c1, ClientHandler c2)
	{
		client1Ready = false;
		client2Ready = false;
		matchStarted = false;
		matchProposed = false;
		client1 = c1;
		client2 = c2;
		client1.addObserver(this);
		client2.addObserver(this);
		client1.deleteObserver(MatchMakingManager.getMatchMakingManager());
		client2.deleteObserver(MatchMakingManager.getMatchMakingManager());

		client1.sendMessage(new ControlNetworkMessage(null, ControlNetworkMessageCommand.MATCHMAKING_FOUND, null));
		client2.sendMessage(new ControlNetworkMessage(null, ControlNetworkMessageCommand.MATCHMAKING_FOUND, null));
	}
	
	public void endMatch()
	{
		client1.sendMessage(new ControlNetworkMessage(NetworkMessage.ServerSenderId, ControlNetworkMessageCommand.MATCHMAKING_QUIT, null));
		client2.sendMessage(new ControlNetworkMessage(NetworkMessage.ServerSenderId, ControlNetworkMessageCommand.MATCHMAKING_QUIT, null));
		client1.closeClient();
		client2.closeClient();
	}
	
	@Override
	public void run()
	{
		boolean running = true;
		
		//make sure both clients are connected, if not then throw the one that is back into matchmaking, and end this thread.
		if(client1.clientConnected() && !client2.clientConnected())
		{
			MatchMakingManager.getMatchMakingManager().addToMatchMakingQueue(client2);
			ControlNetworkMessage msg = new ControlNetworkMessage(NetworkMessage.ServerSenderId, ControlNetworkMessageCommand.MATCHMAKING_RESTARTING, null);
			client2.sendMessage(msg);
		}
		else if(client2.clientConnected() && !client1.clientConnected())
		{
			MatchMakingManager.getMatchMakingManager().addToMatchMakingQueue(client1);
			ControlNetworkMessage msg = new ControlNetworkMessage(NetworkMessage.ServerSenderId, ControlNetworkMessageCommand.MATCHMAKING_RESTARTING, null);
			client1.sendMessage(msg);
		}
		else if(!client2.clientConnected() && !client1.clientConnected())
		{
			//MatchMakingManager.getMatchMakingManager().addToMatchMakingQueue(client1);
			//MatchMakingManager.getMatchMakingManager().addToMatchMakingQueue(client2);
			//ControlNetworkMessage msg = new ControlNetworkMessage(NetworkMessage.ServerSenderId, ControlNetworkMessageCommand.MATCHMAKING_RESTARTING, null);
			//client2.sendMessage(msg);
			//client1.sendMessage(msg);
		}
		running = true;
		
		while(running)
		{
		//needs to be a be a thread...	
		}
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		NetworkMessage msg = (NetworkMessage)arg1;
		switch(msg.getNetworkMessageType())
		{
		case GAME_ACTION_MESSAGE:
			GameActionNetworkMessage gameActionMsg = (GameActionNetworkMessage)arg1;
			UUID msgSender = gameActionMsg.getSenderId();
			if(client1.getClientId().compareTo(msgSender) == 0)
			{
				if(gameActionMsg.getActionObject().getEventType() == EventType.GAME_ACTION_PLAYER_READY)
				{
					client1Ready = true;
				}
				client2.sendMessage(gameActionMsg);
			}
			else if(client2.getClientId().compareTo(msgSender) == 0)
			{
				if(gameActionMsg.getActionObject().getEventType() == EventType.GAME_ACTION_PLAYER_READY)
				{
					client2Ready = true;
				}
				client1.sendMessage(gameActionMsg);
			}
			
			break;
		case CONTROL_MESSAGE:
			ControlNetworkMessage controlMsg = (ControlNetworkMessage)arg1;
			if(controlMsg.getCommand() == ControlNetworkMessageCommand.MATCHMAKING_QUIT)
			{
				if(client1.getClientId().compareTo(controlMsg.getSenderId()) == 0)
				{
					if(client2.clientConnected())
					{
						client2.addObserver(MatchMakingManager.getMatchMakingManager());
						if(!matchProposed)
						{
							client2.sendMessage(controlMsg);
						}
					}
					//client2.deleteObserver(this);
					client1.deleteObserver(this);
					client1.closeClient();
				}
				else if(client2.getClientId().compareTo(controlMsg.getSenderId()) == 0)
				{
					if(client1.clientConnected())
					{
						client1.addObserver(MatchMakingManager.getMatchMakingManager());
						if(!matchProposed)
						{
							client1.sendMessage(controlMsg);
						}
					}
					//client1.addObserver(MatchMakingManager.getMatchMakingManager());
					client2.deleteObserver(this);
					client2.closeClient();
				}
				MatchMakingManager.getMatchMakingManager().endMatch(this);
			}
			else if(controlMsg.getCommand() == ControlNetworkMessageCommand.MATCHMAKING_RESTARTING)
			{
				if(matchProposed)
				{
					matchStarted = false;
					client1Ready = false;
					client2Ready = false;
					client1.sendMessage(new ControlNetworkMessage(null, ControlNetworkMessageCommand.MATCHMAKING_FOUND, null));
					client2.sendMessage(new ControlNetworkMessage(null, ControlNetworkMessageCommand.MATCHMAKING_FOUND, null));
				}
				matchProposed = !matchProposed;
			}
			else
			{
				if(client1.getClientId().compareTo(msg.getSenderId()) == 0)
				{
					client2.sendMessage(msg);
				}
				else if(client2.getClientId().compareTo(msg.getSenderId()) == 0)
				{
					client1.sendMessage(msg);
				}
			}
			break;
		case CHAT_MESSAGE:
			ChatNetworkMessage chatMsg = (ChatNetworkMessage)arg1;
			if(client1.getClientId().compareTo(chatMsg.getSenderId()) == 0)
			{
				client2.sendMessage(chatMsg);
			}
			else if(client2.getClientId().compareTo(chatMsg.getSenderId()) == 0)
			{
				client1.sendMessage(chatMsg);
			}
			break;
		}
		if(client1Ready && client2Ready && !matchStarted)
		{
			matchStarted = true;
			Random rand = new Random();
			boolean client1Turn = rand.nextBoolean();
			if(client1Turn)
			{
				client1.sendMessage(new GameActionNetworkMessage(NetworkMessage.ServerSenderId, true, new Event(EventType.GAME_ACTION_PLAYER_TURN)));
			}
			else
			{
				client1.sendMessage(new GameActionNetworkMessage(NetworkMessage.ServerSenderId, true, new Event(EventType.GAME_ACTION_PLAYER_TURN)));
			}
		}
	}
}
