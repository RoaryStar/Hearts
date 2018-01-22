import java.awt.*;

public class Card extends Sprite
{
	//self-explanatory
	protected int suit;
	protected int value;
	protected boolean face_up;
	protected boolean selected;

	public Card ()
	{
		super (Globals.CARD_UP_IMG, 0, new Point (0, 0), Globals.CARD_DIM);
	}


	public Card (int s, int v, int layer, Point loc)
	{
		super (Globals.CARD_UP_IMG, new Point (Globals.CARD_DIM.x * v, Globals.CARD_DIM.y * s), layer, loc, Globals.CARD_DIM);
		suit = s;
		value = v;
		face_up = true;
		selected = false;
	}


	public boolean get_face ()
	{
		return face_up;
	}


	public void set_face (boolean faceup)
	{
		face_up = faceup;
		
		//also set the image being used to paint this
		if (face_up)
		    set_image(Globals.CARD_UP_IMG);
		else
		    set_image(Globals.CARD_DOWN_IMG);
	}


	public void flip ()
	{
		set_face(!face_up);
	}


	public int get_suit ()
	{
		return suit;
	}


	public int get_value ()
	{
		return value;
	}


	public int point_value ()
	{
		//hearts are worth one point...
		if (suit == Globals.HEARTS)
			return 1;
		//and the Queen of Spades 13
		if (suit == Globals.SPADES && value == 11)
			return 13;
		return 0;
	}
	
	public void select()
	{
	    selected = true;
	}
	public void deselect()
	{
	    selected = false;
	}
	
	public boolean is_selected()
	{
	    return selected;
	}
	
	public int get_id()
	{
	    return suit*13 + value;
	}
}
