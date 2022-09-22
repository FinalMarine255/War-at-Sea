package ca.sait.cprg311.WarAtSea.Server;

import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MatchMakingQueue
{
	private Queue<ClientHandler> matchingQueue;
	
	public MatchMakingQueue()
	{
		matchingQueue = new ConcurrentLinkedQueue<ClientHandler>();
	}
	
	public synchronized void addToQueue(ClientHandler client)
	{
		matchingQueue.add(client);
	}
	public synchronized ClientHandler getFromQueue()
	{
		return matchingQueue.remove();
	}
	public synchronized void removeFromQueue(ClientHandler client)
	{
		matchingQueue.remove(client);
	}
	public synchronized int getNumberInQueue()
	{
		return matchingQueue.size();
	}
}
