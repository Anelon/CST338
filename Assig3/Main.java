/** Team SAGA */
/** Card Deck */
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.Random;


class Card
{
   public enum Suit {spades, hearts, clubs, diamonds};
   //Characters to hold the values cards can be
   private static final char ACE = 'A', KING = 'K', QUEEN = 'Q', JACK = 'J';
   private static final char TEN = 'T';
   private static final char MIN_VALUE = '2', MAX_VALUE = '9';

   //private class values of suit, card value, and errorflag
   private Suit suit;
   private char value;
   //holds if an error has occured
   private boolean errorFlag = false;

   //Static finals for default card values
   public static final Suit DEFAULT_SUIT = Suit.spades;
   public static final char DEFAULT_VALUE = ACE;

   //card to string converter
   public String toString()
   {
      if(errorFlag)
         return "[Invalid]";
      return value + " of " + suit;

   }

   //returns if this card and another card are the same
   public boolean equals(Card card)
   {
      return card.getValue() == value && card.getSuit() == suit;
   }

   //default constructor
   public Card()
   {
      value = ACE;
      suit = Suit.spades;
   }

   //For use of checking of the values are valid
   boolean isValid(char newValue, Suit newSuit)
   {
      if ((newValue >= MIN_VALUE && newValue <= MAX_VALUE)
            || newValue == ACE || newValue == KING || newValue == QUEEN
            || newValue == TEN || newValue == JACK)
      {
         return true;
      }
      return false;
   }

   //get Number for the card returns if its valid and value was set
   public boolean setValue(char newValue)
   {
      if (isValid(newValue, suit))
      {
         value = newValue;
         errorFlag = false;
         return true;
      }
      errorFlag = true;
      return false;
   }

   //takes in a new suit and if its valid sets the suit and returns true
   public boolean setSuit(Suit newSuit)
   {
      suit = newSuit;
      return true;
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

   //returns if an error has occured
   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   //collection of constructors
   public Card(char newValue, Suit newSuit)
   {
      errorFlag = !setValue(newValue);
      setSuit(newSuit);
   }
   public Card(Suit newSuit)
   {
      this(ACE, newSuit);
   }
   public Card(char newValue)
   {
      this(newValue, Suit.spades);
   }
   //constructor for duplicating cards
   public Card(Card copy)
   {
      errorFlag = !setValue(copy.getValue());
      setSuit(copy.getSuit());

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
               switch (cardNum)
               {
               case 1:
               {
                  cardValue = 'A';
                  break;
               }
               case 10:
               {
                  cardValue = 'T';
                  break;
               }
               case 11:
               {
                  cardValue = 'J';
                  break;
               }
               case 12:
               {
                  cardValue = 'Q';
                  break;
               }
               case 13:
               {
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
   //set up scanner object
   Scanner keyboard = new Scanner(System.in);
   //boolean that runs the class tests if true
   private static final boolean TESTING = true;

   public static void main(String[] args)
   {
      if(TESTING)
      {
         CardTest.main();
      }
   }
}

class CardTest
{
   static final int TEST_CARD_SIZE = 3;
   static Card[] testCards = new Card[TEST_CARD_SIZE];

   public static void main()
   {
      //set up test of card class
      testCards[0] = new Card('Q', Card.Suit.hearts);
      testCards[1] = new Card('D', Card.Suit.hearts);
      testCards[2] = new Card();
      //print test cards
      for(Card card : testCards)
         System.out.println(card.toString());
      //fix the 2 bad ones
      testCards[1].setValue('6');
      testCards[2].setValue('F');
      System.out.println();
      //print fixed test cards
      for(Card card : testCards)
         System.out.println(card.toString());
   }
}
