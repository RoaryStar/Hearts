import java.awt.*;

public abstract class Player
{
    protected int player_id;
    protected Pile hand;
    protected Deck taken_tricks;
    protected int points_this_hand;
    protected int points_this_game;
    protected Game game;
    private Card shift_temp [] = {null, null, null};

    public Player ()
    {
	player_id = -1;
	hand = null;
	taken_tricks = null;
	points_this_hand = -1;
	points_this_game = -1;
	game = null;
    }


    public Player (int id, Game g)
    {
	player_id = id;
	hand = new Pile (Globals.LOC_PLAYERS [id], Globals.OFF_PLAYERS [id], Globals.SEL_PLAYERS [id], false);
	taken_tricks = new Deck (Globals.LOC_TAKENS [id]);
	taken_tricks.set_layer(0);
	points_this_hand = 0;
	points_this_game = 0;
	game = g;
    }


    public abstract void shift_choose (int to);
    public boolean shift_chosen()
    {
	for (int i = 0; i < 13; ++i)
	{
	    if (hand.card(i).is_selected())
		return true;
	}
	return false;
    }
    
    
    public void shift_cards_setup ()
    {
	for (int i = 0, j = 0 ; j < 3 ; ++i)
	{
	    if (hand.card (i).is_selected ())
	    {
		shift_temp [j] = hand.card (i);
		shift_temp [j++].set_face(false);
		hand.remove_card (hand.card (i--));
	    }
	}
	hand.update_cards();
    }


    public void shift_cards_shift (int to)
    {
	Pile hto = game.get_player(to).get_hand ();
	for (int i = 0 ; i < 3 ; ++i)
	{
	    Point old_loc = shift_temp [i].get_loc ();
	    Point new_loc = hto.next_pos ();
	    hto.add_card (shift_temp [i]);
	    shift_temp [i].set_loc (old_loc);
	    shift_temp [i].move_to_location (new_loc, 0.500);
	    shift_temp [i].deselect();
	    shift_temp [i] = null;
	}
    }
    
    public void signal(int sig)
    {
    }

    public abstract Card next_card_trick (Card lead);
    public abstract boolean chosen_trick (Card lead);

    public Pile get_hand ()
    {
	return hand;
    }
    public Deck get_takens()
    {
	return taken_tricks;
    }


    public void play (Deck to, Card c)
    {
	hand.transfer_card (to, c);
    }
    
    
    public void handle_input()
    {
    }
    
    public void set_points_this_hand(int p)
    {
	points_this_hand = p;
    }
    public int get_points_this_hand()
    {
	return points_this_hand;
    }
    
    public void end_hand()
    {
	points_this_game += points_this_hand;
	points_this_hand = 0;
    }
    public int get_points()
    {
	return points_this_game;
    }
}
