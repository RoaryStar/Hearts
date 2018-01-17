import java.awt.*;

public class AIPlayer extends Player
{
	
	public AIPlayer()
	{
		super();
	}
	public AIPlayer(int id, Game g)
	{
		super(id, g);
	}
	
	public void shift_choose(int to)
	{
		if(!shift_chosen())
		{
		    for (int i = 0; i < 3; ++i)
		    {
			int j = Globals.rand.nextInt(13);
			if (hand.card(j).is_selected())
			{
			    --i;
			}
			else
			{
			    hand.card(j).select();
			}
		    }
		    hand.update_cards();
		}
	}
	public Card next_card_trick()
	{
		throw new ArithmeticException("AIPlayer::next_card_trick not yet implemented!");
		//return null;
	}
}
