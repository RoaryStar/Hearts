import java.awt.*;

public abstract class GameObject
{
	protected Point location;
	protected Point dimensions;
	protected int layer;

	public GameObject ()
	{
		location = new Point (0, 0);
		dimensions = new Point (1, 1);
		layer = -1;
	}


	public GameObject (Point loc, Point dim, int lr)
	{
		location = loc;
		dimensions = dim;
		layer = lr;
	}


	public void set_loc (Point l)
	{
		location = new Point(l.x, l.y);
	}


	public void set_loc_x (int x)
	{
		location.x = x;
	}


	public void set_loc_y (int y)
	{
		location.y = y;
	}


	public Point get_loc ()
	{
		return new Point(location.x, location.y);
	}


	public int get_loc_x ()
	{
		return location.x;
	}


	public int get_loc_y ()
	{
		return location.y;
	}


	public void set_dimensions (Point d)
	{
		dimensions = d;
	}


	public void set_width (int w)
	{
		dimensions.x = w;
	}


	public void set_height (int h)
	{
		dimensions.y = h;
	}


	public Point get_dimensions ()
	{
		return dimensions;
	}


	public int get_width ()
	{
		return dimensions.x;
	}


	public int get_height ()
	{
		return dimensions.y;
	}


	public void set_layer (int l)
	{
		layer = l;
	}


	public int get_layer ()
	{
		return layer;
	}
	
	public boolean is_inside(Point p)
	{
		return (p.x > location.x && p.y > location.y &&
			p.x < location.x + dimensions.x &&
			p.y < location.y + dimensions.y);
	}
}
