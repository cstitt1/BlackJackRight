package blackjack;

import java.util.ArrayList;

public class Tester
{

	public static void main(String[] args)
	{
		//testShoe();
		testHandCards();
	}
	
	public static void testHandCards()
	{
		Shoe shoe = new Shoe(6);
		shoe.shuffle();
		Hand hand = new Hand(shoe); //works
		System.out.println(hand); //works
		System.out.println(hand.checkHand());
		hand.addCard(); //works
		System.out.println(hand);
		System.out.println(hand.sumCards()); //works
//		System.out.println(hand.resolve("Win")); 
		
		
	}

	public void testShoe()
	{
		Shoe shoe = new Shoe(6);
		shoe.shuffle();
		
		System.out.println(shoe.getNext());
		ArrayList<String> cards = shoe.getCards();
		for(int x =0; x < cards.size(); x++)
		{
			System.out.println(cards.get(x));
		}
	}
}
