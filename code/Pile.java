import java.awt.*;
import java.util.*;

public class Pile extends Deck
{
	protected Point offset;
	protected Point selset;
	protected boolean shown;

	public Pile ()
	{
		super ();
		offset = new Point (Globals.CARD_DIM.x / 3, 0);
		selset = new Point (0, Globals.CARD_DIM.y/3);
		shown = true;
	}


	public Pile (Point loc, Point off, Point sel, boolean sho)
	{
		super (loc);
		offset = off;
		shown = sho;
		selset = sel;
	}


	public void add_card (Card c)
	{
		if (!cards.contains (c))
		{
			cards.add (c);
			c.set_layer(layer + num_cards());
		}
		else
			Globals.ERROR_LOG.add ("Attempted to re-add card to pile!");
	}


	public void remove_card (Card c)
	{
		if (cards.contains (c))
		{
			cards.remove (c);
		}
		else
			Globals.ERROR_LOG.add ("Attempted to remove nonexistent card from deck!");
	}


	public void update_cards ()
	{
		for (int i = 0 ; i < num_cards () ; ++i)
		{
			Point des_loc = new Point(location.x + i * offset.x,
						    location.y + i * offset.y); 
			if (card(i).is_selected())
			    des_loc.translate(selset.x, selset.y);
			if (!card(i).get_loc().equals(des_loc))
			    card(i).move_to_location(des_loc, 0.500);
			card(i).set_face (shown);
			card(i).set_layer(layer + i);
		}
	}
	
	public Point next_pos()
	{
		return new Point(location.x + offset.x * cards.size(),
						location.y + offset.y * cards.size());
	}
	
	public void set_shown(boolean s)
	{
	    shown = s;
	}
}
