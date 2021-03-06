import java.awt.*;
import java.util.*;

public class Deck extends GameObject
{
	protected Vector cards;

	public Deck ()
	{
		super ();
		cards = new Vector();
	}
	public Deck (Point loc)
	{
		super (loc, new Point (1, 1), 0);
		cards = new Vector();
	}

	//creates a standard 52-card deck
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
	
	//shuffles the cards
	public void shuffle ()
	{
		Collections.shuffle (cards, Globals.rand);
		
		//keep everything in the right layer
		for (int i = 0; i < cards.size(); ++i)
		{
			((Card)cards.elementAt(i)).set_layer(layer + i);
		}
	}
	
	
	public void add_card (Card c)
	{
		//don't add the same card twice
		if (!cards.contains (c))
		{
			cards.add (c);
			
			//let there be something to show large numbers of cards in a deck
			c.set_loc (new Point (location.x + num_cards () / 10 * 2, location.y));
			
			c.set_layer(layer + num_cards());
		}
		else
			Globals.ERROR_LOG.add ("Attempted to re-add card to deck!");
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
	public void transfer_card (Deck d, Card c)
	{
		Point old_loc = c.get_loc();
		d.add_card (c);
		remove_card (c);
		Point new_loc = c.get_loc();
		c.set_loc (old_loc);
		c.move_to_location (new_loc, 0.5);
	}
	public void transfer_card (Pile p, Card c)
	{
		Point new_loc = p.next_pos();
		p.add_card (c);
		remove_card (c);
		c.set_loc (location);
		c.move_to_location (new_loc, 0.5);
	}


	public int num_cards ()
	{
		return cards.size ();
	}
	public Card card (int index)
	{
		//normally would return an exception
		if (index >= num_cards())
		    return null;
		return (Card) cards.elementAt (index);
	}
	public int index (Card c)
	{
		return cards.indexOf (c);
	}
	
	//sorts cards by layer (for drawing)
	public void sort_layer()
	{
		Collections.sort(cards, new Comparator() {
				public int compare(Object o1, Object o2) {
					return (((Card)o1).get_layer() < ((Card)o2).get_layer())? -1 :
							((((Card)o1).get_layer() == ((Card)o2).get_layer())? 0 : 1);
				}
			});
	}
	
	//sorts cards Clubs->Diamonds->Spades->Hearts, 2->A
	public void sort_standard()
	{
		Collections.sort(cards, new Comparator() {
				public int compare(Object o1, Object o2) {
					return (((Card)o1).get_suit() < ((Card)o2).get_suit())? -1 :
							((((Card)o1).get_suit() == ((Card)o2).get_suit())? 
							((((Card)o1).get_value()+12)%13 < (((Card)o2).get_value()+12)%13)? -1 :
							((((Card)o1).get_value() == ((Card)o2).get_value())? 0 : 1) : 1);
				}
			});
	}
	
	//counts the number of cards in a certain suit
	public int num_suit(int suit)
	{
	    int ret = 0;
	    for (int i = 0; i < num_cards(); ++i)
	    {
		if (card(i).get_suit() == suit) ++ret;
	    }
	    return ret;
	}
}
