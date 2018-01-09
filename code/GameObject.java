import java.awt.*;

public abstract class GameObject
{
    protected Point location;
    protected Point dimensions;
    
    public GameObject()
    {
	location = new Point(0,0);
	dimensions = new Point(1,1);
    }
    public GameObject(Point loc, Point dim)
    {
	location = loc;
	dimensions = dim;
    }
    
    public void set_loc(Point l)
    {
	location = l;
    }
    public void set_loc_x(int x)
    {
	location.x = x;
    }
    public void set_loc_y(int y)
    {
	location.y = y;
    }
    public Point get_loc()
    {
	return location;
    }
    public int get_loc_x()
    {
	return location.x;
    }
    public int get_loc_y()
    {
	return location.y;
    }
    
    public void set_dimensions(Point d)
    {
	dimensions = d;
    }
    public void set_width(int w)
    {
	dimensions.x = w;
    }
    public void set_height(int h)
    {
	dimensions.y = h;
    }
    public Point get_dimensions()
    {
	return dimensions;
    }
    public int get_width()
    {
	return dimensions.x;
    }
    public int get_height()
    {
	return dimensions.y;
    }
}
