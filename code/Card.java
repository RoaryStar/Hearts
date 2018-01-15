import java.awt.*;

public class Card extends Sprite
{
	protected int suit;
	protected int value;
	protected boolean face_up;
	protected boolean selected;
	
	public Card()
	{
	super(Globals.CARD_UP_IMG, 0, new Point(0,0), Globals.CARD_DIM);
	}
	public Card(int s, int v, int layer, Point loc)
	{
	super(Globals.CARD_UP_IMG, new Point(Globals.CARD_DIM.x * v, Globals.CARD_DIM.y * s),layer, loc, Globals.CARD_DIM);
	suit = s;
	value = v;
	face_up = true;
	selected = false;
	}
	
	public boolean get_face()
	{
	return face_up;
	}
	public void set_face(boolean faceup)
	{
	face_up = faceup;
	}
	public void flip()
	{
	face_up = !face_up;
	}
	
	public int get_suit()
	{
	return suit;
	}
	public int get_value()
	{
	return value;
	}
	public int point_value()
	{
	if (suit == Globals.HEARTS)
		return 1;
	if (suit == Globals.SPADES && value == 11)
		return 13;
	return 0;
	}
}
