package ca.sait.cprg311.WarAtSea.Client;

import java.util.Observable;
import java.util.Observer;

import ca.sait.cprg311.WarAtSea.Client.Graphics.SpriteLayer;
import ca.sait.cprg311.WarAtSea.Client.Event.GameGridChangeEvent;
import ca.sait.cprg311.WarAtSea.Client.Graphics.ImageManager;
import ca.sait.cprg311.WarAtSea.Client.Graphics.Sprite;
import ca.sait.cprg311.WarAtSea.exceptions.ImageNotLoadedException;


/**
 * Class Description: A class that manages the sprites associated with an observed GameGrid object.
 * 
 * @author Jory Vardas
 * @version 1.0
 *
 */
public class GameGridSpriteHandler implements Observer
{
	//Constants
	private static final int BLOCK_SIZE = 32;
	//Attributes
	private SpriteLayer sprites;

	//Constructors
	
	/**
	 * Constructs a new GameGridSpriteHandler object
	 */
	public GameGridSpriteHandler(int gridWidth, int gridHeight)
	{
		sprites = new SpriteLayer();
	}
	
	//Getter and Setter Methods
	//Operational Methods
	
	/**
	 * Handles an update event called from an observed GameGrid object.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		GameGrid grid = (GameGrid)arg0;
		
		if(arg1 == null)
		{
			synchronized(sprites)
			{
				for(int i = 0; i < grid.getGridWidth(); ++i)
				{
					for(int j = 0; j < grid.getGridHeight(); ++j)
					{
						GameGridBlockType type = grid.getTypeAt(i, j);
						try
						{
							Sprite sp = ImageManager.getImageManager().createSpriteFromLoadedImage(type.toString());
							alignSpriteToGrid(sp,i,j);
							if(grid.getInvalidPlacementMaskAt(i, j))
							{
								Sprite mask = ImageManager.getImageManager().createSpriteFromLoadedImage("INVALID_PLACEMENT");
								alignSpriteToGrid(sp,i,j);
								sprites.addSprite(sp);
							}
							sprites.addSprite(sp);
						}
						catch(ImageNotLoadedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		else
		{
			GameGridChangeEvent event = (GameGridChangeEvent)arg1;
			
			if(event.affectsAll())
			{
				for(int i = 0; i < sprites.getNumberSprites(); ++i)
				{
					Sprite sp = sprites.getSprite(i);
					Sprite sp2 = null;
					try {
						sp2 = ImageManager.getImageManager().createSpriteFromLoadedImage(((GameGridBlockType)event.getNewValue()).toString());
					} catch (ImageNotLoadedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sp2.setX(sp.getX());
					sp2.setY(sp.getY());
					sp2.setEventHandler(sp.getEventHandler());
					sprites.setSpriteAt(sp2, i);
				}
			}
			else
			{
				Sprite sp = sprites.getSpriteContainingPoint(event.getX() * BLOCK_SIZE + 1, event.getY() * BLOCK_SIZE +1);
				Sprite sp2 = null;
				try {
					sp2 = ImageManager.getImageManager().createSpriteFromLoadedImage(((GameGridBlockType)event.getNewValue()).toString());
				} catch (ImageNotLoadedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sp2.setX(sp.getX());
				sp2.setY(sp.getY());
				sp2.setEventHandler(sp.getEventHandler());
				sprites.removeSprite(sp);
				sprites.addSprite(sp2);
			}
		}
	}
	
	/**
	 * Get the SpriteLayer containing all of the sprite's handled by the GameGridSpriteHandler object
	 * @return the SpriteLayer of the GameGridSpriteHandler object
	 */
	public SpriteLayer getSriteLayer()
	{
		return sprites;
	}
	
	/**
	 * Aligns the specified sprite to the specified grid coordinates.
	 * This effectively sets the sprite's location to [gridCoordX * gridElementSize, gridCoordY * gridElementSize].
	 * @param sprite The sprite to align to the specified grid coordinates.
	 * @param gridCoordX The horizontal grid coordinate to align the sprite to.
	 * @param gridCoordY The vertical grid coordinate to align the sprite to.
	 */
	public void alignSpriteToGrid(Sprite sprite, int gridCoordX, int gridCoordY)
	{
		sprite.setX(gridCoordX * BLOCK_SIZE);
		sprite.setY(gridCoordY * BLOCK_SIZE);
	}
	/**
	 * Get the sprite located at the specified grid coordinates.
	 * @param x The horizontal  grid coordinate at which the request sprite is located.
	 * @param y The vertical  grid coordinate at which the request sprite is located.
	 * @return The sprite located at the specified grid coordinates, or null if there is no visible sprite located at the specified coordinates.
	 */
	public Sprite getSpriteAt(int x, int y)
	{
		return sprites.getSpriteContainingPoint(x*BLOCK_SIZE + 1, y*BLOCK_SIZE + 1);
	}
	
	//Private Methods
}
