import java.awt.*;

public class Ego
{
    //three node layers for the neural network
    protected double inputs[];
    protected double hiddens[];
    protected double utility[];

    //input information
    protected boolean in_hand[];
    protected boolean played[];
    protected boolean taken[];

    //output will be given in an ordering of cards by utility
    protected int order[];
    
    //we need to keep track of the player and game for updates
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
    
    //a sigmoid function for neuron activation
    private double sigmoid(double d)
    {
	double ed = Math.exp(d);
	return (ed - 1)/(ed + 1);
    }
    
    //updates all inputs that can be done at any time
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
    
    //updates the input vector
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
    
    //propagate and calculate utilities for every card
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
    
    //the "who played what" tracker needs to be reset every time a new hand is started
    public void new_hand()
    {
	for (int i = 0; i < 208; ++i)
	{
	    played[i] = false;
	}
    }
    
    //it also can't be determined whenever, and instead must be done whenever
    //a card is placed in play
    public void card_played(int player, int card)
    {
	played[player * 52 + card] = true;
    }
    
    //calculates utility and then orders from highest to lowest (insertion sort)
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
    
    //calculates utility and then orders from lowest to highest (insertion sort)
    public void choose_flush()
    {
	calc_utility();
	for (int i = 1; i < 52; ++i)
	    for (int j = i; j > 0; --j)
		if (utility[order[j]] < utility[order[j-1]])
		{
		    int t = order[j];
		    order[j] = order[j-1];
		    order[j-1] = t;
		}
    }
    
    //returns the order of cards to be used
    public int[] get_order()
    {
	return order;
    }
}
