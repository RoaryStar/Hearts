import java.awt.*;

public abstract class MovingObject extends GameObject
{
	private Point new_loc;
	private Point prev_loc;
	private double progress;
	private double expected_time;

	public MovingObject ()
	{
		super ();
		new_loc = new Point (0, 0);
		prev_loc = new Point (0, 0);
		progress = 2.0;
		expected_time = 0.0;
	}


	public MovingObject (Point l, Point d, int lr)
	{
		super (l, d, lr);
		new_loc = new Point (0, 0);
		prev_loc = new Point (0, 0);
		progress = 2.0;
		expected_time = 0.0;
	}


	public void move_to_location (Point l, double time)
	{
		//nothing occurs here, since it's at updates that things
		//should happen so that more than one thing can be
		//animated at once
		prev_loc = new Point(location.x, location.y);
		new_loc = new Point(l.x,l.y);
		expected_time = time;
		progress = 0.0;
	}


	public void update (double time_elapsed)
	{
		if (progress < 1.0)
		{
			progress += time_elapsed / expected_time;

			if (progress >= 1.0)
			{
				location.setLocation (new_loc);
			}
			else
			{
				//smooth movements
				double from = (1.0 + Math.cos ((Math.PI * progress))) / 2.0;
				double to = 1.0 - from;
				location.x = (int) (prev_loc.x * from + new_loc.x * to);
				location.y = (int) (prev_loc.y * from + new_loc.y * to);
			}
		}
	}
	
	public boolean moving()
	{
		return progress < 1.0;
	}
	
	public void complete_movement()
	{
		//next update the movement will be complete
		progress = 0.99999;
	}
}
