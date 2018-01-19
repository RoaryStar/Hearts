import java.awt.*;

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
	    players [0] = new UserPlayer (0, this, ui);
	else
	    players [0] = new AIPlayer (0, this);
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
	players[0].handle_input();
    
	boolean update_occured = false;
	for (int i = 0 ; i < 52 ; ++i)
	{   
	    if (all_cards.card(i).moving())
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
		    pool.card(i).set_face(false);
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
		    timer = 0;
		}
		break;
	    case Globals.STATE_DEAL:
		if (pool.num_cards () > 0)
		{
		    pool.transfer_card (players [timer % 4].get_hand (), pool.card (pool.num_cards () - 1));
		    timer += 1;
		}
		else
		{
		    hearts_broken = false;
		    lead = null;
		    
		    state = Globals.STATE_SORT;
		    timer = 0;
		}

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
		    for (int i = 0; i < 4; ++i)
		    {
			players[i].shift_choose((i+hand_num)%4);
		    }
		    ++part;
		    timer = 0;
		}
		else if (part == 1 && players[0].shift_chosen() && timer > 60)
		{                    
		    ++part;
		    timer = 0;
		}
		else if (part == 2)
		{
		    for (int i = 0; i < 4; ++i)
			players[i].shift_cards_setup();
		    for (int i = 0; i < 4; ++i)
			players[i].shift_cards_shift((i+hand_num)%4);
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
		    for (int i = 0; i < 4; ++i)
		    {
			players[i].get_hand().sort_standard();
			players[i].get_hand().update_cards();
		    }
		    ++part;
		    timer = 0;
		}
		else if (part == 5 && timer > 60)
		{
		    //2 of clubs starts
		    for (turn = 0; ; ++turn)
			if (players[turn].get_hand().card(0).get_value() == 1 &&
			    players[turn].get_hand().card(0).get_suit() == 0)
			    break;
		
		    state = Globals.STATE_TRICK;
		    part = 0;
		    timer = 0;
		}
		break;
	    case Globals.STATE_TRICK:
		++timer;
		if (players[turn].chosen_trick(lead) && timer > 31)
		{
		    trick_cards[turn].set_layer(200+part);
		    players[turn].play(trick_cards[turn],
			    players[turn].next_card_trick(lead));
		    players[turn].get_hand().update_cards();
		    trick_cards[turn].card(0).set_face(true);
		    if (trick_cards[turn].card(0).get_suit() == Globals.HEARTS)
			hearts_broken = true;
		    if (part == 0)
			lead = trick_cards[(turn-part+4)%4].card(0);
		    turn = (turn+1)%4;
		    ++part;
		    timer = 0;
		}
		if (part >= 4 && timer >= 30)
		{
		    state = Globals.STATE_TRICK_END;
		    timer = 0;
		}
		break;
	    case Globals.STATE_TRICK_END:
		if (timer == 30)
		{
		    lead = null;
		    for (int i = 0; i < 52; ++i)
			all_cards.card(i).deselect();
		    int highest = turn;
		    for (int i = 0; i < 4; ++i)
		    {
			if (trick_cards[i].card(0).get_suit() ==
			    trick_cards[turn].card(0).get_suit() &&
			    ((trick_cards[i].card(0).get_value()+12)%13) >
			    ((trick_cards[highest].card(0).get_value()+12)%13))
			    highest = i;
		    }
		    for (int i = 0; i < 4; ++i)
		    {
			trick_cards[i].transfer_card(
				players[highest].get_takens(),
				trick_cards[i].card(0));
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
		    for (int i = 0; i < 4; ++i)
		    {
			if (players[i].get_takens().num_cards() > 0)
			{
			    if (players[i].get_takens().card(0).point_value()>0)
			    {
				players[i].get_takens().transfer_card(
					players[i].get_hand(),
					players[i].get_takens().card(0));
			    }
			    else
			    {
				players[i].get_takens().card(0).set_face(false);
				players[i].get_takens().transfer_card(
					pool,
					players[i].get_takens().card(0));
			    }
			}
		    }
		    if (timer > 52)
		    {
			part = 1;
			timer = -1;
		    }
		}
		else if (part == 1 && timer == 0)
		{
		    for (int i = 0; i < 4; ++i)
		    {
			players[i].get_hand().set_shown(true);
			players[i].get_hand().sort_standard();
			players[i].get_hand().update_cards();
		    }
		}
		else if (part == 1 && timer > 300)
		{
		    for (int i = 0; i < 4; ++i)
		    {
			if (timer % 2 == 0 && players[i].get_hand().num_cards()>0)
			{
			    players[i].get_hand().card(0).set_face(false);
			    players[i].play(pool,
				    players[i].get_hand().card(0));
			}
		    }
		    if (timer > 330)
		    {
			for (int i = 1; i < 4; ++i)
			{
			    players[i].get_hand().set_shown(false);
			}
			part = 2;
			timer = -1;
		    }
		}
		else if (part == 2)
		{
		    ++hand_num;
		    state = Globals.STATE_SHUFFLE;
		}
		++timer;
		break;
	}
    }


    void draw_game (Graphics g)
    {
	all_cards.sort_layer ();
	for (int i = 0 ; i < 52 ; ++i)
	{
	    all_cards.card (i).draw (g);
	}
    }
    
    public Player get_player(int id)
    {
	return players[id];
    }
    
    public boolean broken_hearts()
    {
	return hearts_broken;
    }
    public int cur_trick()
    {
	return 13-tricks_left;
    }
    public int get_state()
    {
	return state;
    }
    public boolean player_zero_turn()
    {
	return state == Globals.STATE_TRICK && turn == 0 && timer > 31;
    }
}
