import java.awt.*;
import java.util.*;

public class Globals
{
		static Point CARD_DIM = new Point(100,100);
		
		static Image CARD_UP_IMG;
		static Image CARD_DOWN_IMG;
		
		final static int CLUBS = 0;
		final static int DIAMONDS = 1;
		final static int SPADES = 2;
		final static int HEARTS = 3;
		
		final static int NORTH = 0;
		final static int EAST = 1;
		final static int SOUTH = 2;
		final static int WEST = 3;
		
		final static Point LOC_PLAYER_NORTH = new Point(0, 0);
		final static Point LOC_PLAYER_EAST = new Point(100, 0);
		final static Point LOC_PLAYER_SOUTH = new Point(100, 100);
		final static Point LOC_PLAYER_WEST = new Point(0, 100);
		final static Point LOC_PLAYERS[] =
				{LOC_PLAYER_SOUTH, LOC_PLAYER_WEST,
				 LOC_PLAYER_NORTH, LOC_PLAYER_EAST};

		final static Point OFF_PLAYER_NORTH = new Point(0, 0);
		final static Point OFF_PLAYER_EAST = new Point(0, 0);
		final static Point OFF_PLAYER_SOUTH = new Point(0, 0);
		final static Point OFF_PLAYER_WEST = new Point(0, 0);
		final static Point OFF_PLAYERS[] =
				{OFF_PLAYER_SOUTH, OFF_PLAYER_WEST,
				 OFF_PLAYER_NORTH, OFF_PLAYER_EAST};
		
		final static Point LOC_TAKEN_NORTH = new Point(0, 0);
		final static Point LOC_TAKEN_EAST = new Point(0, 0);
		final static Point LOC_TAKEN_SOUTH = new Point(0, 0);
		final static Point LOC_TAKEN_WEST = new Point(0, 0);
		final static Point LOC_TAKENS[] =
				{LOC_TAKEN_SOUTH, LOC_TAKEN_WEST,
				 LOC_TAKEN_NORTH, LOC_TAKEN_EAST};
				 
		static Vector ERROR_LOG = new Vector();
		
		static Random rand = new Random();
		
		final static int STATE_BEGIN     = 0;
		final static int STATE_SHUFFLE   = 1;
		final static int STATE_DEAL      = 2;
		final static int STATE_FLUSH     = 3;
		final static int STATE_TRICK     = 4;
		final static int STATE_TRICK_END = 5;
		final static int STATE_HAND_END  = 6;
}
