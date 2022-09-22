package ca.sait.cprg311.WarAtSea.Client.Graphics;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

public class RenderTarget extends Canvas
{
	private BufferStrategy bufferStrat;
	
	public RenderTarget()
	{
		bufferStrat = null;
	}
	public BufferStrategy getBufferStrat()
	{
		if(bufferStrat == null)
		{
			createBufferStrategy(3);
			bufferStrat = getBufferStrategy();
		}
		return bufferStrat;
	}
}
