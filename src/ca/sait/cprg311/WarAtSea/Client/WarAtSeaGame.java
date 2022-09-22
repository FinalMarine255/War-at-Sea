package ca.sait.cprg311.WarAtSea.Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import ca.sait.cprg311.WarAtSea.Client.Graphics.ImageManager;
import ca.sait.cprg311.WarAtSea.Client.Graphics.Sprite;
import ca.sait.cprg311.WarAtSea.Client.Graphics.SpriteLayer;
import ca.sait.cprg311.WarAtSea.exceptions.ImageAlreadyLoadedException;
import ca.sait.cprg311.WarAtSea.exceptions.ImageNotLoadedException;
import ca.sait.cprg311.WarAtSea.util.ChatNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.ControlNetworkMessageCommand;
import ca.sait.cprg311.WarAtSea.util.GameActionNetworkMessage;
import ca.sait.cprg311.WarAtSea.util.Event.Event;
import ca.sait.cprg311.WarAtSea.util.Event.EventHandler;
import ca.sait.cprg311.WarAtSea.util.Event.EventType;
import ca.sait.cprg311.WarAtSea.Client.Event.ChatMessageRecievedEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.ChatMessageSendEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.GameActionRequestEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.GameActionResponseEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.GameStateChangeEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.GameStateParam;
import ca.sait.cprg311.WarAtSea.Client.Event.MouseClickEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.MouseClickType;
import ca.sait.cprg311.WarAtSea.Client.Event.ShipPlacementSpriteEventHandler;

public class WarAtSeaGame extends Game implements Observer{

	private GameWindow window;
	private NetworkHandler networkHandler;
	private String username;
	
	private Ship[] ships;
	private Ship selectedShip;
	private int selectedX;
	private int selectedY;
	private boolean placementValid;
	private ShipPlacementGrid placementGrid;
	private SpriteLayer uiOverlay;
	private SpriteLayer oponentTurnMsg;
	private SpriteLayer allyTurnMsg;
	private SpriteLayer waitingMsgLayer;
	private int intactShipSegments;
	private GameGridBlockType attackedType;
	private int attackedX;
	private int attackedY;
	private SpriteLayer missileLayer;
	private Missile allyMissile;
	private Missile enemyMissile;
	private SpriteLayer attackSelectLayer;
	private int lastHoveredGridX;
	private int lastHoveredGridY;
	private int gameSync;
	private Object lock;
	
	public WarAtSeaGame()
	{
		lock = new Object();
		gameSync = 0;
		//intactShipSegments = 17;
		intactShipSegments = 1;
		uiOverlay = new SpriteLayer();
		uiOverlay.setZ(3);
		uiOverlay.setVisible(true);
		oponentTurnMsg = new SpriteLayer();
		oponentTurnMsg.setZ(2);
		oponentTurnMsg.setVisible(false);
		allyTurnMsg = new SpriteLayer();
		allyTurnMsg.setZ(2);
		allyTurnMsg.setVisible(false);
		waitingMsgLayer = new SpriteLayer();
		waitingMsgLayer.setZ(2);
		waitingMsgLayer.setVisible(false);
		missileLayer = new SpriteLayer();
		missileLayer.setZ(2);
		missileLayer.setVisible(true);
		attackSelectLayer = new SpriteLayer();
		attackSelectLayer.setZ(1);
		attackSelectLayer.setVisible(true);
		
		ships = new Ship[5];
		selectedShip = null;
	}
	
	private void initGameGridForMatch()
	{
		//get a reference to the GameGrid object, to avoid destroying the call stack and timing.
		GameGrid grid = gameState.getGameGrid();
		GameGridSpriteHandler spriteHandler = gameState.getGameGridSpriteHandler();
		ShipPlacementSpriteEventHandler eventHandler = new ShipPlacementSpriteEventHandler();
		//set ally side to water
		for(int i = 0; i < 12; ++i)
		{
			for(int j = 0; j < 12; ++j)
			{
				grid.setTypeAt(GameGridBlockType.WATER, i, j);
				spriteHandler.getSpriteAt(i, j).setEventHandler(eventHandler);
			}
		}
		//set enemy side to water_fog
		for(int i = 0; i < 12; ++i)
		{
			for(int j = 13; j < 25; ++j)
			{
				grid.setTypeAt(GameGridBlockType.WATER_FOG, i, j);
				spriteHandler.getSpriteAt(i, j).setEventHandler(eventHandler);
			}
		}
		
		//set ally ships to default positions.
		ships[0] = new Ship(0, 0, ShipOrientation.HORIZONTAL, ShipType.AIRCRAFT_CARRIER, 1);
		ships[1] = new Ship(0, 2, ShipOrientation.VERTICAL, ShipType.BATTLESHIP, 2);
		ships[2] = new Ship(5, 3, ShipOrientation.HORIZONTAL, ShipType.CRUISER, 3);
		ships[3] = new Ship(2, 6, ShipOrientation.VERTICAL, ShipType.DESTROYER, 4);
		ships[4] = new Ship(9, 9, ShipOrientation.HORIZONTAL, ShipType.SUBMARINE, 5);
		placementGrid.placeShip(ships[0]);
		placementGrid.placeShip(ships[1]);
		placementGrid.placeShip(ships[2]);
		placementGrid.placeShip(ships[3]);
		placementGrid.placeShip(ships[4]);
	}
	
	@Override
	public void init()
	{
		
		super.imageManager = ImageManager.getImageManager();

		try
		{
			imageManager.loadFromFile("res/imgInfo.txt", "res/imgs/");
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		} 
		catch (ImageAlreadyLoadedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO
		//String serverIp = JOptionPane.showInputDialog("Please enter the server's IP address");
		//username = JOptionPane.showInputDialog("Please enter the username you wish to use");
		String serverIp = "localhost";
		username = "test";

		super.gameState = new GameState(12, 25, false, this);
		
		window = new GameWindow(gameState, 800, 800);
	    try
	    {
			networkHandler = new NetworkHandler(serverIp, 5555);
	    }
	    catch (UnknownHostException e)
	    {
			JOptionPane.showMessageDialog(null, "Could not connect to the server, closing");
			System.exit(0);
		}
	    catch (IOException e)
	    {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not connect to the server, closing");
			System.exit(0);
		}
	    networkHandler.addObserver(this);
	    
	    window.setChatMessageSenderEventHandler(new EventHandler(){

			@Override
			public void handleEvent(Event event)
			{
				if(event.getEventType() == EventType.CHAT_MESSAGE_SEND)
				{
					ChatMessageSendEvent chatMessageSendEvent = (ChatMessageSendEvent)event;
					networkHandler.sendMessage(new ChatNetworkMessage(null, username,chatMessageSendEvent.getMessage()));
				}
			}
	    	
	    });

		
		
	    gameState.getGameGrid().addObserver(gameState.getGameGridSpriteHandler());
	    //gameState.getGameGrid().setAllTo(GameGridBlockType.WATER);
	    
	    Thread windowThread = new Thread(window, "GUI Thead");
	    windowThread.start();
	    
	    gameState.setMatchmakingStatus(MatchmakingStatus.SEARCHING);

	    gameState.getSpriteLayerGroup().addSpriteLayer(uiOverlay);
	    gameState.getSpriteLayerGroup().addSpriteLayer(missileLayer);
	    gameState.getSpriteLayerGroup().addSpriteLayer(oponentTurnMsg);
	    gameState.getSpriteLayerGroup().addSpriteLayer(allyTurnMsg);
	    gameState.getSpriteLayerGroup().addSpriteLayer(waitingMsgLayer);
	    gameState.getSpriteLayerGroup().addSpriteLayer(attackSelectLayer);

	    Sprite oponentTurn = null;
	    try 
	    {
	    	oponentTurn = ImageManager.getImageManager().createSpriteFromLoadedImage("OPONENT_TURN");
		} 
	    catch (ImageNotLoadedException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    oponentTurn.setX(32);
	    oponentTurn.setY(384);
	    oponentTurnMsg.addSprite(oponentTurn);
	    Sprite allyTurn = null;
	    try 
	    {
	    	allyTurn = ImageManager.getImageManager().createSpriteFromLoadedImage("ALLY_TURN");
		} 
	    catch (ImageNotLoadedException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    allyTurn.setX(32);
	    allyTurn.setY(384);
	    allyTurnMsg.addSprite(allyTurn);
	    
	    Sprite waitingMsg = null;
	    try 
	    {
	    	waitingMsg = ImageManager.getImageManager().createSpriteFromLoadedImage("WAITING");
		} 
	    catch (ImageNotLoadedException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    waitingMsg.setX(32);
	    waitingMsg.setY(384);
	    waitingMsgLayer.addSprite(waitingMsg);
	}

	@Override
	public void play()
	{
		//start of new match.
	    while(gameState.isRunning())
	    {
	    	lastHoveredGridX = 0;
	    	lastHoveredGridY = 0;
	    	gameState.reset();
		    gameState.getGameGrid().setAllTo(GameGridBlockType.WATER);
		    uiOverlay.clearSprites();
		    missileLayer.clearSprites();
		    attackSelectLayer.clearSprites();
		    oponentTurnMsg.setVisible(false);
		    allyTurnMsg.setVisible(false);
		    waitingMsgLayer.setVisible(false);
		    //oponentTurnMsg.clearSprites();
		    //allyTurnMsg.clearSprites();
	    	
	    	//gameState.setMatchmakingStatus(MatchmakingStatus.IN_MATCH);
	    	
		    window.repaint();
		    
	    	Sprite searching = null;
		    try 
		    {
				searching = ImageManager.getImageManager().createSpriteFromLoadedImage("SEARCHING");
			} 
		    catch (ImageNotLoadedException e) 
		    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    searching.setX(32);
		    searching.setY(384);
		    uiOverlay.addSprite(searching);
		    Timer spriteTimer = new Timer(1000, new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
			    	gameState.getSpriteLayerGroup().updateAnimated();
				}
		    	
		    });
		    spriteTimer.start();
	    	
	    	while(gameState.getMatchmakingStatus() == MatchmakingStatus.SEARCHING)
	    	{
	    		try 
	    		{
					Thread.sleep(0);
				} 
	    		catch (InterruptedException e1) 
	    		{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	}
	    	uiOverlay.removeSprite(searching);
	    	if(gameState.getMatchmakingStatus() == MatchmakingStatus.SERVER_ENDED_MATCH)
	    	{
	    		break;
	    	}
	    	else if(gameState.getMatchmakingStatus() == MatchmakingStatus.OPONENT_LEFT)
	    	{
	    		int findNewMatch = JOptionPane.showConfirmDialog(null, "The enemy has retreated, would you like to find a new match", "VITAL INTEL", JOptionPane.YES_NO_OPTION);
	    		if(findNewMatch == JOptionPane.YES_OPTION)
	    		{
	    			networkHandler.sendMessage(new ControlNetworkMessage(null, ControlNetworkMessageCommand.MATCHMAKING_RESTARTING, null));
	    			continue;
	    		}
	    		else
	    		{
	    			break;
	    		}
	    	}
	    	
		    placementGrid = new ShipPlacementGrid(12, 12);
		    
		    gameState.getSpriteLayerGroup().addSpriteLayer(placementGrid.getSpriteLayer());
		    placementGrid.getSpriteLayer().setZ(1);
	
		    initGameGridForMatch();
		    
		    MouseListener shipPlacementMouseListener = new MouseListener(){
	
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if(gameState.getGameboardSetup())
					{
						if(selectedShip == null)
						{
	
							Sprite sp = uiOverlay.getSpriteContainingPoint(arg0.getX(), arg0.getY());
							if(sp != null)
							{
								sp.onClick(arg0.getX(), arg0.getY(), MouseClickType.LEFT_CLICK);
							}
							else
							{
							
								int shipCode = 0;
								int x = arg0.getX() / 32;
								int y = arg0.getY() / 32;
								if(!(x < 0 || x >= 12 || y < 0 || y >= 12))
								{
									try
									{
										shipCode = placementGrid.getGridPosition(arg0.getX() / 32, arg0.getY() / 32);
										for(int i = 0; i < ships.length; ++i)
										{
											if(ships[i].getShipCode() == shipCode)
											{
												selectedShip = ships[i];
												break;
											}
										}
										selectedX = arg0.getX() / 32;
										selectedY = arg0.getY() / 32;
									}
									catch(ArrayIndexOutOfBoundsException e)
									{
									}
								}
							}
						}
						else
						{
							if(placementValid)
							{
								selectedShip = null;
							}
						}
					}
					else
					{
						gameState.getGameGridSpriteHandler().getSpriteAt(arg0.getX() / 32, arg0.getY() / 32).onClick(arg0.getX(), arg0.getY(), MouseClickType.LEFT_CLICK);
					}
				}
	
				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
	    		
	    	};
		    MouseWheelListener shipPlacementMouseWheelListener = new MouseWheelListener(){
	
				@Override
				public void mouseWheelMoved(MouseWheelEvent arg0)
				{
					if(gameState.getGameboardSetup())
					{
						
						if(selectedShip != null)
						{
							//needed to solve an error where the rapid movement of one ship can erase another from the grid.
							for(int i = 0; i < ships.length; ++i)
							{
								if(ships[i] != selectedShip)
								{
									placementGrid.removeShip(ships[i]);
									placementGrid.placeShip(ships[i]);
								}
							}
							int x = arg0.getX() / 32;
							int y = arg0.getY() / 32;
							//if(x != selectedX || y != selectedY)
							//{
								placementGrid.removeShip(selectedShip);
								int gridX = selectedShip.getX() - (x - selectedX);
								int gridY = selectedShip.getY() - (y - selectedY);
								if(selectedShip.getOrientation() == ShipOrientation.HORIZONTAL)
								{
									selectedShip.setOrientation(ShipOrientation.VERTICAL);
								}
								else if(selectedShip.getOrientation() == ShipOrientation.VERTICAL)
								{
									selectedShip.setOrientation(ShipOrientation.HORIZONTAL);
								}
								selectedShip.setX(gridX);
								selectedShip.setY(gridY);
								selectedX = x;
								selectedY = y;
								placementValid = placementGrid.placeShip(selectedShip);
							//}
						}
					}
				}
	    	};
		    MouseMotionListener shipPlacementMouseMotionListener = new MouseMotionListener(){
	
				@Override
				public void mouseDragged(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void mouseMoved(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if(selectedShip != null)
					{
						//needed to solve an error where the rapid movement of one ship can erase another from the grid.
						for(int i = 0; i < ships.length; ++i)
						{
							if(ships[i] != selectedShip)
							{
								placementGrid.removeShip(ships[i]);
								placementGrid.placeShip(ships[i]);
							}
						}
						int x = arg0.getX() / 32;
						int y = arg0.getY() / 32;
						if(x != selectedX || y != selectedY)
						{
							placementGrid.removeShip(selectedShip);
							int gridX = selectedShip.getX() + (x - selectedX);
							int gridY = selectedShip.getY() + (y - selectedY);
							selectedShip.setX(gridX);
							selectedShip.setY(gridY);
							selectedX = x;
							selectedY = y;
							placementValid = placementGrid.placeShip(selectedShip);
						}
					}
				}
		    	
		    };
	    	window.getRenderTarget().addMouseListener(shipPlacementMouseListener);
		    window.getRenderTarget().addMouseWheelListener(shipPlacementMouseWheelListener);
		    window.getRenderTarget().addMouseMotionListener(shipPlacementMouseMotionListener);
		    
		    Sprite btnReady = null;
		    try
		    {
				btnReady = ImageManager.getImageManager().createSpriteFromLoadedImage("BTN_READY");
			} 
		    catch (ImageNotLoadedException e) 
		    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    btnReady.setX(142);
		    btnReady.setY(372);
		    btnReady.setEventHandler(new EventHandler(){
	
				@Override
				public void handleEvent(Event event)
				{
					if(event.getEventType() == EventType.MOUSE_CLICK)
					{
						MouseClickEvent clickEvent = (MouseClickEvent)event;
						gameState.setGameboardSetup(false);
						//send the ready notification to the opponent.
						networkHandler.sendMessage(new GameActionNetworkMessage(null, false, new Event(EventType.GAME_ACTION_PLAYER_READY)));
						
						for(int i = 0; i < ships.length; ++i)
						{
							gameState.getGameGrid().placeShip(ships[i]);
						}
					}
				}
		    
		    });
		    
		    uiOverlay.addSprite(btnReady);
		    
		    while(gameState.getGameboardSetup() && gameState.getMatchmakingStatus() == MatchmakingStatus.IN_MATCH)
		    {
		    	//needed or the thread will never be run.
		    	//wonder if that is a glitch in java, or by design??
		    	try 
		    	{
					Thread.sleep(0);
				} 
		    	catch (InterruptedException e)
		    	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    gameState.getSpriteLayerGroup().removeSpriteLayer(placementGrid.getSpriteLayer());
		    uiOverlay.removeSprite(btnReady);
	    	if(gameState.getMatchmakingStatus() == MatchmakingStatus.SERVER_ENDED_MATCH)
	    	{
	    		break;
	    	}
	    	else if(gameState.getMatchmakingStatus() == MatchmakingStatus.OPONENT_LEFT)
	    	{
	    		int findNewMatch = JOptionPane.showConfirmDialog(null, "The enemy has retreated, would you like to find a new match", "VITAL INTEL", JOptionPane.YES_NO_OPTION);
	    		if(findNewMatch == JOptionPane.YES_OPTION)
	    		{
	    			networkHandler.sendMessage(new ControlNetworkMessage(null, ControlNetworkMessageCommand.MATCHMAKING_RESTARTING, null));
	    			continue;
	    		}
	    		else
	    		{
	    			break;
	    		}
	    	}
	
		    
		    //make sure that there are not more events being passed to the button.
		    btnReady.setEventHandler(null);
		    btnReady = null;
		    
		    waitingMsgLayer.setVisible(true);
		    
		    //TODO diplay waiting for oponent message
		    while(!gameState.getOponentReady() && gameState.getMatchmakingStatus() == MatchmakingStatus.IN_MATCH)
		    {
		    	try
		    	{
					Thread.sleep(0);
				} 
		    	catch (InterruptedException e)
		    	{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }

		    waitingMsgLayer.setVisible(false);
		    if(gameState.getMatchmakingStatus() == MatchmakingStatus.SERVER_ENDED_MATCH)
	    	{
	    		break;
	    	}
	    	else if(gameState.getMatchmakingStatus() == MatchmakingStatus.OPONENT_LEFT)
	    	{
	    		int findNewMatch = JOptionPane.showConfirmDialog(null, "The enemy has retreated, would you like to find a new match", "VITAL INTEL", JOptionPane.YES_NO_OPTION);
	    		if(findNewMatch == JOptionPane.YES_OPTION)
	    		{
	    			networkHandler.sendMessage(new ControlNetworkMessage(null, ControlNetworkMessageCommand.MATCHMAKING_RESTARTING, null));
	    			continue;
	    		}
	    		else
	    		{
	    			break;
	    		}
	    	}

		    //TODO oponent is ready;
		    
		    window.getRenderTarget().removeMouseListener(shipPlacementMouseListener);
		    window.getRenderTarget().removeMouseWheelListener(shipPlacementMouseWheelListener);
		    window.getRenderTarget().removeMouseMotionListener(shipPlacementMouseMotionListener);
		   

			Timer missileTimer = new Timer(100, new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
			    	if(allyMissile != null)
			    	{
			    		allyMissile.update();
			    	}
			    	if(enemyMissile != null)
			    	{
			    		enemyMissile.update();
			    	}
				}
				
			});
			missileTimer.start();
		    
		    MouseListener attackMouseListener = new MouseListener()
		    {
	
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if(gameState.isAllyTurn())
					{
						int x = arg0.getX() / 32;
						int y = arg0.getY() / 32;
						if(x >= 0 && x < 12 && y >= 13 && y < 25)
						{
							GameGridBlockType type = gameState.getGameGrid().getTypeAt(x, y);
							if(type == GameGridBlockType.WATER_FOG)
							{
								gameState.setAllyTurn(false);
								networkHandler.sendMessage(new GameActionNetworkMessage(null, false, new GameActionRequestEvent(x, y - 13)));
								attackedX = x;
								attackedY = y;
								allyMissile = new Missile(x, -1, attackedX, attackedY, 1);
								allyMissile.addObserver(gameState.getGame());
								missileLayer.addSprite(allyMissile.getSprite());
								missileLayer.setVisible(true);
								attackSelectLayer.setVisible(false);
							}
						}
					}
				}
	
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
		    	
		    };
		    MouseMotionListener attackMouseMostionListener = new MouseMotionListener(){

				@Override
				public void mouseDragged(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseMoved(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if(gameState.isAllyTurn())
					{
						int x = arg0.getX() / 32;
						int y = arg0.getY() / 32;
						if(x != lastHoveredGridX || y != lastHoveredGridY)
						{
							attackSelectLayer.clearSprites();
							lastHoveredGridX = x;
							lastHoveredGridY = y;
							Sprite invalidPlace = null;
							try {
								invalidPlace = ImageManager.getImageManager().createSpriteFromLoadedImage("INVALID_ATTACK");
							} catch (ImageNotLoadedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(x >= 0 && x < 12 && y >= 13 && y < 25)
							{
								GameGridBlockType type = gameState.getGameGrid().getTypeAt(x, y);
								if(type == GameGridBlockType.WATER_FOG)
								{
									Sprite validPlace = null;
									try {
										validPlace = ImageManager.getImageManager().createSpriteFromLoadedImage("VALID_ATTACK");
									} catch (ImageNotLoadedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									validPlace.setX(x * 32);
									validPlace.setY(y * 32);
									attackSelectLayer.addSprite(validPlace);
								}
								else
								{
									invalidPlace.setX(x * 32);
									invalidPlace.setY(y * 32);
									attackSelectLayer.addSprite(invalidPlace);
								}
							}
							else if(x >= 0 && x < 12 && y >= 0 && y < 13)
							{
								invalidPlace.setX(x * 32);
								invalidPlace.setY(y * 32);
								attackSelectLayer.addSprite(invalidPlace);
							}
						}
					}
				}
		    	
		    };
		    
		    window.getRenderTarget().addMouseListener(attackMouseListener);
		    window.getRenderTarget().addMouseMotionListener(attackMouseMostionListener);
		    
		    if(!allyTurnMsg.isVisible())
		    {
		    	oponentTurnMsg.setVisible(true);
		    }
		    //TODO 
		    while(gameState.getMatchmakingStatus() == MatchmakingStatus.IN_MATCH)
		    {
		    	
		    	try 
		    	{
					Thread.sleep(0);
				} 
		    	catch (InterruptedException e2)
		    	{
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		    	
		    	
		    }
			if(gameState.getMatchmakingStatus() == MatchmakingStatus.SERVER_ENDED_MATCH)
	    	{
	    		break;
	    	}

		    
		    //TODO
			if(gameState.getMatchmakingStatus() == MatchmakingStatus.MATCH_OVER)
			{
			    if(gameState.getAllyVictory())
			    {
					int findNewMatch = JOptionPane.showConfirmDialog(null, "You have won!\n\nWould you like to play another match?", "You Win", JOptionPane.YES_NO_OPTION);
		    		if(findNewMatch == JOptionPane.YES_OPTION)
		    		{
		    			networkHandler.sendMessage(new ControlNetworkMessage(null, ControlNetworkMessageCommand.MATCHMAKING_RESTARTING, null));
		    			continue;
		    		}
		    		else
		    		{
		    			break;
		    		}
			    }
			    else
			    {
					int findNewMatch = JOptionPane.showConfirmDialog(null, "You have lost!\n\nWould you like to play another match to try and redeem your honor?", "You Lose", JOptionPane.YES_NO_OPTION);
		    		if(findNewMatch == JOptionPane.YES_OPTION)
		    		{
		    			networkHandler.sendMessage(new ControlNetworkMessage(null, ControlNetworkMessageCommand.MATCHMAKING_RESTARTING, null));
		    			continue;
		    		}
		    		else
		    		{
		    			break;
		    		}
			    }
			}
	    }
		if(gameState.getMatchmakingStatus() == MatchmakingStatus.SERVER_ENDED_MATCH)
    	{
    		JOptionPane.showMessageDialog(null, "The connection to the server has been lost, please try again later.  \nWar at Sea will now close.");
    		System.exit(0);
    	}
    	else if(gameState.getMatchmakingStatus() == MatchmakingStatus.OPONENT_LEFT)
    	{
    		System.exit(0);
    	}
    	else if(gameState.getMatchmakingStatus() == MatchmakingStatus.MATCH_OVER)
    	{
    		System.exit(0);
    	}
	}

	@Override
	public void clean()
	{
		// TODO Auto-generated method stub
	}
	private GameGridBlockType getDestroyedBlockTypeAt(int x, int y)
	{
		GameGrid grid = gameState.getGameGrid();
		GameGridBlockType type = grid.getTypeAt(x,  y);
		GameGridBlockType retType = GameGridBlockType.WATER;
		switch(type)
		{
		case AIRCRAFT_CARRIER_SEC_1:
			retType = GameGridBlockType.DESTROYED_AIRCRAFT_CARRIER_SEC_1;
			break;
		case AIRCRAFT_CARRIER_SEC_2:
			retType = GameGridBlockType.DESTROYED_AIRCRAFT_CARRIER_SEC_2;
			break;
		case AIRCRAFT_CARRIER_SEC_3:
			retType = GameGridBlockType.DESTROYED_AIRCRAFT_CARRIER_SEC_3;
			break;
		case AIRCRAFT_CARRIER_SEC_4:
			retType = GameGridBlockType.DESTROYED_AIRCRAFT_CARRIER_SEC_4;
			break;
		case AIRCRAFT_CARRIER_SEC_5:
			retType = GameGridBlockType.DESTROYED_AIRCRAFT_CARRIER_SEC_5;
			break;
		case BATTLESHIP_SEC_1:
			retType = GameGridBlockType.DESTROYED_BATTLESHIP_SEC_1;
			break;
		case BATTLESHIP_SEC_2:
			retType = GameGridBlockType.DESTROYED_BATTLESHIP_SEC_2;
			break;
		case BATTLESHIP_SEC_3:
			retType = GameGridBlockType.DESTROYED_BATTLESHIP_SEC_3;
			break;
		case BATTLESHIP_SEC_4:
			retType = GameGridBlockType.DESTROYED_BATTLESHIP_SEC_4;
			break;
		case CRUISER_SEC_1:
			retType = GameGridBlockType.DESTROYED_CRUISER_SEC_1;
			break;
		case CRUISER_SEC_2:
			retType = GameGridBlockType.DESTROYED_CRUISER_SEC_2;
			break;
		case CRUISER_SEC_3:
			retType = GameGridBlockType.DESTROYED_CRUISER_SEC_3;
			break;
		case DESTROYER_SEC_1:
			retType = GameGridBlockType.DESTROYED_DESTROYER_SEC_1;
			break;
		case DESTROYER_SEC_2:
			retType = GameGridBlockType.DESTROYED_DESTROYER_SEC_2;
			break;
		case SUBMARINE_SEC_1:
			retType = GameGridBlockType.DESTROYED_SUBMARINE_SEC_1;
			break;
		case SUBMARINE_SEC_2:
			retType = GameGridBlockType.DESTROYED_SUBMARINE_SEC_2;
			break;
		case SUBMARINE_SEC_3:
			retType = GameGridBlockType.DESTROYED_SUBMARINE_SEC_3;
			break;
		case WATER:
			break;
		}
		return retType;
	}
	
	private void enemyFireGridUpdate(int x, int y)
	{
		GameGrid grid = gameState.getGameGrid();
		GameGridBlockType type = grid.getTypeAt(x,  y);
		switch(type)
		{
		case AIRCRAFT_CARRIER_SEC_1:
			grid.setTypeAt(GameGridBlockType.DESTROYED_AIRCRAFT_CARRIER_SEC_1, x, y);
			--intactShipSegments;
			break;
		case AIRCRAFT_CARRIER_SEC_2:
			grid.setTypeAt(GameGridBlockType.DESTROYED_AIRCRAFT_CARRIER_SEC_2, x, y);
			--intactShipSegments;
			break;
		case AIRCRAFT_CARRIER_SEC_3:
			grid.setTypeAt(GameGridBlockType.DESTROYED_AIRCRAFT_CARRIER_SEC_3, x, y);
			--intactShipSegments;
			break;
		case AIRCRAFT_CARRIER_SEC_4:
			grid.setTypeAt(GameGridBlockType.DESTROYED_AIRCRAFT_CARRIER_SEC_4, x, y);
			--intactShipSegments;
			break;
		case AIRCRAFT_CARRIER_SEC_5:
			grid.setTypeAt(GameGridBlockType.DESTROYED_AIRCRAFT_CARRIER_SEC_5, x, y);
			--intactShipSegments;
			break;
		case BATTLESHIP_SEC_1:
			grid.setTypeAt(GameGridBlockType.DESTROYED_BATTLESHIP_SEC_1, x, y);
			--intactShipSegments;
			break;
		case BATTLESHIP_SEC_2:
			grid.setTypeAt(GameGridBlockType.DESTROYED_BATTLESHIP_SEC_2, x, y);
			--intactShipSegments;
			break;
		case BATTLESHIP_SEC_3:
			grid.setTypeAt(GameGridBlockType.DESTROYED_BATTLESHIP_SEC_3, x, y);
			--intactShipSegments;
			break;
		case BATTLESHIP_SEC_4:
			grid.setTypeAt(GameGridBlockType.DESTROYED_BATTLESHIP_SEC_4, x, y);
			--intactShipSegments;
			break;
		case CRUISER_SEC_1:
			grid.setTypeAt(GameGridBlockType.DESTROYED_CRUISER_SEC_1, x, y);
			--intactShipSegments;
			break;
		case CRUISER_SEC_2:
			grid.setTypeAt(GameGridBlockType.DESTROYED_CRUISER_SEC_2, x, y);
			--intactShipSegments;
			break;
		case CRUISER_SEC_3:
			grid.setTypeAt(GameGridBlockType.DESTROYED_CRUISER_SEC_3, x, y);
			--intactShipSegments;
			break;
		case DESTROYER_SEC_1:
			grid.setTypeAt(GameGridBlockType.DESTROYED_DESTROYER_SEC_1, x, y);
			--intactShipSegments;
			break;
		case DESTROYER_SEC_2:
			grid.setTypeAt(GameGridBlockType.DESTROYED_DESTROYER_SEC_2, x, y);
			--intactShipSegments;
			break;
		case SUBMARINE_SEC_1:
			grid.setTypeAt(GameGridBlockType.DESTROYED_SUBMARINE_SEC_1, x, y);
			--intactShipSegments;
			break;
		case SUBMARINE_SEC_2:
			grid.setTypeAt(GameGridBlockType.DESTROYED_SUBMARINE_SEC_2, x, y);
			--intactShipSegments;
			break;
		case SUBMARINE_SEC_3:
			grid.setTypeAt(GameGridBlockType.DESTROYED_SUBMARINE_SEC_3, x, y);
			--intactShipSegments;
			break;
		case WATER:
			grid.setTypeAt(GameGridBlockType.WATER_MISSED, x, y);
			break;
		}
	}
	
	@Override
	public void update(Observable o, Object arg)
	{
		// TODO Auto-generated method stub
		Event event = (Event)arg;
		switch(event.getEventType())
		{
		case MISSILE_TARGET_REACHED:
			if(((Missile)o).equals(allyMissile))
			{
				while(attackedType == null)
				{
				}
				gameState.getGameGrid().setTypeAt(attackedType, attackedX, attackedY);
				//missileLayer.setVisible(false);
				if(missileLayer.getNumberSprites() == 1)
				{
					missileLayer.clearSprites();
				}
				else
				{
					missileLayer.removeSprite(((Missile)o).getSprite());
				}
				////
				allyMissile.deleteObserver(gameState.getGame());
				allyMissile = null;
				attackedType = null;
				oponentTurnMsg.setVisible(true);
				allyTurnMsg.setVisible(false);
				synchronized(lock)
				{
					gameSync++;
					if(gameSync == 2)
					{
						
						gameSync = 0;
						networkHandler.sendMessage(new GameActionNetworkMessage(null, false, new GameStateChangeEvent(GameStateParam.PLAYER_TURN, true)));
					}
				}
			}
			else
			{
				enemyFireGridUpdate(enemyMissile.getX(), enemyMissile.getY());
				//missileLayer.setVisible(false);
				if(missileLayer.getNumberSprites() == 1)
				{
					missileLayer.clearSprites();
				}
				else
				{
					missileLayer.removeSprite(((Missile)o).getSprite());
				}
				////
				enemyMissile.deleteObserver(gameState.getGame());
				enemyMissile = null;
				attackedType = null;
				//gameState.setAllyTurn(true);
				//oponentTurnMsg.setVisible(false);
				//allyTurnMsg.setVisible(true);
				networkHandler.sendMessage(new ControlNetworkMessage(null, ControlNetworkMessageCommand.GAME_SYNC, null));
			}
			break;
		case GAME_ACTION_REQUEST:
			GameActionRequestEvent gameActionRequest = (GameActionRequestEvent)event;
			GameGridBlockType type = getDestroyedBlockTypeAt(gameActionRequest.getX(), gameActionRequest.getY());
			
			networkHandler.sendMessage(new GameActionNetworkMessage(null, false, new GameActionResponseEvent(gameActionRequest.getX(), gameActionRequest.getY(), type)));
			//gameState.setAllyTurn(true);
			enemyMissile = new Missile(gameActionRequest.getX(), 12, gameActionRequest.getX(), gameActionRequest.getY(), -1);
			enemyMissile.addObserver(gameState.getGame());
			missileLayer.addSprite(enemyMissile.getSprite());

			break;
		case CHAT_MESSAGE_RECIEVED:
			
			window.appendChatMessage((ChatMessageRecievedEvent)event);
			
			break;
		case GAME_ACTION_RESPONSE:
			GameActionResponseEvent gameActionResponse = (GameActionResponseEvent)event;
			if(gameActionResponse.getBlockType() == GameGridBlockType.INVALID && gameActionResponse.getX() == -1 && gameActionResponse.getY() == -1)
			{
					gameState.setAllyVictory(true);
					gameState.setMatchmakingStatus(MatchmakingStatus.MATCH_OVER);
			}
			else
			{
				//gameState.getGameGrid().setTypeAt(gameActionResponse.getBlockType(), gameActionResponse.getX(), gameActionResponse.getY() + 13);
				attackedType = gameActionResponse.getBlockType();
			}
			break;
		case STATE_CHANGE:
			GameStateChangeEvent stateChangeEvent = (GameStateChangeEvent)event;
			switch(stateChangeEvent.getParam())
			{
			case OPONENT_READY:
				gameState.setOponentReady(true);
				//JOptionPane.showMessageDialog(null, "opponent ready");
				break;
			case MATCHMAKING_STATUS:
				//TODO
				gameState.setMatchmakingStatus((MatchmakingStatus)stateChangeEvent.getValue());
				break;
			case PLAYER_TURN:
		    	if(intactShipSegments <= 0)
		    	{
		    		networkHandler.sendMessage(new GameActionNetworkMessage(null, false, new GameActionResponseEvent(-1, -1, GameGridBlockType.INVALID)));
	
					gameState.setAllyVictory(false);
					gameState.setMatchmakingStatus(MatchmakingStatus.MATCH_OVER);
		    	}
		    	else
		    	{
					gameState.setAllyTurn(true);
					oponentTurnMsg.setVisible(false);
					allyTurnMsg.setVisible(true);
					attackSelectLayer.setVisible(true);
		    	}
				break;
			}
			break;
		case GAME_SYNC:
			synchronized(lock)
			{
				gameSync++;
				if(gameSync == 2)
				{
					
					gameSync = 0;
					networkHandler.sendMessage(new GameActionNetworkMessage(null, false, new GameStateChangeEvent(GameStateParam.PLAYER_TURN, true)));
				}
			}
			break;
		default:
			break;
		}
	}
}
