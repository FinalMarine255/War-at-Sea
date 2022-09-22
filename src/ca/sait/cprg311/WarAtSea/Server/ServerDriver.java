package ca.sait.cprg311.WarAtSea.Server;

import java.awt.EventQueue;

public class ServerDriver
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
					MatchMakingManager.getMatchMakingManager().setGuiToReportTo(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		NetworkHandler handler = new NetworkHandler(5555);
		
		Thread matchmakingThread = new Thread(MatchMakingManager.getMatchMakingManager(), "matchmaking thread");
		matchmakingThread.start();
		
		handler.open();
		//Thread t = new Thread(handler);
		//t.start();
		//try {
		//	t.join();
		//} catch (InterruptedException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		//handler.close();
		handler.run();
	}
}
