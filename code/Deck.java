import java.awt.*;
import java.util.*;

public class Deck extends GameObject
{
    Vector cards;
    
    public Deck()
    {
	super();
    }
    public Deck(Point loc)
    {
	super(loc, new Point(1,1));
    }
    
    public void initialize_as_standard()
    {
	throw new ArithmeticException("Deck::initialize_as_standard not yet implemented!");
    }
    public void shuffle()
    {
	throw new ArithmeticException("Deck::shuffle not yet implemented!");
    }
    
    public void add_card(Card c)
    {
	if (!cards.contains(c))
	{
	    cards.add(c);
	    c.set_loc(location);
	}
	else
	    Globals.ERROR_LOG.add("Attempted to re-add card to deck!");
    }
    public void remove_card(Card c)
    {
	if (cards.contains(c))
	{
	    cards.remove(c);
	    c.set_loc(location);
	}
	else
	    Globals.ERROR_LOG.add("Attempted to remove nonexistent card from deck!");
    }
    public void transfer_card(Deck d, Card c)
    {
	d.add_card(c);
	remove_card(c);
	c.move_to_location(d.get_loc(), 500.0);
    }

    public int num_cards()
    {
	return cards.size();
    }
    
    public Card card(int index)
    {
	return (Card)cards.elementAt(index);
    }
    public int index(Card c)
    {
	return cards.indexOf(c);
    }
    
    public void draw(Graphics g)
    {
	throw new ArithmeticException("Deck::draw not yet implemented!");
    }
}
