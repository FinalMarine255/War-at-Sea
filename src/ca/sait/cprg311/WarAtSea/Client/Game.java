package ca.sait.cprg311.WarAtSea.Client;

import java.util.Observer;

import ca.sait.cprg311.WarAtSea.Client.Graphics.ImageManager;
import ca.sait.cprg311.WarAtSea.Client.Graphics.RenderTarget;
import ca.sait.cprg311.WarAtSea.Client.Graphics.Renderer;

/**
 * Class Description: An abstract class containing attributes and methods that are shared by any 2D game.
 * @author Jory Vardas
 * @version 1.0
 *
 */
public abstract class Game
{
	//Constants
	//Attributes
	
	/**
	 * A GameState object containing the game state information of the game.
	 */
	protected GameState gameState;
	/**
	 * The image manger of the game.
	 */
	protected ImageManager imageManager;
	/**
	 * The render target of the game.
	 */
	protected RenderTarget renderTarget;
	/**
	 * The renderer of the game.
	 */
	protected Renderer renderer;

	//Constructors
	//Getter and Setter Methods
	//Operational Methods
	
	/**
	 * Contains the code used to initialized the game and it's objects.
	 * Will also spawn any threads that the game may need.
	 */
	public abstract void init();
	//TODO
	/**
	 * Contains the code to play the game.
	 */
	public abstract void play();
	//public abstract void update();
	
	/**
	 * Contains the code to dispose of any game objects or threads that need it.
	 */
	public abstract void clean();
	
	//Private Methods
}
