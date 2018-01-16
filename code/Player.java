import java.awt.*;

public abstract class Player
{
	protected int  player_id;
	protected Pile hand;
	protected Deck taken_tricks;
	protected int  points_this_hand;
	protected int  points_this_game;
	protected Game game;
	
	public Player()
	{
		player_id = -1;
		hand = null;
		taken_tricks = null;
		points_this_hand = -1;
		points_this_game = -1;
		game = null;
	}
	public Player(int id, Game g)
	{
		player_id = id;
		hand = new Pile(Globals.LOC_PLAYERS[id], Globals.OFF_PLAYERS[id], false);
		taken_tricks = new Deck(Globals.LOC_TAKENS[id]);
		points_this_hand = 0;
		points_this_game = 0;
		game = g;
	}
	
	public abstract void give_three_cards(int to);
	public abstract Card next_card_trick();
	
	public Deck get_hand()
	{
		return hand;
	}
	public void play(Card c, Deck to)
	{
		hand.transfer_card(to, c);
	}
}
