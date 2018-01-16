import java.awt.*;

public class Game
{
	protected Player players[];
	protected Deck trick_cards[];
	protected Deck all_cards;
	protected Deck pool;
	protected int hand_num;
	protected int turn_num;
	protected int lead;
	protected boolean hearts_broken;
	protected UI user_interface;
	protected int state;
	protected int timer;

	Game (boolean human_player, UI ui)
	{
		state = Globals.STATE_BEGIN;

		pool = new Deck ();
		all_cards = new Deck ();
		all_cards.initialize_as_standard ();

		players = new Player [4];
		trick_cards = new Deck [4];

		if (human_player)
			players [0] = new UserPlayer (0, this, ui);
		else
			players [0] = new AIPlayer (0, this);
		trick_cards [0] = new Deck (Globals.LOC_TAKENS [0]);
		for (int i = 1 ; i < 4 ; ++i)
		{
			players [i] = new AIPlayer (i, this);
			trick_cards [i] = new Deck (Globals.LOC_TAKENS [i]);
			players [i].get_hand().set_layer(100);
		}
		hand_num = 0;
		turn_num = 0;
		lead = 0;
		hearts_broken = false;
		user_interface = ui;
	}


	void update_game (double time_elapsed)
	{
		for (int i = 0; i < 52; ++i)
		{
			all_cards.card(i).update(time_elapsed);
		}   
		switch (state)
		{
			case Globals.STATE_BEGIN:
				for (int i = 0; i < 52; ++i)
				{
					pool.add_card(all_cards.card(i));
				}
				state = Globals.STATE_SHUFFLE;
				break;
			case Globals.STATE_SHUFFLE:
				pool.shuffle ();

				state = Globals.STATE_DEAL;
				timer = 0;
				break;
			case Globals.STATE_DEAL:
				if (pool.num_cards () > 0)
				{
					pool.transfer_card (players [timer % 4].get_hand (), pool.card (pool.num_cards () - 1));
					timer += 1;
				}
				else
				{
					state = Globals.STATE_FLUSH;
					timer = 0;
				}

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


	void draw_game (Graphics g)
	{
		all_cards.sort_layer();
		for (int i = 0; i < 52; ++i)
		{
			all_cards.card(i).draw(g);
		}
	}
}
