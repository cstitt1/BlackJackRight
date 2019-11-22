package blackjack;
import java.util.ArrayList;

public class Hand 
{
	private ArrayList<String> cards;
	private Shoe shoe;
	
	public Hand(Shoe shoe)
	{
		cards = new ArrayList<String>();
		this.shoe = shoe;

	}
	
	/**
	 *  Deals cards and maintains a
	 *  suitable number of cards in shoe
	 *  if cards <= 10 it shuffles
	 */
	public void changeCards()
	{
		
		if(shoe.getCards().size() <= 10)
		{
			shoe = new Shoe(6);
			shoe.shuffle();
		}
		
		cards = new ArrayList<String>();
		
		cards.add(shoe.getNext());
		cards.add(shoe.getNext());
	}
	
	/**
	 * adds another card to the hand
	 */
	public void addCard()
	{
		cards.add(shoe.getNext());
	}
	
	/**
	 * prints all the cards in the hand
	 */
	public String toString()
	{
		return cards.toString();
	}
	
	/**
	 * checks if hand has blackjack
	 * @return true if hand had blackjack and false if not
	 */
	public boolean isBlackJack()
	{
		String card1 = cards.get(0);
		String card2 = cards.get(1);
		
		if(card1.substring(0,1).equals("A"))
		{
			if(card2.substring(0,1).equals("1") || card2.substring(0,1).equals("J") || card2.substring(0,1).equals("Q") || card2.substring(0,1).equals("K"))
				return true;
		}
		
		if(card2.substring(0,1).equals("A"))
		{
			if(card1.substring(0,1).equals("1") || card1.substring(0,1).equals("J") || card1.substring(0,1).equals("Q") || card1.substring(0,1).equals("K"))
				return true;
		}
		
		return false;
			
	}
	
	/**
	 * Possible return values:
	 * Bust -- over 21 and holder busts
	 * Blackjack -- Is 21 with only 2 cards, holder wins
	 * Numerical value -- the sum of the values in the hand
	 * @return the result of checking the hand
	 */
	public String checkHand() 
	{
		if (cards.size() == 2 && isBlackJack())
			return "Blackjack";
		
		int sum = sumCards();
		
		if (sum <= 21)
			return ""+sum;
		else
			return "Bust";
	}
	
	/**
	 * computes the sum of the cards in hand with proper ace amounts
	 * @return sum of the cards in hand
	 */
	public int sumCards() 
	{
		int sum = 0;
		boolean ace = false;
		for (String card : cards) {
			if (card.substring(0,1).equals("J") || card.substring(0,1).equals("Q") || card.substring(0,1).equals("K") || card.substring(0,1).equals("1"))
				sum += 10;
			else if (card.substring(0,1).equals("A")) {
				sum += 1;
				ace = true;
			}
			else
				sum +=  Integer.parseInt(card.substring(0,1));
		}
		
		if (ace && sum + 10 <= 21)
			sum += 10;
		
		return sum;
	}
	
	
}

//dealer extending off of hand
