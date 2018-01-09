import java.awt.*;

public abstract class MovingObject extends GameObject
{
    private Point new_loc;
    private Point prev_loc;
    private double progress;
    private double expected_time;
    
    public MovingObject()
    {
	super();
	new_loc = new Point(0, 0);
	prev_loc = new Point(0, 0);
	progress = 0.0;
	expected_time = 0.0;
    }
    public MovingObject(Point l, Point d)
    {
	super(l, d);
	new_loc = new Point(0, 0);
	prev_loc = new Point(0, 0);
	progress = 0.0;
	expected_time = 0.0;
    }
    
    public void move_to_location(Point l, double time)
    {
	throw new ArithmeticException("MovingObject::move_to_location not implemented!");
    }
    
    public void update(double time_elapsed)
    {
	throw new ArithmeticException("MovingObject::update not implemented!");
    }
}
