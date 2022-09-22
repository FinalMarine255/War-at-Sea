package ca.sait.cprg311.WarAtSea.Client;

import java.util.Observable;
import java.util.Observer;

import ca.sait.cprg311.WarAtSea.Client.Event.AttributeChangedEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.GameGridChangeEvent;
import ca.sait.cprg311.WarAtSea.Client.Event.ShipRepositionedEvent;

/**
 * Class Description: A class used to store a 2D grid of GameGridBlockType elements.
 * @author Jory Vardas
 * @version 1.0
 *
 */
public class GameGrid extends Observable implements Observer
{
	//Constants
	//Attributes
	/**
	 * A 1D array of GameGridBlockType elements which stores the entire grid.
	 */
	private GameGridBlockType[] gridBlockTypes;
	private boolean[] invalidPlacementMask;
	/**
	 * The number of columns in the grid.
	 */
	private int numBlocksWidth;
	/**
	 * The number of rows in the grid.
	 */
	private int numBlocksHeight;
	
	//Constructors
	/**
	 * Constructs a new GameGrid object with the specified number of rows and columns.
	 * @param numBlocksAcross The number of columns the grid is to have.
	 * @param numBlocksDown The number of rows the grid is to have.
	 */
	public GameGrid(int numBlocksAcross, int numBlocksDown)
	{
		gridBlockTypes = new GameGridBlockType[numBlocksAcross * numBlocksDown];
		invalidPlacementMask = new boolean[numBlocksAcross * numBlocksDown];
		this.numBlocksHeight = numBlocksDown;
		this.numBlocksWidth = numBlocksAcross;
		for(int i = 0; i < numBlocksAcross * numBlocksDown; ++i)
		{
			gridBlockTypes[i] = GameGridBlockType.INVALID;
			invalidPlacementMask[i] = false;
		}
	}
	
	//Getter and Setter Methods

	/**
	 * Get the number of columns in the grid.
	 * @return The number of columns in the grid.
	 */
	public int getGridWidth()
	{
		return numBlocksWidth;
	}
	/**
	 * Get the number of rows in the grid.
	 * @return The number of rows in the grid.
	 */
	public int getGridHeight()
	{
		return numBlocksHeight;
	}
	
	//Operational Methods
	
	/**
	 * Gets the GameGridBlockType element at the specified grid coordinates.
	 * @param i The horizontal grid coordinate of the element to return.
	 * @param j The vertical grid coordinate of the element to return.
	 * @return The element located at the specified grid coordinates.
	 */
	public GameGridBlockType getTypeAt(int i, int j)
	{
		return gridBlockTypes[j * numBlocksWidth + i];
	}
	public boolean getInvalidPlacementMaskAt(int i, int j)
	{
		return invalidPlacementMask[j * numBlocksWidth + i];
	}
	/**
	 * Sets the GameGridBlockType element at the specified grid coordinates to the specified GameGridBlockType.
	 * This method will notify observers of the change.
	 * @param type The GameGridBlockType to set the element at the specified grid coordinates to.
	 * @param i The horizontal grid coordinate of the element to set.
	 * @param j The vertical grid coordinate of the element to set.
	 */
	public void setTypeAt(GameGridBlockType type, int i, int j)
	{
		int pos = j * numBlocksWidth + i;
		GameGridChangeEvent event = new GameGridChangeEvent(gridBlockTypes[pos], type, i, j);
		gridBlockTypes[pos] = type;
		setChanged();
		notifyObservers(event);
	}
	/**
	 * Sets every GameGridBlockType element in the grid to the specified GameGridBlockType.
	 * This method will notify observers of the change.
	 * @param type The GameGridBlockType to set every element of the grid to.
	 */
	public void setAllTo(GameGridBlockType type)
	{
		for(int i = 0; i < numBlocksWidth * numBlocksHeight; ++i)
		{
			gridBlockTypes[i] = type;
		}
		setChanged();
		notifyObservers(new GameGridChangeEvent(type));
	}
	@Override
	public void addObserver(Observer obs)
	{
		super.addObserver(obs);
		if(gridBlockTypes != null)
		{
			setChanged();
			notifyObservers();
		}
	}
	
	public void placeShip(Ship ship)
	{
		switch(ship.getOrientation())
		{
		case HORIZONTAL:
			switch(ship.getShipType())
			{
			case AIRCRAFT_CARRIER:
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX()] = GameGridBlockType.AIRCRAFT_CARRIER_SEC_1;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 1] = GameGridBlockType.AIRCRAFT_CARRIER_SEC_2;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 2] = GameGridBlockType.AIRCRAFT_CARRIER_SEC_3;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 3] = GameGridBlockType.AIRCRAFT_CARRIER_SEC_4;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 4] = GameGridBlockType.AIRCRAFT_CARRIER_SEC_5;

				break;
			case BATTLESHIP:
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX()] = GameGridBlockType.BATTLESHIP_SEC_1;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 1] = GameGridBlockType.BATTLESHIP_SEC_2;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 2] = GameGridBlockType.BATTLESHIP_SEC_3;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 3] = GameGridBlockType.BATTLESHIP_SEC_4;
				break;
			case CRUISER:
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX()] = GameGridBlockType.CRUISER_SEC_1;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 1] = GameGridBlockType.CRUISER_SEC_2;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 2] = GameGridBlockType.CRUISER_SEC_3;
				break;
			case DESTROYER:
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX()] = GameGridBlockType.DESTROYER_SEC_1;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 1] = GameGridBlockType.DESTROYER_SEC_2;
				break;
			case SUBMARINE:
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX()] = GameGridBlockType.SUBMARINE_SEC_1;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 1] = GameGridBlockType.SUBMARINE_SEC_2;
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX() + 2] = GameGridBlockType.SUBMARINE_SEC_3;
				break;
			}
			
			break;
		case VERTICAL:
			switch(ship.getShipType())
			{
			case AIRCRAFT_CARRIER:
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX()] = GameGridBlockType.AIRCRAFT_CARRIER_SEC_1;
				gridBlockTypes[(ship.getY() + 1) * numBlocksWidth + ship.getX()] = GameGridBlockType.AIRCRAFT_CARRIER_SEC_2;
				gridBlockTypes[(ship.getY() + 2) * numBlocksWidth + ship.getX()] = GameGridBlockType.AIRCRAFT_CARRIER_SEC_3;
				gridBlockTypes[(ship.getY() + 3) * numBlocksWidth + ship.getX()] = GameGridBlockType.AIRCRAFT_CARRIER_SEC_4;
				gridBlockTypes[(ship.getY() + 4) * numBlocksWidth + ship.getX()] = GameGridBlockType.AIRCRAFT_CARRIER_SEC_5;
				break;
			case BATTLESHIP:
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX()] = GameGridBlockType.BATTLESHIP_SEC_1;
				gridBlockTypes[(ship.getY() + 1) * numBlocksWidth + ship.getX()] = GameGridBlockType.BATTLESHIP_SEC_2;
				gridBlockTypes[(ship.getY() + 2) * numBlocksWidth + ship.getX()] = GameGridBlockType.BATTLESHIP_SEC_3;
				gridBlockTypes[(ship.getY() + 3) * numBlocksWidth + ship.getX()] = GameGridBlockType.BATTLESHIP_SEC_4;
				break;
			case CRUISER:
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX()] = GameGridBlockType.CRUISER_SEC_1;
				gridBlockTypes[(ship.getY() + 1) * numBlocksWidth + ship.getX()] = GameGridBlockType.CRUISER_SEC_2;
				gridBlockTypes[(ship.getY() + 2) * numBlocksWidth + ship.getX()] = GameGridBlockType.CRUISER_SEC_3;
				break;
			case DESTROYER:
				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX()] = GameGridBlockType.DESTROYER_SEC_1;
				gridBlockTypes[(ship.getY() + 1) * numBlocksWidth + ship.getX()] = GameGridBlockType.DESTROYER_SEC_2;
				break;
			case SUBMARINE:

				gridBlockTypes[ship.getY() * numBlocksWidth + ship.getX()] = GameGridBlockType.SUBMARINE_SEC_1;
				gridBlockTypes[(ship.getY() + 1) * numBlocksWidth + ship.getX()] = GameGridBlockType.SUBMARINE_SEC_2;
				gridBlockTypes[(ship.getY() + 2) * numBlocksWidth + ship.getX()] = GameGridBlockType.SUBMARINE_SEC_3;
				break;
			}
			
			break;
		}

		setChanged();
		notifyObservers();
	}
	//Private Methods
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if(o instanceof Ship)
		{
			Ship ship = (Ship)o;
			ShipRepositionedEvent event = (ShipRepositionedEvent)arg;
			//removeShip(event.getPreviousX(), event.getPreviousY(), event.getPreviousOrientation(), ship.getShipType());
			//placeShip(ship);
		}
	}
}
