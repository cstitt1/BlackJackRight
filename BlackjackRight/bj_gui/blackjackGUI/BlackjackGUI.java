package blackjackGUI;


import java.util.ArrayList;

import bj_horn.*;

import processing.core.PApplet;
import processing.core.PImage;

public class BlackjackGUI extends PApplet
{
	private Blackjack bj;
	private int doubledHand;
	private boolean hasPlacedBet;
	
	public void setup()
	{
		size(800, 600);
		PImage background = loadImage("../../images/bg.png");
		image(background, 0, 0);
		
		bj  = new Blackjack(1000);
		hasPlacedBet = false;
		doubledHand = -1;
	}
	
	public void draw()
	{
		PImage background = loadImage("../../images/bg.png");
		image(background, 0, 0);
		
		if(!hasPlacedBet)
		{
			textSize(20);
			text("Enter a bet (1 for 10, 2 for 20, etc) ", 15, 585);
		}

		textSize(20);
		text("Bankroll:" + bj.getPlayersMoney(), 15, 30);
		
		for(int x = 0; x < bj.getNumHands(); x ++)
		{
			text("Bet " + (x + 1) + ": " + bj.getTotalBet(x), 15, 55 + x * 20);
		}
		
		String moves = "";
		
		if(hasPlacedBet)
		{	
			displayDealersHand();
			for(int x = 0; x < bj.getNumHands(); x ++)
			{
				displayPlayersHand(x);
			}
			
			
			if( bj.getCurrentHand() >= 0)
			{
				if (bj.canHit())
					moves += "H to hit";
				if (bj.canDoubleDown())
					moves += ", D to double";
				if (bj.canSplit())
					moves += ", X to split";		
				if (!moves.equals(""))
					text("Press " + moves.substring(0, moves.indexOf("i")+2) + ", S to stand" + moves.substring(moves.indexOf("i")+2), 15, 585);
				
				if(bj.getNumHands() > 1)
				{
					text("you are playing hand " + (bj.getCurrentHand() + 1), 480, 585);
				}
			}
			
			
			if(bj.getCurrentHand() < 0)
			{
				bj.playDealersHand();
				
				for(int x = 0; x < bj.getNumHands(); x ++)
				{
					if (bj.isPush(x))
					{
						text("hand" + (x + 1) + ": You tie!", 15, 560 - x * 25);
					}
						
					else if (bj.isPlayerWin(x))
					{
						if(bj.getNumHands() == 1 && bj.isPlayerBlackjack())
							text("hand" + (x + 1) + ": You got blackjack!", 15, 560 - x * 25);
						else
							text("hand" + (x + 1) + ": You win!", 15, 560 - x * 25);
					}
					else
					{
						text("hand" + (x + 1) + ": You lose!", 15, 560 - x * 25);
					}
				}
				

				text("R to reset", 15, 585);
			}
			
			
			
		}
	}
	
	
	public void keyPressed()
	{
		if(keyCode >= 49 && keyCode <= 57 && ! hasPlacedBet)
		{
			bj.placeInitialBetAndDealCards((keyCode - 48)*10);
			hasPlacedBet = true;
		}
		
		if(bj.getCurrentHand() >= 0 && bj.getTotalBet(bj.getCurrentHand()) != 0)
		{
			if(keyCode == 72 && bj.canHit())
			{
				bj.hit();
			}
		
			if(keyCode == 83)
			{
				bj.stand();	
			}
		
			if(keyCode == 68 && bj.canDoubleDown())
			{
				doubledHand = bj.getCurrentHand();
				bj.doubleDown(bj.getTotalBet(bj.getCurrentHand()));
			}
			
			if(keyCode == 88 && bj.canSplit())
			{
				bj.split();
			}
		}
		
		if(keyCode == 82)
		{
			resolveAndReset();
		}
		
	}
	
	
	public void resolveAndReset()
	{

		bj.resolveBetsAndReset();
		doubledHand = -1;
		hasPlacedBet = false;
	}

	
	/**
	 * Used by displayPlayersHand(int index) and displayDealersHand
	 * TODO: Handle splitting
	 * 
	 * @param c			card to be displayed
	 * @param cardNum	determines location of cards based on number
	 * @param isPlayer	determines location of cards based on player (true) or dealer (false)
	 */
	public void displayCard(Card c, int cardNum, boolean isPlayer)
	{
		String str = c.toString();
		PImage card = loadImage("../../images/" + str + ".png");
		
		if(isPlayer) //isn't setup to handle splitting
		{
			if(cardNum == 0)
				image(card, 387, 450);
			else if(doubledHand >= 0 && cardNum == 2)
			{
				//rotate image
				rotateCard(card, 547 , 445);
			}
			else
				image(card, 387 + (40*cardNum), 430);
		}
		else //dealer's hand
		{
			if(cardNum == 0)
				image(card, 387, 70);

			if(bj.getFaceUpDealersHand().getNumCards() > 1 && cardNum > 0) //dealer has played
				image(card, 387 + 40 + (40*(cardNum-1)), 50);
			
			else if(cardNum == 0)
			{
				PImage blank = loadImage("../../images/back.png");
				image(blank, 387 + 40, 50); 
			}
		}

	}
	
	/**
	 * Call when displaying the player's hand
	 * @param index
	 */
	public void displayPlayersHand(int index)
	{
		if(bj.getNumHands() > 1)
		{
			displaySplitHand(index);
		}
		else
		{
			ArrayList<Card> cards = bj.getPlayersHand(index).getCards();
			
			for(int cardNum = 0; cardNum < cards.size(); cardNum++)
			{
				displayCard(cards.get(cardNum), cardNum, true);
			}
		}	
	}
	
	/**
	 * Call when displaying the dealer's hand
	 */
	public void displayDealersHand()
	{
		ArrayList<Card> cards = bj.getFaceUpDealersHand().getCards();
		
		for(int cardNum = 0; cardNum < cards.size(); cardNum++)
		{
			displayCard(cards.get(cardNum), cardNum, false);
		}
	}
	
	public void displaySplitHand(int index)
	{
		ArrayList<Card> cards = bj.getPlayersHand(index).getCards();
		
		for(int cardNum = 0; cardNum < cards.size(); cardNum++)
			displaySplitCard(cards.get(cardNum), cardNum, index);
		
	}
	
	public void displaySplitCard(Card c, int cardNum, int index)
	{
		String str = c.toString();
		PImage card = loadImage("../../images/" + str + ".png");
		
		if(cardNum == 0)
			image(card, 200 + (200*index), 430);
		else if(doubledHand == index && cardNum == 2)
			rotateCard(card, 350 + (200*index) , 435); //problem: card rotates back after playing second hand
		else
			image(card, 200 + (200*index + cardNum * 40), 400);
			
	}
	
	public void rotateCard(PImage card, int x, int y)
	{
		translate(x,y);
		rotate(HALF_PI);
		image(card, 0,0);
		
		rotate(-HALF_PI);
		translate(-x,-y);
		
	}

}
