import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class Main extends Applet implements MouseListener, MouseMotionListener
{
	Game game;
	
	Graphics buffer_g;
	Image offscreen;
	Card c;
	
	long te = System.currentTimeMillis();
		
	public void init ()
	{
		offscreen = createImage(getSize().width, getSize().height);
		buffer_g = offscreen.getGraphics();
		
		try 
		{
			Globals.CARD_UP_IMG = ImageIO.read(new File("cards_up.gif"));
			Globals.CARD_DOWN_IMG = ImageIO.read(new File("cards_down.gif"));
			
			Globals.CARD_DIM = new Point(
					Globals.CARD_UP_IMG.getWidth(this) / 13,
					Globals.CARD_UP_IMG.getHeight(this) / 4);
		} 
		catch (IOException e)
		{
			System.out.println("asdf");
		}
		
		addMouseListener (this);
		addMouseMotionListener (this);
	}
		
	public void paint (Graphics g)
	{
		double elapsed = System.currentTimeMillis() - te;
		te = System.currentTimeMillis();
	
		buffer_g.clearRect(0, 0, getSize().width, getSize().height);
		
		c = new Card(Globals.rand.nextInt(4), Globals.rand.nextInt(13), 0, new Point(50,50));
		c.draw(buffer_g);
		
		g.drawImage(offscreen, 0, 0, this);
		
		repaint(200);
	}
	
	public void update(Graphics g)
	{
		long l = System.currentTimeMillis() - te;
		try
		{
			if (l < 200)
				Thread.sleep(200-l);
		}
		catch (InterruptedException e)
		{
		}
		paint(g);
	}
	
	public void mouseClicked (MouseEvent e)
	{
	}

	public void mouseEntered (MouseEvent e)
	{
	}

	public void mouseExited (MouseEvent e)
	{
	}

	public void mousePressed (MouseEvent e)
	{
	}

	public void mouseReleased (MouseEvent e)
	{
	}

	public void mouseDragged (MouseEvent e)
	{
	}

	public void mouseMoved (MouseEvent e)
	{
	}
}
