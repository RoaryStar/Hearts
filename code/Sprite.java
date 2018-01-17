import java.awt.*;

public abstract class Sprite extends MovingObject
{
	private Image image;
	private Point img_source;

	public Sprite ()
	{
		super ();
		image = null;
		img_source = new Point (0, 0);
	}


	public Sprite (Image i, int lr, Point l, Point d)
	{
		super (l, d, lr);
		set_image (i);
		img_source = new Point (0, 0);
	}


	public Sprite (Image i, Point s, int lr, Point l, Point d)
	{
		super (l, d, lr);
		set_image (i);
		img_source = s;
	}


	public void draw (Graphics g)
	{
		g.drawImage (image,
				location.x, location.y,
				location.x + dimensions.x, location.y + dimensions.y,
				img_source.x, img_source.y,
				img_source.x + dimensions.x, img_source.y + dimensions.y,
				null);
	}


	public void set_image (Image i)
	{
		image = i;
	}


	public void set_source_coords (Point s)
	{
		img_source = s;
	}
}
