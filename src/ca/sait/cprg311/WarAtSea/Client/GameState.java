package ca.sait.cprg311.WarAtSea.Client;

import java.awt.Frame;

import ca.sait.cprg311.WarAtSea.Client.Graphics.SpriteLayerGroup;

public class GameState
{
	private GameGrid playField;
	private GameGridSpriteHandler gridSpriteHandler;
	private SpriteLayerGroup spriteLayers;
	private boolean allyTurn;
	private boolean gameboardSetup;
	private boolean running;
	private boolean oponentReady;
	private boolean allyVictory;
	private MatchmakingStatus matchmakingStatus;
	private WarAtSeaGame game;
	
	public GameState(int playFieldWidth, int playFieldHeight, boolean allyTurn, WarAtSeaGame game)
	{
		this.playField = new GameGrid(playFieldWidth, playFieldHeight);
		this.gridSpriteHandler = new GameGridSpriteHandler(playFieldWidth, playFieldHeight);
		//this.playField.addObserver(gridSpriteHandler);
		this.allyTurn = allyTurn;
		this.running = true;
		spriteLayers = new SpriteLayerGroup();
		spriteLayers.addSpriteLayer(gridSpriteHandler.getSriteLayer());
		gameboardSetup = true;
		oponentReady = false;
		allyVictory = false;
		matchmakingStatus = MatchmakingStatus.SEARCHING;
		this.game = game;
	}
	
	public void reset()
	{
		//this.playField = new GameGrid(playFieldWidth, playFieldHeight);
		//this.gridSpriteHandler = new GameGridSpriteHandler(playFieldWidth, playFieldHeight);
		//this.playField.addObserver(gridSpriteHandler);
		this.allyTurn = false;
		this.running = true;
		//spriteLayers = new SpriteLayerGroup();
		//spriteLayers.addSpriteLayer(gridSpriteHandler.getSriteLayer());
		//synchronized(spriteLayers)
		//{
		//	for(int i = 0; i < spriteLayers.getNumberLayers(); ++i)
		//	{
		//		spriteLayers.getSpriteLayer(0).clearSprites();
		//	}
		//}
		gameboardSetup = true;
		oponentReady = false;
		allyVictory = false;
		matchmakingStatus = MatchmakingStatus.SEARCHING;
	}
	
	public boolean isAllyTurn()
	{
		return allyTurn;
	}
	public boolean isRunning()
	{
		return running;
	}
	public GameGrid getGameGrid()
	{
		return playField;
	}
	public GameGridSpriteHandler getGameGridSpriteHandler()
	{
		return gridSpriteHandler;
	}
	public SpriteLayerGroup getSpriteLayerGroup()
	{
		return spriteLayers;
	}
	public boolean getOponentReady()
	{
		return oponentReady;
	}
	public void setOponentReady(boolean oponentReady)
	{
		this.oponentReady = oponentReady;
	}
	
	public void setAllyTurn(boolean allyTurn)
	{
		this.allyTurn = allyTurn;
	}
	public void setRunning(boolean running)
	{
		this.running = running;
	}
	public void setGameboardSetup(boolean setup)
	{
		this.gameboardSetup = setup;
	}
	public boolean getGameboardSetup()
	{
		return gameboardSetup;
	}
	public void setAllyVictory(boolean allyVictory)
	{
		this.allyVictory = allyVictory;
	}
	public boolean getAllyVictory()
	{
		return this.allyVictory;
	}
	public MatchmakingStatus getMatchmakingStatus()
	{
		return this.matchmakingStatus;
	}
	public void setMatchmakingStatus(MatchmakingStatus matchmakingStatus)
	{
		this.matchmakingStatus = matchmakingStatus;
	}
	public WarAtSeaGame getGame()
	{
		return this.game;
	}
}
