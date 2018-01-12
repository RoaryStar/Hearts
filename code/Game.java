import java.awt.*;

public class Game
{
    Player players[];
    Deck trick_cards[];
    int hand_num;
    int turn_num;
    int lead;
    boolean hearts_broken;
    
    Game()
    {
	players = new Player[4];
	trick_cards = new Deck[4];
	for (int i = 0; i < 4; ++i)
	{
	    players[i] = new Player(i, this);
	    trick_cards[i] = new Deck(Globals.LOC_TAKENS[i]);
	}
	hand_num = 0;
	turn_num = 0;
	lead = 0;
	hearts_broken = false;
    }
}
