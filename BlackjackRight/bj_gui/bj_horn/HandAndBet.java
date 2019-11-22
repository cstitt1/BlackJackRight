package bj_horn;

import java.util.ArrayList;

/**
 * Represents a single player's hand and associated bet
 */
public class HandAndBet
{
    private double bet;
    private Hand hand;
    private double additionalBet;
    
    /**
     * Constructs a hand with the specified initial bet and no cards
     * @param bet the initial bet
     */
    public HandAndBet(double bet)
    {
        this.bet = bet;
        hand = new Hand();
        additionalBet = 0;
    }
    
    /**
     * Returns the initial bet associated with this hand (excluding double down)
     * @return the initial bet associated with this hand
     */
    public double getInitialBet()
    {
        return bet;
    }
    
    /**
     * Return the additional bet associated with this hand
     * @return the additional bet associated with this hand
     */
    public double getAdditionalBet()
    {
        return additionalBet;
    }
    
    /**
     * Returns the total bet associated with this hand (including double down)
     * @return the total bet associated with this hand
     */
    public double getTotalBet()
    {
        return bet + additionalBet;
    }
    
    /**
     * Returns a copy of the hand associated with this hand and bet
     * @return a copy of the hand associated with this hand and bet
     */
    public Hand getHand()
    {
        return new Hand(hand);
    }
    
    /**
     * Returns true if the player can double down on this hand, false otherwise
     * Note that this class does not enforce restrictions against double after
     * split.
     * @return true if the player can double down on this hand, false otherwise
     */
    public boolean canDoubleDown()
    {
        return hand.getNumCards() == 2 && additionalBet == 0;
    }
    
    /**
     * Doubles down for the specified additional amount
     * @param additionalBet the additional amount to be
     * @param card the card to add to this hand
     * Precondition: canDoubleDown() && additionalBet <= getBet()
     */
    public void doubleDown(double additionalBet, Card card)
    {
        if( ! canDoubleDown() )
            throw new IllegalStateException("this hand cannot be doubled down");
        
        if( ! (additionalBet <= getInitialBet()) )
            throw new IllegalArgumentException("additionalBet must be less than or equal to getInitialBet()");
        
        hand.addCard(card);
        this.additionalBet = additionalBet;
    }
    
    /**
     * Returns true if this hand contains 2 cards that can be split,
     * false otherwise. Note that this class does not enforce restrictions
     * against resplitting.
     * @return true if this hand contains 2 cards that can be split, false otherwise
     */
    public boolean canSplit()
    {
        if(hand.getNumCards() != 2 || additionalBet != 0)
            return false;
        
        ArrayList<Card> cards = hand.getCards();
        
        return cards.get(0).getValue() == cards.get(1).getValue() ||
        		(cards.get(0).getValue() >= 10 && cards.get(1).getValue() >= 10);
    }
    
    /**
     * Creates a new hand with 1 of the cards in this hand and the same bet.
     * Removes one of the cards from this hand.
     * @return the new hand
     * Precondition: canSplit()
     */
    public HandAndBet split()
    {
        if( ! canSplit() )
            throw new IllegalStateException("cannot split this hand");
        
        ArrayList<Card> cards = hand.getCards();
        
        hand = new Hand(cards.get(0));
        
        HandAndBet newHand = new HandAndBet(bet);
        newHand.hit(cards.get(1));
        return newHand;
    }
    
    /**
     * Returns true if this hand can accept another card, false otherwise
     * Note: This class does not enforce restrictions against hitting split aces
     * @return true if this hand can accept another card, false otherwise
     */
    public boolean canHit()
    {
        return additionalBet == 0 && hand.getValue() < 21;
    }
    
    /**
     * Returns true if this hand must accept another card (immediately
     * after creation and immediately after a split), false otherwise
     * @return true if this hand must accept another card, false otherwise
     */
    public boolean mustHit()
    {
        return hand.getNumCards() < 2;
    }
    
    /**
     * Adds the specified card to this hand
     * @param card the card to add
     * Precondition: canHit()
     */
    public void hit(Card card)
    {
        if( ! canHit() )
            throw new IllegalStateException("cannot hit this hand");
        
        hand.addCard(card);
    }
}
