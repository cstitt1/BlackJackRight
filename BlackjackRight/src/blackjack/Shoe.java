package blackjack;

import java.util.ArrayList;

public class Shoe
{

	private ArrayList<String> cards;
	
	/**
	 * Constructs a new Shoe by combining numDecks number of 52 card decks
	 * @param numDecks the number of 52 card decks in the shoe
	 */
	public Shoe(int numDecks)
	{
		cards = new ArrayList<String>();
		
		int num = 1;
		
		String[] temp = new String[52];
		
		for(int x = 0; x < temp.length; x ++)
		{
			if(x % 4 == 0)
				temp[x] = "S";	
			else if(x % 4 == 1)
				temp[x] = "H";	
			else if(x % 4 == 2)
				temp[x] = "C";		
			else
				temp[x] = "D";
			
			if(num == 11)
				temp[x] = "J" + temp[x];
				
			else if(num == 12)
				temp[x] =  "Q" + temp[x];
				
			else if(num == 13)
				temp[x] = "K" + temp[x];
				
			else if(num == 1)
				temp[x] = "A"  + temp[x];
				
			else
				temp[x] = num + temp[x];
				
			if(x % 4 == 3)
				num ++;
		}
		
		for(int x = 0; x < numDecks; x ++)
		{
			for(int y= 0; y < 52; y ++)
			{
				cards.add(temp[y]);
			}
			
		}
		
		
	}
	
	/**
	 * Shuffles the shoe into a random order
	 */
	public void shuffle()
	{
		for(int x = 0; x < cards.size() -1; x ++)
		{
			int index = (int) (Math.random() * (cards.size()-1 -x) + x+1);
		        	
			String temp = cards.get(x);
			cards.set(x, cards.get(index));
			cards.set(index, temp);
		   }
	}
	
	/**
	 * Returns the next card of the shoe
	 * @return the next card of the shoe
	 */
	public String getNext()
	{

		return cards.remove(0);
		
	}
	
	/**
	 * Returns an ArrayList containing the contents of the shoe
	 * @return an ArrayList containing the contents of the shoe
	 */
	public ArrayList<String> getCards()
	{
		return cards;
	}
}
