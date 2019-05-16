/** Team SAGA */
/** Card Deck */
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.Random;


class Card
{
   public enum Suit {spades, hearts, clubs, diamonds};
   private char value;
   private static final char ACE = 'A', KING = 'K', QUEEN = 'Q', JACK = 'J';
   private static final char TEN = 'T';
   private static final int MIN_VALUE = 2, MAX_VALUE = 9;
   private Suit suit;
   private boolean errorFlag;

   //default constructor
   public Card()
   {
      value = '0';
      suit = Suit.spades;
      errorFlag = true;
   }

   //get Number for the card returns if its valid and value was set
   boolean setValue(char cardNumber) 
   {
      if ((cardNumber >= MIN_VALUE && cardNumber <= MAX_VALUE)
            || cardNumber == ACE
            )
      {
         value = cardNumber;
         return true;
      }
      return false;
   }

   //returns the value of the card as a char
   public char getValue()
   {
      return value;
   }

   //returns the suit of the card as an enum
   public Suit getSuit()
   {
      return suit;
   }

   public Card(char cardNumber, Suit suit)
   {
      setValue(cardNumber);

   }
   public Card(Card copy)
   {
      setValue(copy.getValue());

   }

   //card to string converter
   public String toString()
   {
      return "Card";
   }
}







class Deck
{

   public Deck(int numPacks)
   {
      allocateMasterPack();
      init(numPacks);
   }



   public Deck()
   {
      this(1);
   }



   public void init(int numPacks)
   {

      int totalCardIndex = 0;
      for (int packNumber = 0; packNumber < numPacks; ++packNumber)
      {
         for (int masterIndex = 0; masterIndex < masterPack.length; ++masterIndex)
         {
            cards[totalCardIndex] = new Card(masterPack[masterIndex]);
               ++totalCardIndex;
         }
      }
   }



   public void shuffle()
   {
      Random randomGenerator = new Random();
      int randomIndex = randomGenerator.nextInt(cards.length);
      for (int i = 0; i < cards.length; ++i)
      {
         Card placeholder = new Card(cards[randomIndex]);
         cards[randomIndex] = cards[i];
         cards[i] = placeholder;
         randomIndex = randomGenerator.nextInt(cards.length);
      }
   }



   public Card dealCard()
   {
      return new Card(cards[topCard++]);
   }


   public Card inspectCard(int k)
   {
      Card cardToInspect;

      if (k < cards.length && k >= 0)
      {
         cardToInspect = new Card(cards[k]);
      }
      else
         cardToInspect  = new Card('0', Card.Suit.spades);

      return cardToInspect;
   }


   public Card getTopCard()
   {
      return new Card(cards[topCard]);
   }


   /**
    * The following function converts int values to a Card.Suit enum.
    * For use with quick access of enums.
    *
    * @param toBeConverted - integer to be converted to a Card.Suit enum
    * @return one of the 4 suits as defined in Card class. 0 = hearts,
    * 1 = clubs, 2 = diamonds, all other ints will return spades
    */
   private static Card.Suit intToSuit(int toBeConverted)
   {
      switch (toBeConverted)
      {
         case 0:
            return Card.Suit.hearts;
         case 1:
            return Card.Suit.clubs;
         case 2:
            return Card.Suit.diamonds;
         default:
            return Card.Suit.spades;
      }

   }


   private static void allocateMasterPack()
   {
      if (masterPack[0] == null)
      {
         int masterIndex = 0;

         for(int suitInt = 0; suitInt < 4; ++suitInt)
         {
            for (int cardNum = 1; cardNum <=13; ++cardNum)
            {
               char cardValue = (char)cardNum;
               switch (cardNum) {
                  case 1: {
                     cardValue = 'A';
                     break;
                  }
                  case 10: {
                     cardValue = 'T';
                     break;
                  }
                  case 11: {
                     cardValue = 'J';
                     break;
                  }
                  case 12: {
                     cardValue = 'Q';
                     break;
                  }
                  case 13: {
                     cardValue = 'K';
                     break;
                  }
               }
               masterPack[masterIndex] = new Card(cardValue, intToSuit(suitInt));
               ++masterIndex;
            }
         }
      }
   }

   private Card[] cards;
   private int topCard;
   public final int MAX_CARDS = 6*52;
   private static Card[] masterPack = new Card[52];

}

class Hand
{
   //need to change the amount of cards (or use an arrayList?)
   private static Card[] myArray = new Card[120];
   private static int numCards;

   //default constructor
   public Hand()
   {
      numCards = -1;
   }

   //Helper function to take a newCard from the table and add to myArray
   public void takeCard(Card newCard)
   {
      return;
   }

   //Remove card from hand and return that card
   public Card playCard()
   {
      return myArray[0];
   }
}


public class Main
{
   public static void main(String[] args)
   {
      //set up scanner object
      Scanner keyboard = new Scanner(System.in);

      System.out.print("Please enter your first name: ");
      String firstName = keyboard.next();

      System.out.println(firstName);
   }
}
