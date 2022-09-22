package ca.sait.cprg311.WarAtSea.Client.Graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import ca.sait.cprg311.WarAtSea.exceptions.ImageAlreadyLoadedException;
import ca.sait.cprg311.WarAtSea.exceptions.ImageNotLoadedException;

public class ImageManager {
	private static ImageManager manager;
	private Hashtable<String, BufferedImage> loadedImages;
	
	private ImageManager()
	{
		loadedImages = new Hashtable<String, BufferedImage>();
	}
	public static ImageManager getImageManager()
	{
		if(manager == null)
		{
			manager = new ImageManager();
		}
		return manager;
	}
	
	public void loadImage(ImageType type, String path, String imgName, int frameWidth, int frameHeight) throws IOException, ImageAlreadyLoadedException
	{
		if(!loadedImages.containsKey(imgName))
		{
			//BufferedImage img = ImageIO.read(new File(path));
			BufferedImage img = ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
			if(type == ImageType.ANIMATED)
			{
				AnimatedImage animg = new AnimatedImage(img, frameWidth, frameHeight);
				loadedImages.put(imgName,  animg);
			}
			else
			{
				loadedImages.put(imgName, img);
			}
		}
		else
		{
			throw new ImageAlreadyLoadedException("The image is already loaded into the SpriteManager, and can not be added again.");
		}
	}
	public boolean hasImageBeenLoaded(String imgName)
	{
		return loadedImages.containsKey(imgName);
	}
	public void unloadImage(String imgName) throws ImageNotLoadedException
	{
		if(!loadedImages.containsKey(imgName))
		{
			throw new ImageNotLoadedException("The image can not be unloaded from the SpriteManager as it was never loaded.");
		}
		loadedImages.remove(imgName);
	}
	public Sprite createSpriteFromLoadedImage(String imgName) throws ImageNotLoadedException
	{
		if(!loadedImages.containsKey(imgName))
		{
			throw new ImageNotLoadedException("Could not create a Sprite from the specified image as the image has not been loaded.");
		}
		Image img = loadedImages.get(imgName);
		if(img instanceof AnimatedImage)
		{
			//TODO
			return new AnimatedSprite((AnimatedImage)img, 1);
		}
		return new Sprite(loadedImages.get(imgName));
	}
	public void loadFromFile(String file, String relativePathPrefix) throws IOException, ImageAlreadyLoadedException
	{
		ImageManagerFile imgFile = new ImageManagerFile(file);
		ArrayList<ImageManagerFileEntry> entries = imgFile.getEntries();
		for(int i = 0; i < entries.size(); ++i)
		{
			ImageManagerFileEntry entry = entries.get(i); 
			//loadImage(entry.getImageType(), relativePathPrefix + entry.getRelativePath(), entry.getName(), entry.getWidth(), entry.getHeight());
			loadImage(entry.getImageType(), relativePathPrefix + entry.getRelativePath(), entry.getName(), entry.getWidth(), entry.getHeight());
		}
	}
}
