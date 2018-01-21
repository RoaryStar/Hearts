import java.awt.*;
import java.util.*;

public class Globals
{
	static Point CARD_DIM = new Point (100, 100);
	static Point CARD_DIM_H = new Point (50, 50);

	static Image CARD_UP_IMG;
	static Image CARD_DOWN_IMG;
	static Image HEARTS_BROKEN_IMG;
	static Sprite hb_img;

	final static int CLUBS = 0;
	final static int DIAMONDS = 1;
	final static int SPADES = 2;
	final static int HEARTS = 3;

	final static int NORTH = 0;
	final static int EAST = 1;
	final static int SOUTH = 2;
	final static int WEST = 3;

	final static Point LOC_PLAYER_NORTH = new Point (544, 0);
	final static Point LOC_PLAYER_EAST = new Point (728, 370);
	final static Point LOC_PLAYER_SOUTH = new Point (184, 500);
	final static Point LOC_PLAYER_WEST = new Point (0, 130);
	final static Point LOC_PLAYERS[] =
		{LOC_PLAYER_SOUTH, LOC_PLAYER_WEST,
		LOC_PLAYER_NORTH, LOC_PLAYER_EAST};

	final static Point OFF_PLAYER_NORTH = new Point (-30, 0);
	final static Point OFF_PLAYER_EAST = new Point (0, -20);
	final static Point OFF_PLAYER_SOUTH = new Point (30, 0);
	final static Point OFF_PLAYER_WEST = new Point (0, 20);
	final static Point OFF_PLAYERS[] =
		{OFF_PLAYER_SOUTH, OFF_PLAYER_WEST,
		OFF_PLAYER_NORTH, OFF_PLAYER_EAST};
		
	final static Point SEL_PLAYER_NORTH = new Point (0, 30);
	final static Point SEL_PLAYER_EAST = new Point (-20, 0);
	final static Point SEL_PLAYER_SOUTH = new Point (0, -30);
	final static Point SEL_PLAYER_WEST = new Point (20, 0);
	final static Point SEL_PLAYERS[] =
		{SEL_PLAYER_SOUTH, SEL_PLAYER_WEST,
		SEL_PLAYER_NORTH, SEL_PLAYER_EAST};

	final static Point LOC_TAKEN_NORTH = new Point (364, -110);
	final static Point LOC_TAKEN_EAST = new Point (810, 250);
	final static Point LOC_TAKEN_SOUTH = new Point (364, 610);
	final static Point LOC_TAKEN_WEST = new Point (-82, 250);
	final static Point LOC_TAKENS[] =
		{LOC_TAKEN_SOUTH, LOC_TAKEN_WEST,
		LOC_TAKEN_NORTH, LOC_TAKEN_EAST};
		
	final static Point LOC_TRICK_NORTH = new Point (364, 175);
	final static Point LOC_TRICK_EAST = new Point (418, 250);
	final static Point LOC_TRICK_SOUTH = new Point (364, 325);
	final static Point LOC_TRICK_WEST = new Point (310, 250);
	final static Point LOC_TRICKS[] =
		{LOC_TRICK_SOUTH, LOC_TRICK_WEST,
		LOC_TRICK_NORTH, LOC_TRICK_EAST};
		
	final static Point LOC_TEXT_NORTH = new Point (554, 140);
	final static Point LOC_TEXT_EAST = new Point (738, 505);
	final static Point LOC_TEXT_SOUTH = new Point (194, 480);
	final static Point LOC_TEXT_WEST = new Point (10, 110);
	final static Point LOC_TEXTS[] =
		{LOC_TEXT_SOUTH, LOC_TEXT_WEST,
		LOC_TEXT_NORTH, LOC_TEXT_EAST};
		
	final static Point OFF_TEXT_NORTH = new Point (604, 140);
	final static Point OFF_TEXT_EAST = new Point (738, 525);
	final static Point OFF_TEXT_SOUTH = new Point (244, 480);
	final static Point OFF_TEXT_WEST = new Point (60, 110);
	final static Point OFF_TEXTS[] =
		{OFF_TEXT_SOUTH, OFF_TEXT_WEST,
		OFF_TEXT_NORTH, OFF_TEXT_EAST};
	
	static String[] NAMES = {"South", "West", "North", "East"};

	static Vector ERROR_LOG = new Vector ();

	static Random rand = new Random ();

	final static int STATE_BEGIN = 0;
	final static int STATE_SHUFFLE = 1;
	final static int STATE_DEAL = 2;
	final static int STATE_SORT = 3;
	final static int STATE_FLUSH = 4;
	final static int STATE_TRICK = 5;
	final static int STATE_TRICK_END = 6;
	final static int STATE_HAND_END = 7;
	final static int STATE_GAME_END = 8;
}
