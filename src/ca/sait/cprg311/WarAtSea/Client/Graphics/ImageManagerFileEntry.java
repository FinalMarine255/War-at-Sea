package ca.sait.cprg311.WarAtSea.Client.Graphics;

public class ImageManagerFileEntry
{
	private String relativePath;
	private ImageType type;
	private String name;
	private int width;
	private int height;
	
	public ImageManagerFileEntry(ImageType type, String relativePath, String name, int width, int height)
	{
		this.relativePath = relativePath;
		this.type = type;
		this.name = name;
		this.width = width;
		this.height = height;
	}
	
	public String getRelativePath()
	{
		return relativePath;
	}
	public String getName()
	{
		return name;
	}
	public ImageType getImageType()
	{
		return type;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
}
