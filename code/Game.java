import java.awt.*;

public class Game
{
	protected Player  players[];
	protected Deck    trick_cards[];
	protected Deck    all_cards;
	protected Deck    pool;
	protected int     hand_num;
	protected int     turn_num;
	protected int     lead;
	protected boolean hearts_broken;
	protected UI      user_interface;
	protected int     state;
	protected double  timer;

	Game (boolean human_player, UI ui)
	{
		state = Globals.STATE_BEGIN;
		timer = 0.0;
	
		pool = new Deck();
		all_cards = new Deck ();
		all_cards.initialize_as_standard();
	
		players = new Player [4];
		trick_cards = new Deck [4];
		
		if (human_player)
			players [0] = new UserPlayer (0, this, ui);
		else
			players [0] = new AIPlayer (0, this);
		trick_cards [0] = new Deck (Globals.LOC_TAKENS [0]);
		for (int i = 1; i < 4 ; ++i)
		{
			players [i] = new AIPlayer (i, this);
			trick_cards [i] = new Deck (Globals.LOC_TAKENS [i]);
		}
		hand_num = 0;
		turn_num = 0;
		lead = 0;
		hearts_broken = false;
		user_interface = ui;
	}
	
	void update_game(double time_elapsed)
	{
		switch(state)
		{
		case Globals.STATE_BEGIN:
			break;
		case Globals.STATE_SHUFFLE:
			pool.shuffle();
			
			state = Globals.STATE_DEAL;
			timer = 0.0;
			break;
		case Globals.STATE_DEAL:
			break;
		case Globals.STATE_FLUSH:
			break;
		case Globals.STATE_TRICK:
			break;
		case Globals.STATE_TRICK_END:
			break;
		case Globals.STATE_HAND_END:
			break;
		}
	}
	
	void draw_game()
	{
	}
}
