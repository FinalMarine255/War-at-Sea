package ca.sait.cprg311.WarAtSea.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.NetworkMessage;

public class Client
{
	private Socket client;
	private UUID clientId;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	public Client(Socket client, UUID clientId)
	{
		this.client = client;
		this.clientId = clientId;
		try {
			outputStream = new ObjectOutputStream(this.client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			inputStream = new ObjectInputStream(this.client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isConnected()
	{
		return !client.isClosed();
	}
	public UUID getId()
	{
		return clientId;
	}
	public ObjectInputStream getInputStream()
	{
		return inputStream;
	}
	public ObjectOutputStream getOutputStream()
	{
		return outputStream;
	}
	public void close()
	{
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
