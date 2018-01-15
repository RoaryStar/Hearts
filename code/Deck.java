import java.awt.*;
import java.util.*;

public class Deck extends GameObject
{
	protected Vector cards;

	public Deck ()
	{
		super ();
	}
	public Deck (Point loc)
	{
		super (loc, new Point (1, 1), 0);
	}


	public void initialize_as_standard ()
	{
		cards.removeAllElements ();
		for (int i = 0 ; i < 4 ; ++i)
		{
			for (int j = 12 ; j >= 0 ; --j)
			{
				Card c = new Card (i, j, layer + i * 13 + j, location);
				add_card (c);
			}
		}
	}
	public void shuffle ()
	{
		Collections.shuffle (cards, Globals.rand);
	}
	public void add_card (Card c)
	{
		if (!cards.contains (c))
		{
			cards.add (c);
			c.set_loc (new Point (location.x + num_cards () / 10, location.y));
		}
		else
			Globals.ERROR_LOG.add ("Attempted to re-add card to deck!");
	}
	public void remove_card (Card c)
	{
		if (cards.contains (c))
		{
			cards.remove (c);
			c.set_loc (location);
		}
		else
			Globals.ERROR_LOG.add ("Attempted to remove nonexistent card from deck!");
	}
	public void transfer_card (Deck d, Card c)
	{
		d.add_card (c);
		remove_card (c);
		c.move_to_location (d.get_loc (), 500.0);
	}


	public int num_cards ()
	{
		return cards.size ();
	}
	public Card card (int index)
	{
		return (Card) cards.elementAt (index);
	}
	public int index (Card c)
	{
		return cards.indexOf (c);
	}

	public void draw (Graphics g)
	{
		throw new ArithmeticException ("Deck::draw not yet implemented!");
	}
}
