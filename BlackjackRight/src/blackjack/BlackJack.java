package blackjack;

public class BlackJack
{
	private Hand hand;
	private Dealer dealer;
	private Shoe shoe;
	private double bankroll;
	private double bet;

	/**
	 * Constructs a game of blackjack
	 * @param bankroll
	 */
	public BlackJack(double bankroll)
	{
		shoe = new Shoe(6);
		shoe.shuffle();
		hand = new Hand(shoe);
		dealer = new Dealer(shoe);
		this.bankroll = bankroll;
		
	}
	
	/**
	 * Takes a bet and subtracts it from bankroll
	 * Deals cards to player and dealer
	 * @param bet
	 */
	public void bet(double bet)
	{
		this.bet = bet;
		bankroll -= bet;
		hand.changeCards();
		dealer.changeCards();	
	}
	
	/**
	 * Takes an action as a String 
	 * ("H" to hit, any other key to stand)
	 * @param action
	 */
	public void playerAction(String action)
	{
		if(action.equals("H"))
			hand.addCard();
		else
			compareCards();		
	}
	
	/**
	 * Returns immediately if player has blackjack
	 * If the dealer's hand is <17, the dealer takes another card
	 * Prints out both hands
	 */
	public void updateDealerCards()
	{
		if(hand.isBlackJack())
			return;
		
		if(hand.checkHand().equals("Bust"))
			return;
		
		while( dealer.sumCards() < 17) 
				dealer.addCard();
		
	}
	
	public String showAllDealersHand()
	{
		return dealer.toString(true);
	}

	
	/**
	 * Checks if the either player or dealer have blackjack or have busted, if so the player cannot hit
	 * Else the player can hit
	 * @return
	 */
	public boolean canHit()
	{
		if(hand.checkHand().equals("Blackjack") || 
		   dealer.checkHand().equals("Blackjack"))
			return false;
		else if(hand.checkHand().equals("Bust") ||
				dealer.checkHand().equals("Bust"))
			return false;
		
		else if(hand.sumCards() == 21)
			return false;

		return true;
	}
	

	
	/**
	 * Compares the player and dealer hands, printing out a message appropriate to the outcome
	 * Updates bankroll accordingly
	 */
	public void compareCards() //called after standing
	{
		if(hand.checkHand().equals("Blackjack") && dealer.checkHand().equals("Blackjack"))
		{
			updateBankRoll(resolve("tie"));
			System.out.println("You tie");
		}
		
		else if(hand.checkHand().equals("Blackjack"))
		{
			updateBankRoll(resolve("Blackjack"));
			System.out.println("You got blackjack");
		}
		
		else if(dealer.checkHand().equals("Blackjack"))
		{
			updateBankRoll(resolve("Lose"));
			System.out.println("dealer got blackjack");
		}
	
		else if(hand.checkHand().equals("Bust"))
		{
			updateBankRoll(resolve("Lose"));
			System.out.println("You lose");
		}
		
		else if(dealer.checkHand().equals("Bust") || hand.sumCards() > dealer.sumCards())
		{
			updateBankRoll(resolve("Win"));
			System.out.println("You win");
		}
		
		else if(hand.sumCards() == dealer.sumCards())
		{
			updateBankRoll(resolve("tie"));
			System.out.println("You tie");
		}
		
		else
		{
			updateBankRoll(resolve("Lose"));
			System.out.println("You lose");
		}
	}
	
	/**
	 * Possible values of result:
	 * Blackjack -- get bet back and 1.5 times bet
	 * Win -- get bet back doubled
	 * Loss -- don't get anything back
	 * @param result the result of that round
	 * @return how the bankroll should be adjusted
	 */
	public double resolve(String result) {
		if (result.equals("Blackjack"))
			return bet + 1.5*bet;
		else if (result.equals("Win"))
			return bet*2;
		else if(result.equals("tie"))
			return bet;
		else
			return 0;
	}
	
	/**
	 * @return player hand
	 */
	public String getPlayerHand()
	{
		return hand.toString();
	}
	
	/**
	 * @return dealer hand
	 */
	public String getDealerHand()
	{
		return dealer.toString(false);
	}
	
	
	/**
	 * Updates bankroll based on an amount
	 * @param amount
	 */
	public void updateBankRoll(double amount)
	{
		
		bankroll += amount;
	}
	
	/**
	 * @return bankroll
	 */
	public double getBankroll()
	{
		return bankroll;
	}


	
}
