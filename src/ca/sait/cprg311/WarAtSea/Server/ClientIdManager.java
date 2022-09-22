package ca.sait.cprg311.WarAtSea.Server;

import java.util.ArrayList;
import java.util.UUID;

public class ClientIdManager
{
	public ArrayList<UUID> ids;
	private static ClientIdManager manager;
	
	private ClientIdManager()
	{
		ids = new ArrayList<UUID>();
		ids.add(new UUID(0,0));
		ids.add(new UUID(0,1));
	}
	
	public static synchronized ClientIdManager getClientIdManager()
	{
		if(manager == null)
		{
			manager = new ClientIdManager();
		}
		return manager;
	}
	
	public synchronized UUID generateId()
	{
		UUID id = UUID.randomUUID();
		while(ids.contains(id))
		{
			id = UUID.randomUUID();
		}
		ids.add(id);
		return id;
	}
}
