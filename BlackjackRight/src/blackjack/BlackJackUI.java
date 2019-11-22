package blackjack;

import java.util.Scanner;

public class BlackJackUI
{
	/**
	 * Runs the Blackjack User Interface
	 * @param args
	 */
	public static void main(String[] args)
	{
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter a bankroll amount: ");
		double bank = keyboard.nextDouble();
		
		BlackJack bj = new BlackJack(bank);
		boolean isStanding = false;
		
		do
		{
		
			System.out.println("Bet: ");
			double bet = keyboard.nextDouble();
			bj.bet(bet);
			
			System.out.println("hand: " + bj.getPlayerHand() + "\n" +  "dealer: " + bj.getDealerHand() + "\n" +  "bank: " + bj.getBankroll());
			while(bj.canHit() && ! isStanding)
			{
				System.out.println("H for hit or S for stand");
				String action = keyboard.next();
			
				if(action.equals("H") || action.equals("h"))
				{
					bj.playerAction("H"); //hit or stand
					System.out.println("hand: " + bj.getPlayerHand() + "\n" +  "dealer: " + bj.getDealerHand() + "\n" +  "bank: " + bj.getBankroll());
				}
			
				else if(action.equals("S") || action.equals("s"))
					isStanding = true;
			}
		
			bj.updateDealerCards();
			bj.playerAction("S");
			System.out.println("hand: " + bj.getPlayerHand() + "\n" +  "dealer: " + bj.showAllDealersHand() + "\n" +  "bank: " + bj.getBankroll());
		
			System.out.println("P to play or any other key to quit");
		
			isStanding = false;
		}
		while(keyboard.next().equals("P"));

	}
	
	
	

}
