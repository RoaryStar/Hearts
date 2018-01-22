import java.awt.*;

public class Ego
{
    protected double inputs[];
    protected double hiddens[];
    protected double utility[];

    protected boolean in_hand[];
    protected boolean played[];
    protected boolean taken[];

    protected int order[];
    
    protected Player player;
    protected Game game;
    
    public Ego(Player p, Game g)
    {
	player = p;
	game = g;
	
	inputs = new double[312];
	hiddens = new double[104];
	utility = new double[52];
	
	in_hand = new boolean[52];
	played = new boolean[208];
	taken = new boolean[52];
	
	order = new int[52];
	for (int i = 0; i < 52; ++i)
	    order[i] = i;
    }
    
    private double sigmoid(double d)
    {
	double ed = Math.exp(d);
	return (ed - 1)/(ed + 1);
    }
    
    private void update_knowns()
    {
	Card c;
	for (int i = 0; i < 52; ++i)
	{
	    in_hand[i] = false;
	    taken[i] = false;
	}
	for (int i = 0; i < player.get_hand().num_cards(); ++i)
	{
	    c = player.get_hand().card(i);
	    in_hand[c.get_id()] = true;
	}
	for (int i = 0; i < player.get_takens().num_cards(); ++i)
	{
	    c = player.get_takens().card(i);
	    taken[c.get_id()] = true;
	}
    }
    private void update_inputs()
    {
	update_knowns();
	
	for (int i = 0; i < 52; ++i)
	{
	    inputs[i] = in_hand[i] ? 1.0 : 0.0;
	    inputs[i + 260] = taken[i] ? 1.0 : 0.0;
	}
	for (int i = 0; i < 208; ++i)
	    inputs[i + 52] = played[i] ? 1.0 : 0.0;
    }
    private void calc_utility()
    {
	update_inputs();
	
	for (int i = 0; i < 104; ++i)
	{
	    hiddens[i] = 0;
	    for (int j = 0; j < 312; ++j)
	    {   
		hiddens[i] += Globals.weights[i * 312 + j] * inputs[j];
	    }
	    hiddens[i] = sigmoid(hiddens[i]);
	}
	for (int i = 0; i < 52; ++i)
	{
	    utility[i] = 0.0;
	    for (int j = 0; j < 104; ++j)
	    {
		utility[i] += Globals.weights[i * 104 + j + 32448] * hiddens[j];
	    }
	}
    }
    
    public void new_hand()
    {
	for (int i = 0; i < 208; ++i)
	{
	    played[i] = false;
	}
    }
    public void card_played(int player, int card)
    {
	played[player * 52 + card] = true;
    }
    public void choose_play()
    {
	calc_utility();
	for (int i = 1; i < 52; ++i)
	    for (int j = i; j > 0; --j)
		if (utility[order[j]] > utility[order[j-1]])
		{
		    int t = order[j];
		    order[j] = order[j-1];
		    order[j-1] = t;
		}
    }
    public void choose_flush()
    {
	calc_utility();
	for (int i = 1; i < 52; ++i)
	    for (int j = i; j > 0; --j)
		if (utility[order[j]] > utility[order[j-1]])
		{
		    int t = order[j];
		    order[j] = order[j-1];
		    order[j-1] = t;
		}
    }
    
    public int[] get_order()
    {
	return order;
    }
}
