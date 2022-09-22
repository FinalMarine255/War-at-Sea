package ca.sait.cprg311.WarAtSea.Client.Graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ImageManagerFile
{
	private BufferedReader file;
	private ArrayList<ImageManagerFileEntry> entries;
	
	public ImageManagerFile(String fileName) throws IOException
	{
		file = null;
		entries = new ArrayList<ImageManagerFileEntry>();
		open(fileName);
		load();
	}
	
	public void open(String file) throws IOException
	{
		close();
		//this.file = new BufferedReader(new FileReader(file));
		InputStream st = ClassLoader.getSystemResourceAsStream(file);
		this.file = new BufferedReader(new InputStreamReader(st));
	}
	public void close() throws IOException
	{
		if(this.file != null)
		{
			this.file.close();
		}
	}
	public void load() throws IOException
	{
		String line = file.readLine();
		while(line != null)
		{
			if(!line.startsWith("//"))
			{
				StringTokenizer tok = new StringTokenizer(line, ";");
				entries.add(new ImageManagerFileEntry(ImageType.valueOf(tok.nextToken()), tok.nextToken(), tok.nextToken(), Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken())));
			}
			line = file.readLine();
		}
	}
	public ArrayList<ImageManagerFileEntry> getEntries()
	{
		return entries;
	}
}
