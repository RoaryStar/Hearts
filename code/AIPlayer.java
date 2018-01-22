import java.awt.*;

public class AIPlayer extends Player
{
    //the actual AI part; the class is more of an interface
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

    //choose three cards to flush.
    public void shift_choose (int to)
    {
	ego.choose_flush();
	int[] order = ego.get_order();
	for (int i = 0, j = 0; j < 3; ++i)
	{
	    //take the first three available in the order retrieved
	    //from the ego
	    Card c = game.get_all_cards().card(order[i]);
	    if (hand.index(c) >= 0)
	    {
		c.select ();         
		++j; 
	    }  
	}
	hand.update_cards ();
    }

    //a trick's always chosen for AI players as soon as it's asked for
    public boolean chosen_trick(Card lead)
    {
	return true;
    }
    public Card next_card_trick (Card lead)
    {
	//two of clubs must start a hand
	if (lead == null && game.cur_trick() == 0)
	    return hand.card(0);
	
	//small optimization to prevent constant checks later
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
	    //play the highest-utility available, valid card
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
    
    //simple way to give AI players AI-only commands from an implementation
    //of Player
    public void signal(int sig)
    {
	if (sig == -1)
	    ego.new_hand();
	else
	    ego.card_played(sig / 100, sig % 100);
    }
}
