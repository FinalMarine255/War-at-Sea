package ca.sait.cprg311.WarAtSea.Client.Graphics;

import java.util.ArrayList;
import java.util.Observable;

public class SpriteLayer extends Observable
{
	private ArrayList<Sprite> sprites;
	private boolean visible;
	private int z;
	
	public SpriteLayer()
	{
		this.sprites = new ArrayList<Sprite>();
		this.visible = true;
		this.z = 0;
	}
	public SpriteLayer(boolean visible, int z)
	{
		this.sprites = new ArrayList<Sprite>();
		this.visible = visible;
		this.z = z;
	}
	
	public void addSprite(Sprite sprite)
	{
		sprites.add(sprite);
	}
	public void removeSprite(Sprite sprite)
	{
		sprites.remove(sprite);
	}
	public void removeSprite(int index)
	{
		sprites.remove(index);
	}
	public void removeAll()
	{
		sprites.clear();
	}
	
	public Sprite getSpriteContainingPoint(int x, int y)
	{
		if(visible == false)
		{
			return null;
		}
		Sprite ret = null;
		for(int i = 0; i < sprites.size(); ++i)
		{
			Sprite sp = sprites.get(i);
			if((sp.getX() <= x) && (sp.getY() <= y) && (sp.getX() + sp.getSpriteWidth() >= x) && (sp.getY() + sp.getSpriteHeight() >= y))
			{
				ret = sp;
				break;
			}
		}
		return ret;
	}
	
	public boolean isVisible()
	{
		return this.visible;
	}
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
	public int getZ()
	{
		return this.z;
	}
	public void setZ(int z)
	{
		this.z = z;
		setChanged();
		notifyObservers();
	}
	public int getNumberSprites()
	{
		return sprites.size();
	}
	public Sprite getSprite(int index)
	{
		return sprites.get(index);
	}
	public void setSpriteAt(Sprite sprite, int index)
	{
		sprites.set(index, sprite);
	}
	public void updateAnimated()
	{
		for(int i = 0; i < sprites.size(); ++i)
		{
			Sprite sp = sprites.get(i);
			if(sp instanceof AnimatedSprite)
			{
				AnimatedSprite asp = (AnimatedSprite)sp;
				asp.update();
			}
		}
	}
	public void clearSprites()
	{
		sprites.clear();
	}
}
