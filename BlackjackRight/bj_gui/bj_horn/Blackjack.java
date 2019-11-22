package bj_horn;

import java.util.ArrayList;

/**
 * The Blackjack class allows a single player to play a game of blackjack.
 * The class tracks the player's bankroll but makes no attempt to prevent
 * a negative bankroll.
 *
 */
public class Blackjack
{
	private static final int DECKS = 6, CARDS_PER_DECK = 52;
	private static final double SHOE_PENETRATION = 0.75;
	
	private static final int MAX_HANDS = 4;
	
    private Shoe shoe;
    
    private double playersMoney;
    
    private ArrayList<HandAndBet> playersHands;
    private int currentHand;
    
    private Hand dealersHand;
    private boolean dealerPlayed;
    
    /**
     * Constructs a blackjack object that is ready to play.
     * @param playersMoney the player's starting bankroll (all values, including 0 and negative values, are permitted)
     */
    public Blackjack(double playersMoney)
    {
        this.playersMoney = playersMoney;
        this.shoe = new Shoe(DECKS);
        
        reset();
    }
    
    /**
     * Resets for another round, including reseting shoe if necessary
     */
    private void reset()
    {
        playersHands = new ArrayList<HandAndBet>();
        currentHand = -1;
        
        this.dealersHand = null;
        this.dealerPlayed = false;
        
        if(shoe.cardsLeft() < DECKS * CARDS_PER_DECK * (1 - SHOE_PENETRATION))
            shoe.reset();
    }
    
    /**
     * Returns the player's money (can be negative)
     * @return the player's money
     */
    public double getPlayersMoney()
    {
        return this.playersMoney;
    }
    
    /**
     * Returns the number of player hands
     * (0 if the player has not yet placed an initial bet)
     * @return the number of player hands
     */
    public int getNumHands()
    {
        return playersHands.size();
    }
    
    /**
     * Returns the index of the current hand for which a decision needs to be made or
     * -1 if there is no hand with a decision to be made
     * (0 up to but excluding getNumHands())
     * @return the index of the current hand or -1
     * 
     * Precondition: getNumHands() > 0
     */
    public int getCurrentHand()
    {
        if( ! (getNumHands() > 0) )
            throw new IllegalStateException("player has not yet placed an initial bet");
        
        return currentHand;
    }
    
    /**
     * Returns true if there is a decision to make for the player's current hand, false otherwise
     * @return true if there is a decision to make for the player's current hand, false otherwise
     * 
     * Precondition: currentHand >= 0
     */
    private boolean decisionIsRequiredForCurrentHand()
    {
        if( ! (currentHand >= 0) )
            throw new IllegalStateException("currentHand must be >= 0 to check for decision");
        
        return canHit() || canDoubleDown() || canSplit();
    }
    
    /**
     * Advances currentHand to the next hand for which a decision
     * needs to be made or -1 if there is no such hand.
     * 
     * Precondition: currentHand >= 0
     */
    private void advanceToNextHandWithDecision()
    {
        if( ! (currentHand >= 0) )
            throw new IllegalStateException("currentHand must be >= 0 to advance to next hand");
        
        currentHand++;
        while(currentHand < getNumHands() &&
                ! decisionIsRequiredForCurrentHand() )
            currentHand++;
        
        if(currentHand == getNumHands())
            currentHand = -1;
    }
    
    private void validateInitialBet()
    {
        if( ! (getNumHands() > 0) )
            throw new IllegalStateException("player has not yet placed an initial bet");
    }
    
    /**
     * Throws an exception with an appropriate message if the player has not yet
     * placed an initial bet or if index is not valid.
     * @param index the index to validate
     */
    private void validateInitialBetAndIndex(int index)
    {
        validateInitialBet();
        
        if( ! (0 <= index && index < getNumHands() ) )
            throw new IllegalArgumentException("index must be: >= 0 and < getNumHands()");
    }
    
    /**
     * Returns the player's total bet (initial + additional) for the specified hand
     * @param index the index of the hand
     * @return the player's total bet for the hand
     * 
     * Precondition: getNumHands() > 0 && 0 <= index && index < getNumHands()
     */
    public double getTotalBet(int index)
    {
        validateInitialBetAndIndex(index);

    		return playersHands.get(index).getTotalBet();
    }
    
    /**
     * Returns the player's additional bet for the specified hand
     * @param index the index of the hand
     * @return the player's additional bet
     * 
     * Precondition: getNumHands() > 0 && 0 <= index && index < getNumHands()
     */
    public double getAdditionalBet(int index)
    {
        validateInitialBetAndIndex(index);

        return playersHands.get(index).getAdditionalBet();
    }
    
    /**
     * Returns an array of 4 cards dealt from the shoe.
     * [0] and [2] are the player's cards
     * [1] and [3] are the dealer's cards
     * @return an array of 4 cards dealt from the shoe
     */
    private Card[] getInitialCards()
    {
        Card[] cards = new Card[4];
        
        for(int i = 0; i < cards.length; i++)
            cards[i] = shoe.dealCard();
        
        return cards;
    }
    
    /**
     * Places a bet at the start of a round. Deals cards to the player and dealer.
     * @param amount the amount to bet
     * 
     * Precondition: getNumHands() == 0 && amount > 0
     */
    public void placeInitialBetAndDealCards(double amount)
    {
        if (getNumHands() != 0)
            throw new IllegalStateException("only one initial bet can be made per round");

        if (amount <= 0)
            throw new IllegalArgumentException("amount bet must be greater than 0");

        this.playersMoney -= amount;
        playersHands.add(new HandAndBet(amount));
        currentHand = 0;
        
        Card[] cards = getInitialCards();
        
        HandAndBet playersInitialHand = playersHands.get(0);
        playersInitialHand.hit(cards[0]);
        playersInitialHand.hit(cards[2]);

        this.dealersHand = new Hand(cards[1], cards[3]);
        
        if( ! decisionIsRequiredForCurrentHand() )
            advanceToNextHandWithDecision();
    }
    
    /**
     * Returns a copy of the player's specified hand
     * @param index the index of the requested hand
     * @return a copy of the player's specified hand
     * 
     * Precondition: getNumHands() > 0 && 0 <= index && index < getNumHands()
     */
    public Hand getPlayersHand(int index)
    {
        validateInitialBetAndIndex(index);
    		
    		return new Hand(playersHands.get(index).getHand());
    }
    
    /**
     * Returns a copy of the face up portion of the dealer's hand
     * @return a copy of the face up portion of the dealer's hand
     * 
     * Precondition: getNumHands() > 0
     */
    public Hand getFaceUpDealersHand()
    {
        validateInitialBet();
    		
    		if(dealerPlayed)
    			return new Hand(dealersHand);
    		else
    			return new Hand(dealersHand.firstCard());
    }
    
    private void validateCurrentHand()
    {
        if( ! (getCurrentHand() >= 0) )
            throw new IllegalStateException("there is no hand with a decision to be made");
    }
    
    /**
     * Stands on the player's current hand. Advances to the next hand for which
     * a decision is to be made or -1 if there is no additional hand for which
     * a decision is to be made.
     * 
     * Precondition: getNumHands() > 0 && getCurrentHand() >= 0
     */
    public void stand()
    {
        validateInitialBet();
        validateCurrentHand();
        
        advanceToNextHandWithDecision();
    }
    
    /**
     * Returns true if the player can hit the current hand, false otherwise
     * @return true if the player can hit the current hand, false otherwise
     * 
     * Precondition: getNumHands() > 0 && getCurrentHand() >= 0
     */
    public boolean canHit()
    {
        validateInitialBet();
        validateCurrentHand();
        
    		HandAndBet playersHand = playersHands.get(currentHand);
    		
    		if(getNumHands() > 1 && playersHand.getHand().getCards().get(0).getValue() == 1)
    		    return false; // cannot hit split access
    		
    		return ! this.dealersHand.isBlackjack() && playersHand.canHit();
    }
    
    /**
     * Deals another card to the player's current hand.
     * 
     * Precondition: canHit()
     */
    public void hit()
    {
        if( ! canHit() )
            throw new IllegalStateException("player cannot hit this hand");
        
        HandAndBet playersHand = playersHands.get(currentHand);
        playersHand.hit(shoe.dealCard());
        
        if( ! decisionIsRequiredForCurrentHand() )
            advanceToNextHandWithDecision();
    }
    
    /**
     * Returns true if the player can split the current hand, false otherwise
     * @return true if the player can split the current hand, false otherwise
     * 
     * Precondition: getNumHands() > 0 && getCurrentHand() >= 0
     */
    public boolean canSplit()
    {
        validateInitialBet();
        validateCurrentHand();
        
        HandAndBet playersHand = playersHands.get(currentHand);
        return ! this.dealersHand.isBlackjack() && playersHand.canSplit() && getNumHands() < MAX_HANDS;
    }
    
    /**
     * Splits the player's current hand
     * 
     * Precondition: canSplit()
     */
    public void split()
    {
        if( ! canSplit() )
            throw new IllegalStateException("player cannot split this hand");
        
        HandAndBet playersHand = playersHands.get(currentHand);
        
        this.playersMoney -= getTotalBet(currentHand);
        
        HandAndBet newHand = playersHand.split();
        playersHands.add(newHand);
        
        playersHand.hit(shoe.dealCard());
        newHand.hit(shoe.dealCard());
        
        if( ! decisionIsRequiredForCurrentHand() )
            advanceToNextHandWithDecision();
    }
    
    /**
     * Returns true if the player can double down on the current hand, false otherwise
     * @return true if the player can double down on the current hand, false otherwise
     * 
     * Precondition: getNumHands() > 0 && getCurrentHand() >= 0
     */
    public boolean canDoubleDown()
    {
        validateInitialBet();
        validateCurrentHand();
        
        HandAndBet playersHand = playersHands.get(currentHand);
        return canHit() && playersHand.canDoubleDown();
    }
    
    /**
     * Doubles down on the player's current hand.
     * 
     * Precondition: canDoubleDown() && 0 < additionalBet && additionalBet <= getBet(getCurrentHand())
     */
    public void doubleDown(double additionalBet)
    {
        if( ! canDoubleDown() )
            throw new IllegalStateException("player cannot double down on this hand");
        
        if( ! (0 < additionalBet && additionalBet <= getTotalBet(getCurrentHand())) )
            throw new IllegalArgumentException("additionalBet must be > 0 and <= getBet(getCurrentHand())");
        
        playersMoney -= additionalBet;
        
        HandAndBet playersHand = playersHands.get(currentHand);
        playersHand.doubleDown(additionalBet, shoe.dealCard());
        
        advanceToNextHandWithDecision();
    }
    
    /**
     * Plays the dealer's hand. Note that the dealer does not take addition cards if the
     * all of the player's hands busted or if the player's only hand was blackjack
     * 
     * Precondition: getNumHands() > 0 && getCurrentHand() == -1
     */
    public void playDealersHand()
    {
    		/* 
    		 * Running this method a second time has no effect so there is no
    		 * precondition that the dealer has not already played.
    		 */
        
        this.validateInitialBet();
        
        if(getCurrentHand() != -1)
            throw new IllegalStateException("player must make all decisions before dealer plays hand");
        
		boolean playerHasViableHand = false;
		for(int i = 0; i < playersHands.size(); i++)
			if(playersHands.get(i).getHand().getValue() <= 21)
				playerHasViableHand = true;
		
		if( playerHasViableHand && ! (getNumHands() == 1 && playersHands.get(0).getHand().isBlackjack()) )
            while(this.dealersHand.getValue() < 17)
                this.dealersHand.addCard(shoe.dealCard());
        
        this.dealerPlayed = true;
    }
    
    private void validateDealerHasPlayedHand()
    {
        Hand dealersFaceUpHand = getFaceUpDealersHand();
        if(dealersFaceUpHand == null || ! (getFaceUpDealersHand().getNumCards() > 1) )
            throw new IllegalStateException("must play dealer's hand first");
    }
    
    /**
     * Returns true if the player's specified hand is a push, false otherwise
     * @param index the index of the hand to check 
     * @return true if the player's hand is a push, false otherwise
     * 
     * Precondition: getNumHands() > 0 &&
     *               0 <= index && index < getNumHands() &&
     *               getFaceUpDealersHand().getNumCards() > 1
     */
    public boolean isPush(int index)
    {
        validateInitialBetAndIndex(index);
    		validateDealerHasPlayedHand();
    		
    		HandAndBet playersHand = playersHands.get(index);
        int playersHandValue = playersHand.getHand().getValue();
        return playersHandValue <= 21 && playersHandValue == this.dealersHand.getValue();
    }
    
    /**
     * Returns true if the player's specified hand is a player win, false otherwise
     * @param index the index of the hand to check
     * @return true if the player's hand is a player win, false otherwise
     * 
     * Precondition: getNumHands() > 0 &&
     *               0 <= index && index < getNumHands() &&
     *               getFaceUpDealersHand().getNumCards() > 1
     */
    public boolean isPlayerWin(int index)
    {
        validateInitialBetAndIndex(index);
        validateDealerHasPlayedHand();
        
        HandAndBet playersHand = playersHands.get(index);
        int playersHandValue = playersHand.getHand().getValue();
        int dealersHandValue = this.dealersHand.getValue();
        return playersHandValue <= 21 &&
                (dealersHandValue > 21 || playersHandValue > dealersHandValue);
    }
    
    /**
     * Returns true if the player has blackjack, false otherwise
     * @return true if the player has blackjack, false otherwise
     * 
     * Precondition: isPlayerWin(0)
     */
    public boolean isPlayerBlackjack()
    {
        if( ! isPlayerWin(0) )
            throw new IllegalStateException("player must have won initial hand to check for blackjack");
        
        HandAndBet playersHand = playersHands.get(0);
        return getNumHands() == 1 && playersHand.getHand().isBlackjack();
    }
    
    /**
     * Resolves the player's bets (updates player's money based on the
     * results of the round) and resets for another round
     * 
     * Precondition: getFaceUpDealersHand().getNumCards() > 1
     */
    public void resolveBetsAndReset()
    {
        validateDealerHasPlayedHand();
        
        for(int index = 0; index < playersHands.size(); index++)
        {
            if(isPush(index))
                this.playersMoney += getTotalBet(index);
            else if(isPlayerWin(index))
            {
                this.playersMoney += 2 * getTotalBet(index);
                if(index == 0 && isPlayerBlackjack())
                    this.playersMoney += getTotalBet(index) / 2;
            }
        }
        
        reset();
    }
    
    // TODO: add late surrender
}
