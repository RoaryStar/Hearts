import java.awt.*;

//handles all input for simple retrieval
public class UI
{
    //location of the last click
    protected Point click;
    
    //as it says on the tin
    protected int ticks_since_last_click;
    
    public UI ()
    {
	click = new Point(-100, -100);
	ticks_since_last_click = 1;
    }
    
    public void activate_click(Point l)
    {
	click = new Point(l.x, l.y);
	ticks_since_last_click = 0;
    }
    public Point get_click()
    {
	if (ticks_since_last_click == 0)
	{
	    return new Point (click.x, click.y);
	}
	return null;
    }
    
    public void update()
    {
	++ticks_since_last_click;
    }
}
