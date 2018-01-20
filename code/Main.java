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
	
	final int frames_per_second = 60;
	final int ms_per_frame = 1000/frames_per_second;
		
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
			Globals.HEARTS_BROKEN_IMG = ImageIO.read(new File("hearts_broken.gif"));
			
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
		
		Globals.hb_img = new Sprite(Globals.HEARTS_BROKEN_IMG,
				    new Point (0, 0), 1000,
				    new Point (300, 211),
				    new Point (Globals.HEARTS_BROKEN_IMG.getWidth(this),
					       Globals.HEARTS_BROKEN_IMG.getHeight(this)));
		
		ui = new UI();
		game = new Game(true, ui);
		
		d = new Deck(new Point(0,0));
		d.initialize_as_standard();
		e = new Deck(new Point(700,500));
		
		repaint();
	}
		
	public void paint (Graphics g)
	{
		double elapsed = System.currentTimeMillis() - te;
		te = System.currentTimeMillis();
	
		buffer_g.clearRect(0, 0, getSize().width, getSize().height);
		buffer_g.setColor(new Color(0, 127, 0));
		buffer_g.fillRect(0, 0, getSize().width, getSize().height);
		
		game.draw_game(buffer_g);
		
		
		g.drawImage(offscreen, 0, 0, this);
	}
	
	public void update(Graphics g)
	{
		long l = System.currentTimeMillis() - te;
		try
		{
			if (l < ms_per_frame)
				Thread.sleep(ms_per_frame-l);
		}
		catch (InterruptedException e)
		{
		}
		while (Globals.ERROR_LOG.size() > 0)
		{
			System.out.println(Globals.ERROR_LOG.elementAt(0));
			Globals.ERROR_LOG.remove(0);
		}
		
		game.update_game(0.016);
		ui.update();
		
		paint(g);
		
		repaint();
	}
	
	public void mouseClicked (MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1)
			ui.activate_click(e.getPoint());
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
