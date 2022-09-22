package ca.sait.cprg311.WarAtSea.Client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;

import ca.sait.cprg311.WarAtSea.Client.Event.ChatMessageRecievedEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.ChatMessageSendEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.MouseClickType;
import ca.sait.cprg311.WarAtSea.Client.Graphics.RenderTarget;
import ca.sait.cprg311.WarAtSea.Client.Graphics.Renderer;
import ca.sait.cprg311.WarAtSea.util.Event.EventHandler;

public class GameWindow extends Frame implements Runnable
{
    private RenderTarget targ;
    private Renderer rend;
    private GameState gameState;
    private TextArea chatLog;
	private EventHandler chatMessageSender;
	private TextField chatBox;
	private boolean chatModified;
    
    public GameWindow(GameState gameState, int width, int height)
    {

		//window = new Frame("Direct draw demo from tile");

    	
    	this.gameState = gameState;
    	
    	
    	targ = new RenderTarget();
    	targ.setBounds(0, 0, 384, height);
    	
    	
    	rend = new Renderer();
    	
    	
    	//setLayout(new BorderLayout());

    	this.setBounds(100, 100, width, height);
    	
    	Panel pan = new Panel();
    	add(pan);
    	pan.setLayout(null);
    	
    	pan.add(targ);
    	
	    chatLog = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
	    chatLog.setBounds(388, 10, 400, 650);
	    pan.add(chatLog);
	    chatLog.setVisible(true);
	    chatLog.setEnabled(false);
	    
	    chatBox = new TextField();
	    chatBox.setBounds(388, 660, 400, 50);
	    pan.add(chatBox);
	    chatBox.setVisible(true);
	    chatModified = false;
	    chatBox.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				if(!chatModified)
				{
					chatBox.setText("");
				}
				chatModified = true;
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				// TODO Auto-generated method stub
				if(chatBox.getText().equals("") || chatBox.getText().isEmpty())
				{
					chatModified = false;
	    			chatBox.setText("<<Enter a message>>");
				}
			}
	    	
	    });
	    
	    Button chatSendButton = new Button();
	    chatSendButton.setBounds(width - 225, 725, 50, 30);
	    chatSendButton.setLabel("Send");
	    pan.add(chatSendButton);
	    chatSendButton.setVisible(true);
	    chatSendButton.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		if(chatMessageSender != null)
	    		{
	    			//TODO
	    			chatMessageSender.handleEvent(new ChatMessageSendEvent(chatBox.getText()));
	    			chatLog.append("\n\nYou:: \n     " + chatBox.getText());
	    			chatBox.setText("<<Enter a message>>");
	    			chatModified = false;
	    		}
	    	}
	    });
	    
	    //add(chatLog);
	    
	    //targ.setSize(new Dimension(width, height));
	    
	    //add(targ);
	    //frame.pack();
	    setVisible(true);
	    setResizable(false);
	    addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent windowEvent){
	    		gameState.setRunning(false);
	    		try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		System.exit(0);
	    	}
	    });
	    addComponentListener(
	    	      new ComponentAdapter()
	    	      {
	    	        public void componentMoved( ComponentEvent ce )
	    	        {
	    	        	//TODO if this is not commented out, will cause the targ to flicker when moved.
	    	          //targ.repaint();
	    	        }
	    	      });
    }

    private void render()
    {
    	rend.renderSpriteLayerGroup(targ,  gameState.getSpriteLayerGroup());
    	//rend.renderSpriteLayer(targ, gameState.getGameGridSpriteHandler().getSriteLayer());
    }
    
    public RenderTarget getRenderTarget()
    {
    	return targ;
    }
    
    public void setChatMessageSenderEventHandler(EventHandler handler)
    {
    	this.chatMessageSender = handler;
    }
    public void appendChatMessage(ChatMessageRecievedEvent event)
    {
    	chatLog.append("\n\n" + event.getSenderName() + " :: \n     " + event.getMessage());
    }
    
	@Override
	public void run()
	{
		while(true)
		{
			render();
			Thread.yield();
		}
	}

}
