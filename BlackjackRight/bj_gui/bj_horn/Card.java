package bj_horn;

/**
 * Represents a playing card
 */
public class Card
{
    private String suit;
    private int value;
    
    /**
     * Constructs a playing card with the specified suit and value
     * @param suit one of "D", "H", "S", "C"
     * @param value 1 - 13 corresponding to Ace, 2 - 10, Jack, Queen, King
     * @throws IllegalArgumentException if argument formats don't match
     */
    public Card(String suit, int value)
    {
        if(!suit.equals("D") && !suit.equals("H") &&
                !suit.equals("S") && !suit.equals("C"))
            throw new IllegalArgumentException("suit must be one of \"D\", \"H\", \"S\", \"C\"");
        
        if(! (1 <= value && value <= 13) )
            throw new IllegalArgumentException("value must be 1 - 13 (inclusive)");
        
        this.suit = suit;
        this.value = value;
    }
    
    /**
     * Returns this card's suit ("D", "H", "S" or "C")
     * @return this card's suit
     */
    public String getSuit()
    {
        return suit;
    }
    
    /**
     * Return this card's value (1 - 13 corresponding to Ace, 2 - 10, Jack, Queen, King)
     * @return this card's value
     */
    public int getValue()
    {
        return value;
    }
    
    /**
     * Returns this card with the 1 or 2 character value (A, 2-10, J, Q, K)
     * followed by the 1 character suit (D, H, S, C)
     * Examples: JD, 10H, AS, 9C
     */
    public String toString()
    {
        String name = "";

        switch (value)
        {
            case 1:
                name += "A";
                break;

            case 11:
                name += "J";
                break;

            case 12:
                name += "Q";
                break;

            case 13:
                name += "K";
                break;

            default:
                name += value;
        }

        name += suit;
        return name;
    }
}
