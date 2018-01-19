import java.awt.*;

public class AIPlayer extends Player
{

    public AIPlayer ()
    {
	super ();
    }


    public AIPlayer (int id, Game g)
    {
	super (id, g);
    }


    public void shift_choose (int to)
    {
	if (!shift_chosen ())
	{
	    for (int i = 0 ; i < 3 ; ++i)
	    {
		int j = Globals.rand.nextInt (13);
		if (hand.card (j).is_selected ())
		{
		    --i;
		}
		else
		{
		    hand.card (j).select ();
		}
	    }
	    hand.update_cards ();
	}
    }

    public boolean chosen_trick(Card lead)
    {
	return true;
    }
    public Card next_card_trick (Card lead)
    {
	if (lead == null)
	{
	    if (game.cur_trick() == 0)
	    {
		return hand.card(0);
	    }
	    if (game.broken_hearts() || hand.num_cards() == hand.num_suit(Globals.HEARTS))
	    {
		return hand.card(Globals.rand.nextInt(hand.num_cards()));
	    }
	    else
	    {
		return hand.card(Globals.rand.nextInt(hand.num_cards()-hand.num_suit(Globals.HEARTS)));
	    }
	}
	else
	{
	    int num = hand.num_suit(lead.get_suit());
	    if (num > 0)
	    {
		int begin = 0;
		for (int i = 0; i < lead.get_suit(); ++i)
		    begin += hand.num_suit(i);
		return hand.card (Globals.rand.nextInt(num) + begin);
	    }
	    else
		return hand.card (Globals.rand.nextInt (hand.num_cards ()));
	}
    }
}
