package ca.sait.cprg311.WarAtSea.Server;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessageCommand;
import ca.sait.cprg311.WarAtSea.util.NetworkMessage;

public class MatchMakingManager implements Runnable, Observer
{
	private static MatchMakingManager manager;
	private MatchMakingQueue matchingQueue;
	private ThreadGroup matchThreads;
	private ArrayList<Match> matches;
	private ServerGUI guiToReportTo;
	
	MatchMakingManager()
	{
		matchingQueue = new MatchMakingQueue();
		matchThreads = new ThreadGroup("Matches");
		matches = new ArrayList<Match>();
	}
	
	public synchronized void setGuiToReportTo(ServerGUI gui)
	{
		guiToReportTo = gui;
	}
	
	public static MatchMakingManager getMatchMakingManager()
	{
		if(manager == null)
		{
			manager = new MatchMakingManager();
		}
		return manager;
	}
	
	public synchronized void endMatch(Match match)
	{
		matches.remove(match);
		if(guiToReportTo != null)
		{
			guiToReportTo.setClientsConnected(matches.size() * 2 + matchingQueue.getNumberInQueue());
			guiToReportTo.setMatchesInProgress(matches.size());
		}
	}
	
	private synchronized void createNextMatch()
	{
		//should probably throw an exception here if two are not available;
		if(matchingQueue.getNumberInQueue() >= 2)
		{
			Match match = new Match(matchingQueue.getFromQueue(), matchingQueue.getFromQueue());
			matches.add(match);
			//Thread thread = new Thread(matchThreads, match);
			//thread.start();
		}
		else
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(guiToReportTo != null)
		{
			guiToReportTo.setClientsConnected(matches.size() * 2 + matchingQueue.getNumberInQueue());
			guiToReportTo.setMatchesInProgress(matches.size());
		}
	}
	
	public synchronized void addToMatchMakingQueue(ClientHandler client2)
	{
		matchingQueue.addToQueue(client2);
		if(matchingQueue.getNumberInQueue() >= 2)
		{
			notify();
		}
		if(guiToReportTo != null)
		{
			guiToReportTo.setClientsConnected(matches.size() * 2 + matchingQueue.getNumberInQueue());
			guiToReportTo.setMatchesInProgress(matches.size());
		}
	}
	public synchronized void endMatchMaking()
	{
		for(int i = 0; i < matches.size(); ++i)
		{
			matches.get(i).endMatch();
		}
		for(int i = 0; i < matchingQueue.getNumberInQueue(); ++i)
		{
			matchingQueue.getFromQueue().sendMessage(new ControlNetworkMessage(NetworkMessage.ServerSenderId, ControlNetworkMessageCommand.MATCHMAKING_TERMINATED, null));
		}
		if(guiToReportTo != null)
		{
			guiToReportTo.setClientsConnected(matches.size() * 2 + matchingQueue.getNumberInQueue());
			guiToReportTo.setMatchesInProgress(matches.size());
		}
	}
	
	public void run()
	{
		while(true)
		{
			createNextMatch();
		}
	}

	@Override
	public void update(Observable o, Object arg)
	{
		if(o instanceof ClientHandler)
		{
			if(arg instanceof ControlNetworkMessage)
			{
				ControlNetworkMessage msg = (ControlNetworkMessage)arg;
				if(msg.getCommand() == ControlNetworkMessageCommand.MATCHMAKING_QUIT)
				{
					//make sure that the client is removed from matchmaking before the connection is close
					matchingQueue.removeFromQueue((ClientHandler)o);
					((ClientHandler)o).closeClient();
					if(guiToReportTo != null)
					{
						guiToReportTo.setClientsConnected(matches.size() * 2 + matchingQueue.getNumberInQueue());
						guiToReportTo.setMatchesInProgress(matches.size());
					};
				}
				else if(msg.getCommand() == ControlNetworkMessageCommand.MATCHMAKING_RESTARTING)
				{
					addToMatchMakingQueue((ClientHandler)o);
				}
			}
		}
	}
}
