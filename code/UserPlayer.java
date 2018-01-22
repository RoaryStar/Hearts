import java.awt.*;

public class UserPlayer extends Player
{
    //human players need to communicate through an interface
    protected UI user_interface;

    public UserPlayer ()
    {
	super ();
    }


    public UserPlayer (int id, Game g, UI ui)
    {
	super (id, g);
	user_interface = ui;
    }

    //so long as three cards have been chosen, a flush is considered chosen
    public boolean shift_chosen()
    {
	int num = 0;
	for (int i = 0; i < 13; ++i)
	{
	    if (hand.card(i).is_selected())
		++num;
	}
	return num == 3;
    }
    public void shift_choose (int to)
    {
	// this is empty, as everything is handled in shift_chosen(),
	// shift_cards_setup(), and shift_cards_to().
    }

    //so long as a valid card has been selected, a card is considered chosen;
    //invalid cards will be reset.
    public boolean chosen_trick(Card lead)
    {
	Card c;
	for (int i = 0; i < hand.num_cards(); ++i)
	{
	    if ((c = hand.card(i)).is_selected())
	    {
		if (lead == null)
		{
		    if (hand.num_cards() == 13)
		    {
			if (i == 0)
			    return true;
		    }
		    else
		    {
			if (c.get_suit() != Globals.HEARTS)
			    return true;
			else if (game.broken_hearts())
			    return true;
			else if (hand.num_cards() == hand.num_suit(Globals.HEARTS))
			    return true;
		    }
		}
		else if (c.get_suit() == lead.get_suit())
		    return true;
		else if (hand.num_suit(lead.get_suit()) == 0)
		    return true;
		hand.card(i).deselect();
		hand.update_cards();
		return false;
	    }
	}
	return false;
    }
    public Card next_card_trick (Card lead)
    {
	for (int i = 0; i < 13; ++i)
	{
	    if (hand.card(i).is_selected())
		return hand.card(i);
	}
	return null;
    }
    
    //this is how you can actually do things
    public void handle_input()
    {
	Point l = user_interface.get_click();
	if (l == null) return;
	
	//during a flush, you can both select and deselect
	if (game.get_state() == Globals.STATE_FLUSH)
	{
	    for (int i = hand.num_cards()-1; i >= 0; --i)
	    {
		if (hand.card(i).is_inside(l))
		{
		    if (hand.card(i).is_selected())
			hand.card(i).deselect();
		    else
			hand.card(i).select();
		    hand.update_cards();
		    break;
		}
	    }
	}
	//during a turn, you can only select
	else if (game.player_zero_turn())
	{
	    for (int i = hand.num_cards()-1; i >= 0; --i)
	    {
		if (hand.card(i).is_inside(l))
		{   
		    hand.card(i).select();
		    break;
		}
	    }
	}
    }
}
