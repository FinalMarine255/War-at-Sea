package ca.sait.cprg311.WarAtSea.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import ca.sait.cprg311.WarAtSea.util.ChatNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessageCommand;
import ca.sait.cprg311.WarAtSea.util.NetworkMessage;
import ca.sait.cprg311.WarAtSea.util.NetworkMessageType;

public class NetworkHandler implements Runnable
{
	private ServerSocket socket;
	private int port;
	
	public NetworkHandler(int port)
	{
		this.port = port;
	}
	
	public void open()
	{
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void close()
	{
		try {
			socket.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				Socket client = socket.accept();
				//ObjectOutputStream oub = new ObjectOutputStream(client.getOutputStream());
				
				//A single message must be writen to the output stream, or the client will not be able to open an input stream.
				//oub.writeObject(new ControlNetworkMessage(NetworkMessage.ServerSenderId, ControlNetworkMessageCommand.CONNECTION_INIT, null));
				
				ClientHandler clientHandler = new ClientHandler(new Client(client, ClientIdManager.getClientIdManager().generateId()));
				clientHandler.addObserver(MatchMakingManager.getMatchMakingManager());
				Thread clientThread = new Thread(clientHandler, "Client Thread");
				clientThread.start();
				
				MatchMakingManager.getMatchMakingManager().addToMatchMakingQueue(clientHandler);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
