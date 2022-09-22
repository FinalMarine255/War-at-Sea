package ca.sait.cprg311.WarAtSea.Client;

public class GameGridUpdateInfo
{
	public int x;
	public int y;
	public GameGridBlockType prevType;
	public GameGridBlockType newType;
	
	public GameGridUpdateInfo(int x, int y, GameGridBlockType prev, GameGridBlockType n)
	{
		this.x = x;
		this.y = y;
		prevType = prev;
		newType = n;
	}
}
