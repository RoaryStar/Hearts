import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

public class Main extends Applet implements MouseListener, MouseMotionListener
{
    //the game and user interface handler
    Game game;
    UI ui;

    //for drawing things
    Graphics buffer_g;
    Image offscreen;
    Font font_g;
    
    //framerate management
    long te = System.currentTimeMillis ();
    final int frames_per_second = 60;
    final int ms_per_frame = 1000 / frames_per_second;

    public void init ()
    {
	//sets up double-buffering
	offscreen = createImage (getSize ().width, getSize ().height);
	buffer_g = offscreen.getGraphics ();

	//reads everything to be read from files
	try
	{
	    Globals.CARD_UP_IMG = ImageIO.read (new File ("cards_up.gif"));
	    Globals.CARD_DOWN_IMG = ImageIO.read (new File ("cards_down.gif"));
	    Globals.HEARTS_BROKEN_IMG = ImageIO.read (new File ("hearts_broken.gif"));

	    Globals.CARD_DIM = new Point (
		    Globals.CARD_UP_IMG.getWidth (this) / 13,
		    Globals.CARD_UP_IMG.getHeight (this) / 4);
	    Globals.CARD_DIM_H = new Point (
		    Globals.CARD_DIM.x / 2,
		    Globals.CARD_DIM.y / 2);

	    Reader reader = new FileReader ("ai-weights.txt");
	    int data = reader.read ();
	    String s = "";
	    int cur_index = 0;
	    while (data != -1)
	    {
		if ((char) data == '\n')
		{
		    Globals.weights [cur_index++] = Double.parseDouble (s);
		    s = "";
		}
		else
		    s += (char) data;
		data = reader.read ();
	    }
	}
	catch (Exception e)
	{
	    System.out.println (e.getMessage ());
	    return;
	}

	//ensure that input works
	addMouseListener (this);
	addMouseMotionListener (this);

	//hearts broken image
	Globals.hb_img = new Sprite (Globals.HEARTS_BROKEN_IMG,
		new Point (0, 0), 1000,
		new Point (300, 211),
		new Point (Globals.HEARTS_BROKEN_IMG.getWidth (this),
		    Globals.HEARTS_BROKEN_IMG.getHeight (this)));

	//instantiates the game and ui-handler
	ui = new UI ();
	game = new Game (true, ui); //true for human, false for AI

	//instantiates the font
	font_g = new Font ("Verdana", Font.PLAIN, 20);
	buffer_g.setFont (font_g);

	//starts the game
	repaint ();
    }


    public void paint (Graphics g)
    {
	//setup background
	buffer_g.clearRect (0, 0, getSize ().width, getSize ().height);
	buffer_g.setColor (new Color (0, 127, 0));
	buffer_g.fillRect (0, 0, getSize ().width, getSize ().height);

	//draw the game
	game.draw_game (buffer_g);

	//complete double-buffering
	g.drawImage (offscreen, 0, 0, this);
    }


    public void update (Graphics g)
    {
	//framerate management
	long l = System.currentTimeMillis () - te;
	try
	{
	    if (l < ms_per_frame)
		Thread.sleep (ms_per_frame - l);
	}
	catch (InterruptedException e)
	{
	}
	te = System.currentTimeMillis ();
	
	//log any errors (deprecated)
	while (Globals.ERROR_LOG.size () > 0)
	{
	    System.out.println (Globals.ERROR_LOG.elementAt (0));
	    Globals.ERROR_LOG.remove (0);
	}

	//update things
	game.update_game (0.016);
	ui.update ();

	//actually paint
	paint (g);

	//call for another frame
	repaint ();
    }


    public void mouseClicked (MouseEvent e)
    {
	//everything is handled in the ui-hander.
	if (e.getButton () == MouseEvent.BUTTON1)
	    ui.activate_click (e.getPoint ());
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
