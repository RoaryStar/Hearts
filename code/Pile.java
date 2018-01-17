import java.awt.*;
import java.util.*;

public class Pile extends Deck
{
	protected Point offset;
	protected boolean shown;

	public Pile ()
	{
		super ();
		offset = new Point (Globals.CARD_DIM.x / 3, 0);
		shown = true;
	}


	public Pile (Point loc, Point off, boolean sho)
	{
		super (loc);
		offset = off;
		shown = sho;
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
			((Card) cards.elementAt (i)).move_to_location (
							new Point (location.x + i * offset.x,
								location.y + i * offset.y),
							0.500);
			((Card) cards.elementAt (i)).set_face (shown);
			((Card) cards.elementAt (i)).set_layer(layer + i);
		}
	}
	
	public Point next_pos()
	{
		return new Point(location.x + offset.x * cards.size(),
						location.y + offset.y * cards.size());
	}
}
