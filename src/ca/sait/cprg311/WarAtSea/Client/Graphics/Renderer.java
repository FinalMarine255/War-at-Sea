package ca.sait.cprg311.WarAtSea.Client.Graphics;

import java.awt.Color;
import java.awt.Graphics;

public class Renderer
{
	public void renderSpriteLayerGroup(RenderTarget target, SpriteLayerGroup group)
	{
		Graphics graph = target.getBufferStrat().getDrawGraphics();
		for(int i = 0; i < group.getNumberLayers(); ++i)
		{
			SpriteLayer layer = group.getSpriteLayer(i);
			if(layer.isVisible())
			{
				for(int j = 0; j < layer.getNumberSprites(); ++j)
				{
					Sprite sprite = layer.getSprite(j);
					graph.drawImage(sprite.getImage(), sprite.getX(), sprite.getY(), null);
				}
			}
		}
		graph.dispose();
		target.getBufferStrat().show();
	}
	public void renderSpriteLayer(RenderTarget target, SpriteLayer layer)
	{
		Graphics graph = target.getBufferStrat().getDrawGraphics();
		//target.repaint();
		graph.setColor(Color.WHITE);
		graph.fillRect(0,0,target.getWidth(),target.getHeight());
		//target.update(graph);
		if(layer.isVisible())
		{
			for(int j = 0; j < layer.getNumberSprites(); ++j)
			{
				Sprite sprite = layer.getSprite(j);
				graph.drawImage(sprite.getImage(), sprite.getX(), sprite.getY(), null);
			}
		}
		graph.dispose();
		target.getBufferStrat().show();
	}
	public void clearRenderTarget(RenderTarget target)
	{
		Graphics graph = target.getBufferStrat().getDrawGraphics();
		graph.setColor(Color.BLUE);
		graph.fillRect(0,0,target.getWidth(),target.getHeight());
		graph.dispose();
		target.getBufferStrat().show();
	}
}
