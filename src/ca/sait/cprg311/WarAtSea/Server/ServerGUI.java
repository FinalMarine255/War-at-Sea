package ca.sait.cprg311.WarAtSea.Server;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class ServerGUI extends JFrame {

	private JPanel contentPane;
	JLabel lblNumClientsConn;
	JLabel lblMatchesInProg;
	/**
	 * Create the frame.
	 */
	public ServerGUI() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 339, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNumberClientsConnected = new JLabel("Number clients connected:");
		lblNumberClientsConnected.setBounds(10, 11, 219, 14);
		contentPane.add(lblNumberClientsConnected);
		
		JLabel lblNumberMatchesIn = new JLabel("Number matches in progress: ");
		lblNumberMatchesIn.setBounds(10, 35, 219, 14);
		contentPane.add(lblNumberMatchesIn);
		
		lblNumClientsConn = new JLabel("");
		lblNumClientsConn.setBounds(247, 11, 46, 14);
		contentPane.add(lblNumClientsConn);
		
		lblMatchesInProg = new JLabel("");
		lblMatchesInProg.setBounds(247, 35, 46, 14);
		contentPane.add(lblMatchesInProg);
		
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				System.out.println("exit");
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				MatchMakingManager.getMatchMakingManager().endMatchMaking();
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	public synchronized void setClientsConnected(int num)
	{
		lblNumClientsConn.setText(new Integer(num).toString());
	}
	public synchronized void setMatchesInProgress(int num)
	{
		lblMatchesInProg.setText(new Integer(num).toString());
	}
}
