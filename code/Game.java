import java.awt.*;
import java.util.*;

public class Game
{
    protected Player players[];         //the four players
    protected Deck trick_cards[];       //the four cards in a trick
    protected Deck all_cards;           //keeps track of all cards
    protected Deck pool;                //centre pile for shuffling, dealing
    protected Card lead;                //keeps track of lead card in trick
    protected int hand_num;             //current hand (start with zero)
    protected int tricks_left;          //keep track of progress through a hand
    protected int turn;                 //current turn number (player)
    protected boolean hearts_broken;
    protected UI user_interface;        //keep track of the UI handler
    protected int state;                //stage of the game
    protected int part;                 //substate
    protected int timer;                //substate of substate

    // Initalizes a new game. A UI must be passed in.
    Game (boolean human_player, UI ui)
    {
	state = Globals.STATE_BEGIN;

	//instantiate the pool
	pool = new Deck (new Point (400 - Globals.CARD_DIM_H.x,
		    300 - Globals.CARD_DIM_H.y));
	
	//instantiate the tracker, which starts out with all cards
	all_cards = new Deck ();
	all_cards.initialize_as_standard ();

	//instantiate players and decks used to keep track of the trick
	players = new Player [4];
	trick_cards = new Deck [4];

	//usage of classes change depending on whether or not one is playing
	//since this is a separate instantiation, all initializations must
	//be separate.
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
	
	//player zero's (you/south) hand is always visible
	players [0].get_hand ().set_shown (true);
	
	//instatiations for the other three players
	for (int i = 1 ; i < 4 ; ++i)
	{
	    players [i] = new AIPlayer (i, this);
	    trick_cards [i] = new Deck (Globals.LOC_TRICKS [i]);
	    players [i].get_hand ().set_layer (100);
	    players [i].get_hand ().set_shown (false);
	}
	
	//initialize variables so nothing stupid happens
	hand_num = 1;
	lead = null;
	hearts_broken = false;
	user_interface = ui;
    }
 
    // Updates the game. Is called every tick (16 milliseconds).
    void update_game (double time_elapsed)
    {
	//if the player did anything, we need to check for that
	players [0].handle_input ();

	//update all the cards since they can move; some stages
	//need to wait for all cards to finish movement before
	//continuing, so this is where we keep track of it.
	boolean update_occured = false;
	for (int i = 0 ; i < 52 ; ++i)
	{
	    if (all_cards.card (i).moving ())
	    {
		update_occured = true;
		all_cards.card (i).update (time_elapsed);
	    }
	}
	
	//each stage of the game has its own handling.
	switch (state)
	{
	    // A new game has begun.
	    case Globals.STATE_BEGIN:
		//populate the pool
		for (int i = 0 ; i < 52 ; ++i)
		{
		    pool.add_card (all_cards.card (i));
		    pool.card (i).set_face (false);
		}
		state = Globals.STATE_SHUFFLE;
		timer = 0;
		break;
	    
	    // A new hand has begun.
	    case Globals.STATE_SHUFFLE:
		++timer;
		
		//wait one second before doing the shuffle and moving on.
		if (timer > 60)
		{
		    pool.shuffle ();
		    tricks_left = 13;

		    state = Globals.STATE_DEAL;
		    part = 0;
		    timer = 0;
		}
		break;
	    
	    // Deal the cards to the four players.
	    case Globals.STATE_DEAL:   
		//for a cool animation, only deal one card every tick.
		if (pool.num_cards () > 0)
		{
		    //use part to keep track of which player to deal to
		    pool.transfer_card (players [part % 4].get_hand (), pool.card (pool.num_cards () - 1));
		    ++part;
		}
		else
		{
		    hearts_broken = false;
		    lead = null;

		    state = Globals.STATE_SORT;
		    timer = 0;
		}
		break;
	    
	    // The hands have been dealed; now they have to be sorted.
	    case Globals.STATE_SORT:
		//wait for all cards to have settled down, or it'll look
		//really stupid.
		if (!update_occured)
		{
		    //cards are initially dealt face down; this flips all
		    //the cards in South's hand.
		    players [0].get_hand ().update_cards ();
		    
		    ++timer;
		    
		    //wait half a second before the sort
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
		
	    // At the beginning of a hand N, players choose three cards to pass
	    // to the player N places to their left.
	    case Globals.STATE_FLUSH:
		//if it's a hand that nets no change, don't bother.
		if (part == -1 && hand_num % 4 == 0)
		{
		    part = 5;
		    timer = 30;
		}
		++timer;
		
		//ensure that the sorting is done
		if (part == -1 && timer > 60)
		{
		    ++part;
		    timer = 0;
		}
		//all AI players choose their three cards; You does
		//nothing at this stage
		else if (part == 0)
		{
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			players [i].shift_choose ((i + hand_num) % 4);
		    }
		    ++part;
		    timer = 0;
		}
		//wait a second if it's all AI players; otherwise, wait
		//until You have chosen their three cards.
		else if (part == 1 && players [0].shift_chosen () && timer > 60)
		{
		    ++part;
		    timer = 0;
		}
		//nicely animate the cards switching hands
		else if (part == 2)
		{
		    for (int i = 0 ; i < 4 ; ++i)
			players [i].shift_cards_setup ();
		    for (int i = 0 ; i < 4 ; ++i)
			players [i].shift_cards_shift ((i + hand_num) % 4);
		    ++part;
		    timer = 0;
		}
		//wait for the animation to be over
		else if (part == 3 && timer > 60)
		{
		    ++part;
		    timer = 0;
		}
		//sort the hands again
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
		//we're ready to move on to the first trick.
		else if (part == 5 && timer > 60)
		{
		    //2 of clubs starts the first trick
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
		//checks if all four players have played a card in the trick
		//to see if we should continue.
		if (part >= 4 && timer >= 30)
		{
		    state = Globals.STATE_TRICK_END;
		    timer = 0;
		}
		//every half-second a new player can play a card for the trick.
		if (players [turn].chosen_trick (lead) && timer > 31)
		{
		    trick_cards [turn].set_layer (200 + part);
		    
		    //play a card
		    Card tc = players [turn].next_card_trick (lead);
		    players [turn].play (trick_cards [turn], tc);
			    
		    //close the gap in the hand
		    players [turn].get_hand ().update_cards ();
		    
		    //let everyone see what you played, and signal to AI players
		    trick_cards [turn].card (0).set_face (true);
		    for (int i = 0; i < 4; ++i)
			players[i].signal(tc.get_id() + 100 * turn);
		    
		    //check if hearts was broken
		    if (trick_cards [turn].card (0).get_suit () == Globals.HEARTS)
			hearts_broken = true;
		    
		    //if this was the lead in a trick, track it
		    if (part == 0)
			lead = trick_cards [(turn - part + 4) % 4].card (0);
			
		    //next player
		    turn = (turn + 1) % 4;
		    ++part;
		    timer = 0;
		}
		break;
		
	    // A trick has been played; have the appropriate player take the
	    // trick and start the next one.
	    case Globals.STATE_TRICK_END:
		//wait for cards to settle
		if (timer == 30)
		{
		    //deselect everything to prevent glitches futher
		    //down the road
		    for (int i = 0 ; i < 52 ; ++i)
			all_cards.card (i).deselect ();
			
		    //find the player who played the highest card in the lead 
		    //suit of the trick
		    int highest = turn;
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			if (trick_cards [i].card (0).get_suit () == lead.get_suit () &&
				((trick_cards [i].card (0).get_value () + 12) % 13) >
				((trick_cards [highest].card (0).get_value () + 12) % 13))
			    highest = i;
		    }
		    
		    //we no longer need to track this
		    lead = null;
		    
		    //transfer the trick to the appropriate owner
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			trick_cards [i].transfer_card (
				players [highest].get_takens (),
				trick_cards [i].card (0));
		    }
		    
		    //that player starts the next trick
		    turn = highest;
		}
		++timer;
		
		//after a half-second, move on
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
		
	    // The last trick of a hand has just been played; tally points.
	    case Globals.STATE_HAND_END:
		//tallys points.
		if (part == 0)
		{
		    //reset the AI's counting
		    for (int i = 0; i < 4; ++i)
			players[i].signal(-1);
		    
		    //reset this for cosmetic effect
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
			
			//special case for if a player shot the moon
			if (players[i].get_points_this_hand() == 26)
			{
			    players[0].set_points_this_hand(26);
			    players[1].set_points_this_hand(26);
			    players[2].set_points_this_hand(26);
			    players[3].set_points_this_hand(26);
			    players[i].set_points_this_hand(0);
			    break;
			}
		    }
		    ++part;
		}
		//display point cards
		if (part == 1)
		{
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			//one a tick
			if (players [i].get_takens ().num_cards () > 0)
			{
			    //point cards dealt to hand; non-point cards
			    //sent back to pool.
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
		//make sure the hands can be seen
		else if (part == 2 && timer == 0)
		{
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			players [i].get_hand ().set_shown (true);
		    }
		}
		//after five seconds, clean everything up
		else if (part == 2 && timer > 300)
		{
		    //move taken points back to pool
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			if (timer % 2 == 0 && players [i].get_hand ().num_cards () > 0)
			{
			    players [i].get_hand ().card (0).set_face (false);
			    players [i].play (pool,
				    players [i].get_hand ().card (0));
			}
		    }
		    //after half a second, move on.
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
		//commit points and move on
		else if (part == 3)
		{
		    players [0].end_hand ();
		    players [1].end_hand ();
		    players [2].end_hand ();
		    players [3].end_hand ();
		    
		    for (int i = 0 ; i < 4 ; ++i)
		    {
			//the game ends when at least one player has
			//at least one hundred points
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
		//make the cards in the pool fly away
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
		//once that's done, let the user play another game
		else if (user_interface.get_click() != null)
		    new_game();
		break;
	}
    }

    // Draws the game. Called every tick and every time the game needs updating.
    void draw_game (Graphics g)
    {
	//show if hearts has been broken in the background
	if (hearts_broken)
	    Globals.hb_img.draw (g);

	//draw cards starting from the lowest-layer
	all_cards.sort_layer ();
	for (int i = 0 ; i < 52 ; ++i)
	{
	    all_cards.card (i).draw (g);
	}
	
	//reset so that processing for the AI is easier
	all_cards.sort_standard();
	
	g.setColor(Color.black);
	if (state != Globals.STATE_GAME_END)
	{
	    //display points each player has yet won in previous hands
	    for (int i = 0; i < 4; ++i)
	    {
		g.drawString(""+players[i].get_points(), Globals.LOC_TEXTS[i].x, Globals.LOC_TEXTS[i].y);
	    }
	    //while showing points won in the last hand, include the change
	    if (state == Globals.STATE_HAND_END)
	    {
		for (int i = 0; i < 4; ++i)
		    g.drawString("+ " + players[i].get_points_this_hand(),
			    Globals.OFF_TEXTS[i].x, Globals.OFF_TEXTS[i].y);
	    }
	}
	else
	{
	    //rank the players (lowest score is higher)
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
	    
	    //draw the leaderboard
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
	
	//clear everything
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
	//everything, including movement that might not yet have been completed
	for (int i = 0; i < 52; ++i)
	{
	    all_cards.card(i).complete_movement();
	}

	hand_num = 1;
	lead = null;
	hearts_broken = false;
    }
    
    public Deck get_all_cards()
    {
	return all_cards;
    }
}
