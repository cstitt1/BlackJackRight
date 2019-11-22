package bj_horn;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A multi-deck shoe that can deal cards and can be reset.
 *
 */
public class Shoe
{
    private final int decks;
    private ArrayList<Card> cards;
    
    /**
     * Constructs a shoe with the specified number of decks.
     * This shoe will be shuffled.
     * @param decks the number of decks
     */
    public Shoe(int decks)
    {
        this.decks = decks;
        reset();
    }
    
    /**
     * Removes and returns a card from this shoe
     * @return the card removed from this shoe.
     */
    public Card dealCard()
    {
        return cards.remove(cards.size() - 1);
    }
    
    /**
     * Returns the number of cards left in this shoe
     * @return the number of cards left in this shoe
     */
    public int cardsLeft()
    {
        return cards.size();
    }
    
    /**
     * Resets this shoe to contain all of its original cards.
     * This shoe will be shuffled.
     */
    public void reset()
    {
        cards = new ArrayList<Card>();
        
        for(String suit : new String[]{"H", "D", "S", "C"})
            for(int value = 1; value <= 13; value++)
                for(int x = 1; x <= decks; x++)
                    cards.add(new Card(suit, value));
        
        Collections.shuffle(cards);
    }
}
