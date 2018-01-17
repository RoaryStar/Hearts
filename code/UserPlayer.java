import java.awt.*;

public class UserPlayer extends Player
{
	protected UI user_interface;

	public UserPlayer()
	{
		super();
	}
	public UserPlayer(int id, Game g, UI ui)
	{
		super(id, g);
		user_interface = ui;
	}
	
	
	public void give_three_cards(int to)
	{
		throw new ArithmeticException("UserPlayer::give_three_cards not yet implemented!");
	}
	public Card next_card_trick()
	{
		throw new ArithmeticException("UserPlayer::next_card_trick not yet implemented!");
		//return null;
	}
}
