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
	
	public void give_three_cards(int to)
	{
		throw new ArithmeticException("AIPlayer::give_three_cards not yet implemented!");
	}
	public Card next_card_trick()
	{
		throw new ArithmeticException("AIPlayer::next_card_trick not yet implemented!");
		//return null;
	}
}
