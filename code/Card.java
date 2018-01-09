import java.awt.*;

public enum Suit
{
    CLUBS,
    DIAMONDS,
    SPADES,
    HEARTS
}
public class Card extends Sprite
{
    protected Suit suit;
    protected int value;
    protected boolean face_up;
    protected boolean selected;
    
    
}
