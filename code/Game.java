import java.awt.*;
import java.util.*;

public class Game
{
    protected Player players[];
    protected Deck trick_cards[];
    protected Deck all_cards;
    protected Deck pool;
    protected Card lead;
    protected int hand_num;
    protected int turn_num;
    protected int tricks_left;
    protected int turn;
    protected boolean hearts_broken;
    protected UI user_interface;
    protected int state;
    protected int timer;
    protected int part;

    Game (boolean human_player, UI ui)
    {
	state = Globals.STATE_BEGIN;

	pool = new Deck (new Point (400 - Globals.CARD_DIM_H.x,
		    300 - Globals.CARD_DIM_H.y));
	all_cards = new Deck ();
	all_cards.initialize_as_standard ();

	players = new Player [4];
	trick_cards = new Deck [4];

	if (human_player)
	{
	    players [0] = new UserPlayer (0, this, ui);
	    Globals.NAMES[0] = "You";
	}
	else
	{    
	    players [0] = new AIPlayer (0, this);
	    Globals.NAMES[0] = "South";
	}
	trick_cards [0] = new Deck (Globals.LOC_TRICKS [0]);
	players [0].get_hand ().set_shown (true);
	for (int i = 1 ; i < 4 ; ++i)
	{
	    players [i] = new AIPlayer (i, this);
	    trick_cards [i] = new Deck (Globals.LOC_TRICKS [i]);
	    players [i].get_hand ().set_layer (100);
	    players [i].get_hand ().set_shown (false);
	}

	hand_num = 1;
	turn_num = 0;
	lead = null;
	hearts_broken = false;
	user_interface = ui;
    }


    void update_game (double time_elapsed)
    {
	players [0].handle_input ();

	boolean update_occured = false;
	for (int i = 0 ; i < 52 ; ++i)
	{
	    if (all_cards.card (i).moving ())
	    {
		update_occured = true;
		all_cards.card (i).update (time_elapsed);
	    }
	}
	switch (state)
	{
	    case Globals.STATE_BEGIN:
		for (int i = 0 ; i < 52 ; ++i)
		{
		    pool.add_card (all_cards.card (i));
		    pool.card (i).set_face (false);
		}
		state = Globals.STATE_SHUFFLE;
		timer = 0;
		break;
	    case Globals.STATE_SHUFFLE:
		++timer;

		if (timer > 60)
		{
		    pool.shuffle ();
		    tricks_left = 13;

		    state = Globals.STATE_DEAL;
		    part = 0;
		    timer = 0;
		}
		break;
	    case Globals.STATE_DEAL:   
		do
		{
		    if (pool.num_cards () > 0)
		    {
			pool.transfer_card (players [part % 4].get_hand (), pool.card (pool.num_cards () - 1));
			part += 1;
		    }
		    else
		    {
			hearts_broken = false;
			lead = null;
    
			state = Globals.STATE_SORT;
			timer = 0;
		    }
		} while (timer >= 600);
		break;
	    case Globals.STATE_SORT:
		if (!update_occured)
		{
		    players [0].get_hand ().update_cards ();
		    ++timer;
		    if (timer > 30)
		    {
			for (int i = 0 ; i < 4 ; ++i)
			{
			    players [i].get_hand ().sort_standard ();
			    players [i].get_hand ().update_cards ();
			}
			state = Globals.STATE_FLUSH;
			timer = 0;
			part = -1;
		    }
		}
		break;
	    case Globals.STATE_FLUSH:
		if (part == -1 && hand_num % 4 == 0)
		{
		    part = 5;
		    timer = 30;
		}
		++timer;
		if (part == -1 && timer > 60)
		{
		    ++part;
		    timer = 0;
		}
		else if (part == 0)
		{
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			players [i].shift_choose ((i + hand_num) % 4);
		    }
		    ++part;
		    timer = 0;
		}
		else if (part == 1 && players [0].shift_chosen () && timer > 60)
		{
		    ++part;
		    timer = 0;
		}
		else if (part == 2)
		{
		    for (int i = 0 ; i < 4 ; ++i)
			players [i].shift_cards_setup ();
		    for (int i = 0 ; i < 4 ; ++i)
			players [i].shift_cards_shift ((i + hand_num) % 4);
		    ++part;
		    timer = 0;
		}
		else if (part == 3 && timer > 60)
		{
		    ++part;
		    timer = 0;
		}
		else if (part == 4)
		{
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			players [i].get_hand ().sort_standard ();
			players [i].get_hand ().update_cards ();
		    }
		    ++part;
		    timer = 0;
		}
		else if (part == 5 && timer > 60)
		{
		    //2 of clubs starts
		    for (turn = 0 ;; ++turn)
			if (players [turn].get_hand ().card (0).get_value () == 1 &&
				players [turn].get_hand ().card (0).get_suit () == 0)
			    break;

		    state = Globals.STATE_TRICK;
		    part = 0;
		    timer = 0;
		}
		break;
	    case Globals.STATE_TRICK:
		++timer;
		if (part >= 4 && timer >= 30)
		{
		    state = Globals.STATE_TRICK_END;
		    timer = 0;
		}
		if (players [turn].chosen_trick (lead) && timer > 31)
		{
		    trick_cards [turn].set_layer (200 + part);
		    players [turn].play (trick_cards [turn],
			    players [turn].next_card_trick (lead));
		    players [turn].get_hand ().update_cards ();
		    trick_cards [turn].card (0).set_face (true);
		    if (!hearts_broken && trick_cards [turn].card (0).get_suit () == Globals.HEARTS)
		    {
			hearts_broken = true;
		    }
		    if (part == 0)
			lead = trick_cards [(turn - part + 4) % 4].card (0);
		    turn = (turn + 1) % 4;
		    ++part;
		    timer = 0;
		}
		break;
	    case Globals.STATE_TRICK_END:
		if (timer == 30)
		{
		    lead = null;
		    for (int i = 0 ; i < 52 ; ++i)
			all_cards.card (i).deselect ();
		    int highest = turn;
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			if (trick_cards [i].card (0).get_suit () ==
				trick_cards [turn].card (0).get_suit () &&
				((trick_cards [i].card (0).get_value () + 12) % 13) >
				((trick_cards [highest].card (0).get_value () + 12) % 13))
			    highest = i;
		    }
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			trick_cards [i].transfer_card (
				players [highest].get_takens (),
				trick_cards [i].card (0));
		    }
		    turn = highest;
		}
		++timer;
		if (timer > 60)
		{
		    --tricks_left;
		    if (tricks_left == 0)
			state = Globals.STATE_HAND_END;
		    else
			state = Globals.STATE_TRICK;
		    part = 0;
		    timer = 0;
		}
		break;
	    case Globals.STATE_HAND_END:
		if (part == 0)
		{
		    hearts_broken = false;
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			players [i].get_takens ().sort_standard ();
			for (int j = 0; j < players[i].get_takens().num_cards(); ++j)
			{
			    players[i].set_points_this_hand(
				    players[i].get_takens().card(j).point_value() +
				    players[i].get_points_this_hand());
			}
			if (players[i].get_points_this_hand() == 26)
			{
			    players[0].set_points_this_hand(26);
			    players[1].set_points_this_hand(26);
			    players[2].set_points_this_hand(26);
			    players[3].set_points_this_hand(26);
			    players[i].set_points_this_hand(0);
			}
		    }
		    ++part;
		}
		if (part == 1)
		{
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			if (players [i].get_takens ().num_cards () > 0)
			{
			    if (players [i].get_takens ().card (0).point_value () > 0)
			    {
				players [i].get_takens ().transfer_card (
					players [i].get_hand (),
					players [i].get_takens ().card (0));
			    }
			    else
			    {
				players [i].get_takens ().card (0).set_face (false);
				players [i].get_takens ().transfer_card (
					pool,
					players [i].get_takens ().card (0));
			    }
			}
		    }
		    if (timer > 52)
		    {
			++part;
			timer = -1;
		    }
		}
		else if (part == 2 && timer == 0)
		{
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			players [i].get_hand ().set_shown (true);
		    }
		}
		else if (part == 2 && timer > 300)
		{
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			if (timer % 2 == 0 && players [i].get_hand ().num_cards () > 0)
			{
			    players [i].get_hand ().card (0).set_face (false);
			    players [i].play (pool,
				    players [i].get_hand ().card (0));
			}
		    }
		    if (timer > 330)
		    {
			for (int i = 1 ; i < 4 ; ++i)
			{
			    players [i].get_hand ().set_shown (false);
			}
			++part;
			timer = -1;
		    }
		}
		else if (part == 3)
		{
		    players [0].end_hand ();
		    players [1].end_hand ();
		    players [2].end_hand ();
		    players [3].end_hand ();
		    
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			if (players[i].get_points() >= 100)
			{
			    state = Globals.STATE_GAME_END;
			    timer = 0;
			    part = 0;
			    return;
			}
		    }
		    ++hand_num;
		    state = Globals.STATE_SHUFFLE;
		}
		++timer;
		break;
	    case Globals.STATE_GAME_END:
		int last;
		if ((last = pool.num_cards()) > 0)
		{
		    double dir = (part += Globals.rand.nextInt(5)+3)*Math.PI*2.0/50.0;
		    pool.card(--last).flip();
		    pool.card(last).move_to_location(new Point(
			    (int)(Math.cos(dir)*6000) + 364,
			    (int)(Math.sin(dir)*6000) + 250), 2.0);
		    pool.remove_card(pool.card(last));
		}
		else if (user_interface.get_click() != null)
		    new_game();
		break;
	}
    }


    void draw_game (Graphics g)
    {
	if (hearts_broken)
	    Globals.hb_img.draw (g);

	all_cards.sort_layer ();
	for (int i = 0 ; i < 52 ; ++i)
	{
	    all_cards.card (i).draw (g);
	}
	
	g.setColor(Color.black);
	if (state != Globals.STATE_GAME_END)
	{
	    for (int i = 0; i < 4; ++i)
	    {
		g.drawString(""+players[i].get_points(), Globals.LOC_TEXTS[i].x, Globals.LOC_TEXTS[i].y);
	    }
	    if (state == Globals.STATE_HAND_END)
	    {
		for (int i = 0; i < 4; ++i)
		    g.drawString("+ " + players[i].get_points_this_hand(),
			    Globals.OFF_TEXTS[i].x, Globals.OFF_TEXTS[i].y);
	    }
	}
	else
	{
	    int order[] = {0, 1, 2, 3};
	    for (int z = 0; z < 4; ++z)
	    {
		for (int i = 0; i < 3; ++i)
		{
		    if (players[order[i]].get_points() > players[order[i+1]].get_points())
		    {
			int c = order[i];
			order[i] = order[i+1];
			order[i+1] = c;
		    }
		}
	    }
	    g.drawString("Game Over!", 300, 230);
	    for (int i = 0; i < 4; ++i)
	    {
		g.drawString("" + (i+1) + ". " + Globals.NAMES[order[i]], 300, 280+20*i);
		g.drawString("" + players[order[i]].get_points(), 450, 280+20*i); 
	    }
	    g.drawString("Click to play again.", 300, 400);
	}
    }


    public Player get_player (int id)
    {
	return players [id];
    }


    public boolean broken_hearts ()
    {
	return hearts_broken;
    }


    public int cur_trick ()
    {
	return 13 - tricks_left;
    }


    public int get_state ()
    {
	return state;
    }


    public boolean player_zero_turn ()
    {
	return state == Globals.STATE_TRICK && turn == 0 && timer > 31;
    }
    
    public void new_game()
    {
	state = Globals.STATE_BEGIN;
	
	for (int i = 0; i < 4; ++i)
	{
	    while (players[i].get_hand().num_cards() > 0)
		players[i].get_hand().remove_card(players[i].get_hand().card(0));
	    while (players[i].get_takens().num_cards() > 0)
		players[i].get_takens().remove_card(players[i].get_takens().card(0));
	    while (trick_cards[i].num_cards() > 0)
		trick_cards[i].remove_card(trick_cards[i].card(0));
	    
	    players[i].set_points_this_hand(-players[i].get_points());
	    players[i].end_hand();
	}
	for (int i = 0; i < 52; ++i)
	{
	    all_cards.card(i).complete_movement();
	}

	hand_num = 1;
	turn_num = 0;
	lead = null;
	hearts_broken = false;
    }
}
