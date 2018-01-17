import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class Main extends Applet implements MouseListener, MouseMotionListener
{
	Game game;
	UI ui;
	
	Graphics buffer_g;
	Image offscreen;
	
	long te = System.currentTimeMillis();
		
	Deck d;
	Deck e;
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
			Globals.CARD_DIM_H = new Point(
					Globals.CARD_DIM.x/2,
					Globals.CARD_DIM.y/2);
		} 
		catch (IOException e)
		{
			System.out.println("asdf");
		}
		
		addMouseListener (this);
		addMouseMotionListener (this);
		
		//ui = new UI;
		game = new Game(false, ui);
		
		d = new Deck(new Point(0,0));
		d.initialize_as_standard();
		e = new Deck(new Point(700,500));
	}
		
	public void paint (Graphics g)
	{
		double elapsed = System.currentTimeMillis() - te;
		te = System.currentTimeMillis();
	
		buffer_g.clearRect(0, 0, getSize().width, getSize().height);
		buffer_g.setColor(new Color(0, 127, 0));
		buffer_g.fillRect(0, 0, getSize().width, getSize().height);
		
		game.update_game(0.016);
		game.draw_game(buffer_g);
		
		g.drawImage(offscreen, 0, 0, this);
		
		repaint(16);
	}
	
	public void update(Graphics g)
	{
		long l = System.currentTimeMillis() - te;
		try
		{
			if (l < 16)
				Thread.sleep(16-l);
		}
		catch (InterruptedException e)
		{
		}
		while (Globals.ERROR_LOG.size() > 0)
		{
			System.out.println(Globals.ERROR_LOG.elementAt(0));
			Globals.ERROR_LOG.remove(0);
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
