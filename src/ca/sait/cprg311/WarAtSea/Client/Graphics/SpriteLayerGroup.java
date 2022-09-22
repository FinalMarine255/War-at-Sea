package ca.sait.cprg311.WarAtSea.Client.Graphics;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class SpriteLayerGroup implements Observer
{
	ArrayList<SpriteLayer> spriteLayers;
	public SpriteLayerGroup()
	{
		spriteLayers = new ArrayList<SpriteLayer>();
	}
	
	public void addSpriteLayer(SpriteLayer layer)
	{
		layer.addObserver(this);
		spriteLayers.add(layer);
		zOrder();
	}
	public void removeSpriteLayer(SpriteLayer layer)
	{
		spriteLayers.remove(layer);
		layer.deleteObserver(this);
	}
	public int getNumberLayers()
	{
		return spriteLayers.size();
	}
	public SpriteLayer getSpriteLayer(int index)
	{
		return spriteLayers.get(index);
	}
	
	public void zOrder()
	{
		boolean changed = false;
		do
		{
			changed = false;
			for(int i = 0; i < spriteLayers.size() - 1; ++i)
			{
				if(spriteLayers.get(i).getZ() > spriteLayers.get(i+1).getZ())
				{
					changed = true;
					SpriteLayer tmp = spriteLayers.get(i);
					spriteLayers.set(i, spriteLayers.get(i+1));
					spriteLayers.set(i+1, tmp);
				}
			}
		}
		while(changed != false);
	}
	public void updateAnimated()
	{
		for(int i = 0; i < spriteLayers.size(); ++i)
		{
			SpriteLayer layer = spriteLayers.get(i);
			if(layer.isVisible())
			{
				layer.updateAnimated();
			}
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1)
	{
		zOrder();
	}
}
