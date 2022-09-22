package ca.sait.cprg311.WarAtSea.Client.Graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class AnimatedSprite extends Sprite
{
	private int currentFrame;
	private boolean autoLoop;
	private boolean autoIncrement;
	private int framesPerSecond;
	
	public AnimatedSprite(AnimatedImage img, int framesPerSecond)
	{
		super.image = img;
		currentFrame = 0;
		autoLoop = true;
		autoIncrement = true;
		this.framesPerSecond = framesPerSecond;
	}
	
	public BufferedImage getFrame(int index)
	{
		return ((AnimatedImage)image).getFrame(index);
	}
	public BufferedImage getCurrentFrame()
	{
		return getFrame(currentFrame);
	}
	public BufferedImage getNextFrame()
	{
		return getFrame(++currentFrame);
	}
	public int getNumberOfFrames()
	{
		return ((AnimatedImage)super.image).getNumberFrames();
	}
	public int getFramesPerSecond()
	{
		return framesPerSecond;
	}
	public void incrementAnimation()
	{
		if(++currentFrame % getNumberOfFrames() == 0 && autoLoop)
		{
			currentFrame = 0;
		}
	}
	public void decrementAnimation()
	{
		--currentFrame;
	}
	public void skip(int numberFrames)
	{
		int linFrame = currentFrame + numberFrames;
		if(linFrame < getNumberOfFrames())
		{
			currentFrame = linFrame;
		}
		else
		{
			if(!autoLoop)
			{
				currentFrame = getNumberOfFrames()-1;
			}
			else
			{
				currentFrame = linFrame % getNumberOfFrames();
			}
		}
	}
	
	public boolean autoIncrement()
	{
		return this.autoIncrement;
	}
	public boolean autoLoop()
	{
		return this.autoLoop;
	}
	public void setAutoIncrement(boolean value)
	{
		this.autoIncrement = value;
	}
	public void setAutoLoop(boolean value)
	{
		this.autoLoop = value;
	}
	
	public void update()
	{
		if(autoIncrement())
		{
			incrementAnimation();
		}
	}
	
	@Override
	public Image getImage()
	{
		return getCurrentFrame();
	}
}
