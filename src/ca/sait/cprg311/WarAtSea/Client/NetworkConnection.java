package ca.sait.cprg311.WarAtSea.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import ca.sait.cprg311.WarAtSea.Client.Event.NetworkMessageRecievedEvent;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessageCommand;
import ca.sait.cprg311.WarAtSea.util.NetworkMessage;

public class NetworkConnection extends Observable implements Runnable
{
	private Socket connection;
	private String serverAddress;
	private int serverPort;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public NetworkConnection(String serverAddress, int serverPort)
	{
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	
	public void connect() throws UnknownHostException, IOException
	{
		connection = new Socket(this.serverAddress, this.serverPort);
		InputStream st = connection.getInputStream();
		//DataInputStream stream = new DataInputStream(st);
		input = new ObjectInputStream(connection.getInputStream());
		output = new ObjectOutputStream(connection.getOutputStream());
		//Must write a single object to the output stream, or the server may hang while trying to create an input stream.
		output.writeObject(new ControlNetworkMessage(NetworkMessage.VoidSenderId, ControlNetworkMessageCommand.CONNECTION_INIT, null));
	}
	public void disconnect() throws IOException
	{
		connection.close();
	}
	
	public synchronized void sendMessage(NetworkMessage msg)
	{
		try
		{
			output.writeObject(msg);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			//sendMessage(new NetworkMessage());
			NetworkMessage msg = null;
			try {
				msg = (NetworkMessage)input.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				msg = new ControlNetworkMessage(NetworkMessage.ServerSenderId, ControlNetworkMessageCommand.MATCHMAKING_QUIT, null);
			}
			catch(NullPointerException e)
			{
				msg = null;
			}
			if(msg != null)
			{
				setChanged();
				notifyObservers(new NetworkMessageRecievedEvent(msg));
			}
		}
	}
}
