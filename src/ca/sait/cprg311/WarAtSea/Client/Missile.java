package ca.sait.cprg311.WarAtSea.Client;

import java.util.Observable;

import ca.sait.cprg311.WarAtSea.Client.Event.MissileTargetReachedEvent;
import ca.sait.cprg311.WarAtSea.Client.Graphics.ImageManager;
import ca.sait.cprg311.WarAtSea.Client.Graphics.Sprite;
import ca.sait.cprg311.WarAtSea.exceptions.ImageNotLoadedException;

public class Missile extends Observable
{
	private Sprite sprite;
	private int x0;
	private int y0;
	private int xc;
	private int yc;
	private int xf;
	private int yf;
	//speed in seconds
	private int speed = 1;
	
	public Missile(int x0, int y0, int xf, int yf, int speed)
	{
		try {
			if(speed < 0)
			{
				sprite = ImageManager.getImageManager().createSpriteFromLoadedImage("MISSILE_FLIP");
			}
			else
			{
				sprite = ImageManager.getImageManager().createSpriteFromLoadedImage("MISSILE");
			}
		} catch (ImageNotLoadedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.x0 = x0;
		this.y0 = y0;
		this.xc = x0;
		this.yc = y0;
		this.xf = xf;
		this.yf = yf;
		this.speed = speed;
		sprite.setX(x0 * 32);
		sprite.setY(y0 * 32);
	}
	
	public Sprite getSprite()
	{
		return this.sprite;
	}
	
	public int getX()
	{
		return xc;
	}
	public int getY()
	{
		return yc;
	}
	
	public void update()
	{
		if(xc != xf)
		{
			xc += speed;
		}
		if(yc != yf)
		{
			yc += speed;
		}
		
		sprite.setX(xc * 32);
		sprite.setY(yc * 32);
		
		
		if(xc == xf && yc == yf)
		{
			setChanged();
			notifyObservers(new MissileTargetReachedEvent());
		}
	}
}
