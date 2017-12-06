import java.awt.*;

public class GameObject
{
    protected int x;
    protected int y;
    protected int layer;
    protected int width;
    protected int height;
    
    public GameObject()
    {
	x = 0;
	y = 0;
	layer = 0;
	width = 0;
	height = 0;
    }
    public GameObject(int cx, int cy, int w, int h, int l)
    {
	x = cx;
	y = cy;
	width = w;
	height = h;
	layer = l;
    }
    
    public void set_centre(int cx, int cy)
    {
	x = cx;
	y = cy;
    }
    public void set_x(int cx)
    {
	x = cx;
    }
    public void set_y(int cy)
    {
	y = cy;
    }
    public int get_x()
    {
	return x;
    }
    public int get_y()
    {
	return y;
    }
    
    public void set_layer(int l)
    {
	layer = l;
    }
    public int get_layer()
    {
	return l;
    }
    
    public void set_dimensions(int w, int h)
    {
	width = w;
	height = h;
    }
    public void set_width(int w)
    {
	width = w;
    }
    public void set_height(int h)
    {
	height = w;
    }
    public int get_width()
    {
	return width;
    }
    public int get_height()
    {
	return height;
    }
}
