import java.awt.*;

public class AIPlayer extends Player
{
    Ego ego;
    
    public AIPlayer ()
    {
	super ();
	ego = null;
    }


    public AIPlayer (int id, Game g)
    {
	super (id, g);
	ego = new Ego(this, g);
    }


    public void shift_choose (int to)
    {
	ego.choose_flush();
	int[] order = ego.get_order();
	for (int i = 0, j = 0; j < 3; ++i)
	{
	    Card c = game.get_all_cards().card(order[i]);
	    if (hand.index(c) >= 0)
	    {
		c.select ();         
		++j; 
	    }  
	}
	hand.update_cards ();
    }

    public boolean chosen_trick(Card lead)
    {
	return true;
    }
    public Card next_card_trick (Card lead)
    {
	if (lead == null && game.cur_trick() == 0)
	    return hand.card(0);
	    
	int allowed_type;
	
	if (lead == null)
	{
	    if (game.broken_hearts() || hand.num_cards() == hand.num_suit(Globals.HEARTS))
		allowed_type = Globals.TS_ANY;
	    else
		allowed_type = Globals.TS_LEAD;            
	}
	else
	{
	    if (hand.num_suit(lead.get_suit()) == 0)
		allowed_type = Globals.TS_ANY;
	    else
		allowed_type = Globals.TS_FOLLOW;
	}
	 
	
	ego.choose_play();
	int order[] = ego.get_order();
	for (int i = 0; i < 52; ++i)
	{
	    Card c = game.get_all_cards().card(order[i]);
	    if (hand.index(c) >= 0)
	    {   
		if (allowed_type == Globals.TS_ANY)
		{
		    return c;
		}
		else if (allowed_type == Globals.TS_LEAD)
		{
		    if (c.get_suit() != Globals.HEARTS)
		    {
			return c;
		    }
		}
		else if (allowed_type == Globals.TS_FOLLOW)
		{
		    if (c.get_suit() == lead.get_suit())
		    {
			return c;
		    }
		}
	    }
	}
	return null;
    }
    
    public void signal(int sig)
    {
	if (sig == -1)
	    ego.new_hand();
	else
	    ego.card_played(sig / 100, sig % 100);
    }
}
