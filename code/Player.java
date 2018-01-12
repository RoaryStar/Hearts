import java.awt.*;

public class Player
{
    int player_id;
    Pile hand;
    Deck taken_tricks;
    int points_this_hand;
    int points_this_game;
    Game game;
    
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
    
    
}
