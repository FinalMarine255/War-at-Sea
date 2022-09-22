package ca.sait.cprg311.WarAtSea.Client.Graphics;

import java.awt.image.BufferedImage;

public class AnimatedImage extends BufferedImage
{
	private int frameWidth;
	private int frameHeight;
	private int numberFrames;
	
	public AnimatedImage(BufferedImage rawImage, int frameWidth, int frameHeight) {
		//MIGHT BREAK EVERYTHING!!
		super(rawImage.getColorModel(), rawImage.getRaster(), rawImage.isAlphaPremultiplied(), null);
		
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		numberFrames = (rawImage.getWidth() / frameWidth) * (rawImage.getHeight() / frameHeight);
	}
	
	public int getNumberFrames()
	{
		return numberFrames;
	}
	public BufferedImage getFrame(int index)
	{
		int numPerRow = super.getWidth() / frameWidth;
		int rowNum = index / numPerRow;
		int colNum = index % numPerRow;
		int frameX = colNum * frameWidth;
		int frameY = rowNum * frameHeight;
		return super.getSubimage(frameX, frameY, frameWidth, frameHeight);
	}
}
