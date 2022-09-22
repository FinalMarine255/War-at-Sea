package ca.sait.cprg311.WarAtSea.Client;

import java.util.Observable;
import java.util.Observer;

import ca.sait.cprg311.WarAtSea.Client.Event.ShipRepositionedEvent;
import ca.sait.cprg311.WarAtSea.Client.Graphics.ImageManager;
import ca.sait.cprg311.WarAtSea.Client.Graphics.Sprite;
import ca.sait.cprg311.WarAtSea.Client.Graphics.SpriteLayer;
import ca.sait.cprg311.WarAtSea.exceptions.ImageNotLoadedException;

public class ShipPlacementGrid implements Observer
{
	private SpriteLayer sprites;
	private int[] shipPlacement;
	private int gridHeight;
	private int gridWidth;
	
	public ShipPlacementGrid(int gridWidth, int gridHeight)
	{
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		sprites = new SpriteLayer();
		sprites.setZ(1);
		shipPlacement = new int[gridWidth * gridHeight];
		for(int i = 0; i < gridWidth * gridHeight; ++i)
		{
			shipPlacement[i] = 0;
		}
	}
	
	private int calculateCombinationCode(int code1, int code2)
	{
		//TODO place in const?
		return code1 * 10 + code2;
	}
	private int calculateCode1(int combinedCode, int code2)
	{
		return (combinedCode - code2) / 10;
	}
	
	//TODO rename code to id?
	private void setGridPosition(int x, int y, ShipType shipType, int shipCode, boolean remove, boolean forceInvalid)
	{
		if(x < 0 || x >= gridWidth || y < 0 || y >= gridHeight)
		{
			//TODO
			throw new ArrayIndexOutOfBoundsException();
		}
		if(remove)
		{
			shipPlacement[y * gridWidth + x] = calculateCode1(shipPlacement[y * gridWidth + x], shipCode);
		}
		else
		{
			shipPlacement[y * gridWidth + x] = calculateCombinationCode(shipPlacement[y * gridWidth + x], shipCode);
		}
		Sprite sp = sprites.getSpriteContainingPoint(x * 32 + 1, y * 32 + 1);
		if(sp == null)
		{
			sp = new Sprite();
			sp.setX(x*32);
			sp.setY(y*32);
		}
		Sprite sp2 = null;
		if(shipPlacement[y* gridWidth + x] >= 6 || forceInvalid)
		{
			try {
				sp2 = ImageManager.getImageManager().createSpriteFromLoadedImage("INVALID_PLACEMENT");
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
		else if(shipPlacement[y * gridWidth + x] == 0)
		{
			sprites.removeSprite(sp);
		}
		else
		{
			try {
				sp2 = ImageManager.getImageManager().createSpriteFromLoadedImage("VALID_PLACEMENT");
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
	public synchronized int getGridPosition(int x, int y)
	{
		return shipPlacement[y * gridWidth + x];
	}
	
	public synchronized void placeInvalidShip(Ship ship)
	{
		int x = ship.getX();
		int y = ship.getY();
		switch(ship.getOrientation())
		{
		case HORIZONTAL:
			switch(ship.getShipType())
			{
			case AIRCRAFT_CARRIER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+3, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+4, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				
				break;
			case BATTLESHIP:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+3, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case CRUISER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case DESTROYER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case SUBMARINE:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			}
			
			break;
		case VERTICAL:
			switch(ship.getShipType())
			{
			case AIRCRAFT_CARRIER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+3, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+4, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				
				break;
			case BATTLESHIP:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+3, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case CRUISER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case DESTROYER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case SUBMARINE:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), false, true);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			}
			
			break;
		}
	}
	
	public synchronized boolean placeShip(Ship ship)
	{
		boolean canPlace = true;
		int x = ship.getX();
		int y = ship.getY();
		try
		{
			switch(ship.getOrientation())
			{
			case HORIZONTAL:
				switch(ship.getShipType())
				{
				case AIRCRAFT_CARRIER:
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+3, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+4, y, ship.getShipType(), ship.getShipCode(), false, false);
					canPlace = getGridPosition(x, y) < 6 && getGridPosition(x+1, y) < 6 && getGridPosition(x+2, y) < 6 &&
							getGridPosition(x+3, y) < 6 && getGridPosition(x+4, y) < 6; 
					
					break;
				case BATTLESHIP:
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+3, y, ship.getShipType(), ship.getShipCode(), false, false);
					canPlace = getGridPosition(x, y) < 6 && getGridPosition(x+1, y) < 6 && getGridPosition(x+2, y) < 6 &&
							getGridPosition(x+3, y) < 6;
					break;
				case CRUISER:
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), false, false);
					canPlace = getGridPosition(x, y) < 6 && getGridPosition(x+1, y) < 6 && getGridPosition(x+2, y) < 6;
					break;
				case DESTROYER:
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), false, false);
					canPlace = getGridPosition(x, y) < 6 && getGridPosition(x+1, y) < 6;
					break;
				case SUBMARINE:
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), false, false);
					canPlace = getGridPosition(x, y) < 6 && getGridPosition(x+1, y) < 6 && getGridPosition(x+2, y) < 6;
					break;
				}
				
				break;
			case VERTICAL:
				switch(ship.getShipType())
				{
				case AIRCRAFT_CARRIER:
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+3, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+4, ship.getShipType(), ship.getShipCode(), false, false);
					canPlace = getGridPosition(x, y) < 6 && getGridPosition(x, y+1) < 6 && getGridPosition(x, y+2) < 6 &&
							getGridPosition(x, y+3) < 6 && getGridPosition(x, y+4) < 6; 
					
					break;
				case BATTLESHIP:
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+3, ship.getShipType(), ship.getShipCode(), false, false);
					canPlace = getGridPosition(x, y) < 6 && getGridPosition(x, y+1) < 6 && getGridPosition(x, y+2) < 6 &&
							getGridPosition(x, y+3) < 6;
					break;
				case CRUISER:
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), false, false);
					canPlace = getGridPosition(x, y) < 6 && getGridPosition(x, y+1) < 6 && getGridPosition(x, y+2) < 6;
					break;
				case DESTROYER:
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), false, false);
					canPlace = getGridPosition(x, y) < 6 && getGridPosition(x, y+1) < 6;
					break;
				case SUBMARINE:
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), false, false);
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), false, false);
					canPlace = getGridPosition(x, y) < 6 && getGridPosition(x, y+1) < 6 && getGridPosition(x, y+2) < 6;
					break;
				}
				
				break;
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			removeShip(ship);
			placeInvalidShip(ship);
			canPlace = false;
		}
		return canPlace;
	}
	public synchronized void removeShip(Ship ship)
	{
		int x = ship.getX();
		int y = ship.getY();
		switch(ship.getOrientation())
		{
		case HORIZONTAL:
			switch(ship.getShipType())
			{
			case AIRCRAFT_CARRIER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+3, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+4, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				
				break;
			case BATTLESHIP:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+3, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case CRUISER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case DESTROYER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case SUBMARINE:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+1, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x+2, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			}
			
			break;
		case VERTICAL:
			switch(ship.getShipType())
			{
			case AIRCRAFT_CARRIER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+3, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+4, ship.getShipType(), ship.getShipCode(), true, false); 
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				
				break;
			case BATTLESHIP:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+3, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case CRUISER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case DESTROYER:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			case SUBMARINE:
				try
				{
					setGridPosition(x, y, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+1, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				try
				{
					setGridPosition(x, y+2, ship.getShipType(), ship.getShipCode(), true, false);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
				break;
			}
			
			break;
		}
	}
	public SpriteLayer getSpriteLayer()
	{
		return sprites;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		if(arg0 instanceof Ship)
		{
			Ship ship = (Ship)arg0;
			ShipRepositionedEvent event = (ShipRepositionedEvent)arg1;
			//placeShip(ship);
		}
	}
	
	
}
