package blackjack;

/**
 * a dealer's hand
 * @author cyao
 *
 */
public class Dealer extends Hand
{

	/**
	 * creates a dealer's hand
	 * @param shoe the shoe
	 */
	public Dealer(Shoe shoe)
	{
		super(shoe);
	}
	
	/**
	 * one of the dealer's cards
	 * @return one of the dealer's cards
	 */
	public String toString(boolean showAll)
	{
		if(! showAll)
		{
			String cards = super.toString();
			int index = cards.indexOf(",");
			
			return cards.substring(0, index) + ", XX]";
		}
		
		else
			return super.toString();
	}

}
