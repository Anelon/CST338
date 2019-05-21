/** Team SAGA - 
 * Shelly Sun, Andrew Bell, Greg Brown, Andrew Terrado  */

/** Card Deck */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DecimalFormat;
import java.util.Random;
import java.lang.IllegalArgumentException;


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
   private boolean isValid(char newValue, Suit newSuit)
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

   //takes both values and sets them
   public boolean set(char newValue, Suit newSuit)
   {
      setSuit(newSuit);
      return setValue(newValue);
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






/**
 * Class containing data for a deck of cards.  Contains static information of
 * all possible cards as well as current in-use cards/order within the Deck object.
 *
 * public static int MAX_CARDS - Maximum number of cards allowed in play at once.
 * private Card[] cards - current "in use" cards for playing
 * private int topCard - top card of the deck, next to be "dealt".
 * private static Card[] masterPack - static representation of all possible cards
 */
class Deck
{


   private Card[] cards;
   private int topCard;
   public final int MAX_CARDS = 6*52;
   private static Card[] masterPack = new Card[52];

   

   


   /**
    * Constructor that creates the deck based on the number of "packs" given.
    * Note that only a maximum of MAX_CARDS cards can be used.
    *
    * @param numPacks - number of traditional 52 card packs to be used in deck creation
    */
   public Deck(int numPacks)
   {

      allocateMasterPack();
      
      if (52*numPacks > MAX_CARDS)
         cards = new Card[MAX_CARDS];
      else
         cards = new Card[52*numPacks];
      
      init(numPacks);
   }






   /**
    * Empty constructor. Initializes the deck with a default of a single 52 card pack.
    */
   public Deck()
   {
      this(1);
   }






   /**
    * initializes the playable cards in cards[] based on the number of packs.
    * The resulting cards[] object will contain ((int)numPacks) copies of the masterPack.
    * Called by the constructors.
    *
    * @param numPacks number of 52 card packs to be used
    */
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






   /**
    * Randomizes the cards within the in-use cards[] object and resets the topCard to
    * the first position.
    */
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
      
      topCard = 0;
   }


   /**
    * "Deals" the top card from the deck. The next card in cards[] becomes the new
    * topCard.
    *
    * @return the topCard at the time of calling
    */
   public Card dealCard()
   {
      return new Card(cards[topCard++]);
   }


   /**
    * Gives information from the card at the given index within cards[].
    * Note that the card at the index is not returned, only a copy in order
    * to prevent the card itself from being altered in any way.
    *
    * @param k - index of cards[] to be checked
    * @return a copy of the Card object at cards[k]
    */
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


   /**
    * Functions similarly to inspectCard, but only inspects the current topCard.
    *
    * @return a copy of the topCard; the Card object at cards[0]
    */
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


   /**
    * Handles creation of the masterPack.  Called by init and indirectly by constructors.
    * Note that this only occurs once per object lifecycle. Repeated calls from the same object
    * will have no effect.
    */
   private static void allocateMasterPack()
   {
      if (masterPack[0] == null)
      {
         int masterIndex = 0;

         for(int suitInt = 0; suitInt < 4; ++suitInt)
         {
            for (int cardNum = 1; cardNum <=13; ++cardNum)
            {
               char cardValue = (char)('0' + cardNum);
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
}







class Hand
{

   //Keeps track the numbers of cards
   private int numCards;
   public static int MAX_CARDS = 100;
   private Card[] myCards = new Card[MAX_CARDS];

   //default constructor
   public Hand()
   {
      this.myCards= new Card[MAX_CARDS];
   }

   //remove all cards from the hand
   public void resetHand() {

      myCards = new Card[MAX_CARDS];
      numCards = 0;

   }

   //Helper function to take a newCard from the table and add to myArray
   public boolean takeCard(Card newCard)
   {
      myCards[numCards] = newCard;
      ++numCards;

      return true;
   }

   //Remove card from hand and return that card

   public Card playCard()
   {
      --numCards;
      Card cardDrawn = myCards[numCards];
      myCards[numCards] = null;
      return cardDrawn;
   }

   //Used prior to displaying the entire hand.
   public String toString() {

      String cards = "";
      for(int i = 0; i<numCards; i++) {
         cards += myCards[i].getValue() + " of " + myCards[i].getSuit() + ", ";
      }
      return "Hand = (" + cards + ") ";
   }
   //Accessor for numCards.

   public int getnumCards() {

      return numCards;
   }

   //Accessor for an individual card and returns a card with errorFalg if K is not valid.

   public Card inspectCard(int k) {

      if (k<=MAX_CARDS && k>=0){
         return myCards[k];
      }
      return new Card('T', Card.Suit.hearts);
   }


}



public class Main
{
   //set up scanner object
   Scanner keyboard = new Scanner(System.in);
   //boolean that runs the class tests if true
   private static final boolean TESTING = true;

   static final int TEST_CARD_SIZE = 3;
   static Card[] testCards = new Card[TEST_CARD_SIZE];
   
   

   public static void main(String[] args)
   {


      if(TESTING)
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
            System.out.println(card);
         //End of Card class Testing
      }

      //Testing hand class
      System.out.println("");
      System.out.println("**Testing the hand class**");
      Hand myHand = new Hand();


      myHand.takeCard(new Card ('A', Card.Suit.hearts));
      myHand.takeCard(new Card ('Q', Card.Suit.clubs));
      myHand.takeCard(new Card ('9', Card.Suit.spades));
      myHand.takeCard(new Card ('2', Card.Suit.diamonds));
      myHand.takeCard(new Card ('A', Card.Suit.hearts));
      myHand.takeCard(new Card ('Q', Card.Suit.clubs));
      myHand.takeCard(new Card ('9', Card.Suit.spades));
      myHand.takeCard(new Card ('2', Card.Suit.diamonds));
      myHand.takeCard(new Card ('A', Card.Suit.hearts));
      myHand.takeCard(new Card ('Q', Card.Suit.clubs));
      myHand.takeCard(new Card ('9', Card.Suit.spades));
      myHand.takeCard(new Card ('2', Card.Suit.diamonds));


      System.out.println(myHand.toString());

      System.out.println("");
      System.out.println("Testing inspectCard()");
      System.out.println("** legal **");
      System.out.println(myHand.inspectCard(0));
      System.out.println(myHand.inspectCard(2));
      System.out.println(myHand.inspectCard(10));
      System.out.println("** illegal **");
      System.out.println(myHand.inspectCard(200));
      System.out.println(myHand.inspectCard(-1));

      while(myHand.getnumCards()!= 0) {
         System.out.println("playing " + myHand.playCard());
      }
      myHand.resetHand();
      System.out.println("");
      System.out.println("After playing all cards");
      System.out.println(myHand.toString());
      System.out.println("");


      System.out.println("####################" + "\n" +
         "### Deck Testing ###" + "\n"+
         "####################" + "\n\n");


      Deck testDeckOnePack = new Deck();
      for (int i = 0; i < 52; ++i)
      {
         System.out.println(testDeckOnePack.dealCard());
      }
      testDeckOnePack.shuffle();
      for (int i = 0; i < 52; ++i)
      {
         System.out.println(testDeckOnePack.dealCard());
      }

      Deck testDeckSixPacks = new Deck(6);
      for (int i = 0; i < 6*52; ++i)
      {
         System.out.println(testDeckSixPacks.dealCard());
      }
      testDeckSixPacks.shuffle();
      for (int i = 0; i < 6*52; ++i)
      {
         System.out.println(testDeckSixPacks.dealCard());
      }


      testDeckOnePack.shuffle();
      System.out.println(testDeckOnePack.inspectCard(2));
      System.out.println(testDeckOnePack.getTopCard());
      System.out.println(testDeckOnePack.inspectCard(2));
      System.out.println(testDeckOnePack.getTopCard());


      /*
       * Attempt to throw an error by creating a set larger than Deck.MAX_CARDS

      Deck testDeckTooManyPacks = new Deck(7);
      for (int i = 0; i < 7*52; ++i)
      {
    	  System.out.println(testDeckTooManyPacks.dealCard());
      }
      testDeckTooManyPacks.shuffle();
      for (int i = 0; i < 7*52; ++i)
      {
    	  System.out.println(testDeckTooManyPacks.dealCard());
      }
      */
      
      
      
      
      
//Deck and Hand Testing
      System.out.println("");
      System.out.println("Deck + Hand Testing");
      
      //Initializes tester deck
      Deck testerDeck = new Deck();


      //Gets user input on how many hands
      Scanner userInput = new Scanner(System.in);
     
      //Initializes totalHands based on user input
      System.out.println("How many hands, 1-10:");
      int totalHands = userInput.nextInt();
      
      // If totalHands is not a valid number, asks user again for input
      while (totalHands < 1 || totalHands > 10)
      {
         System.out.println("Invalid number. How many hands, 1-10:");
         totalHands = userInput.nextInt();
      }
      
      //Uses user input to initialize length of hand array
      //Creates new hand object in each position
      Hand[] hands = new Hand[totalHands];
      
      for (int i = 0; i < totalHands; i++) {
         hands[i] = new Hand();
      }
      
      //Places cards in hands
      int deckPosition = 0; // initial starting point of array of hands
      for (int i = 0; i < 52; i++) {
         if (deckPosition > totalHands - 1) {
            deckPosition = 0; //resets to first hand, if last hand position is reached
         }
         hands[deckPosition].takeCard(testerDeck.dealCard());
         deckPosition++; //advances to next hand
               
      }
      //Print statement
      System.out.println("Here are our hand(s) from an unshuffled deck:");
      for (int i = 0; i< totalHands; i++) {
         System.out.println(hands[i].toString());
       }
      
    
      //Resets deck and shuffles
      testerDeck = new Deck();
      testerDeck.shuffle();
      
      
      //Empties hands
      for (int i = 0; i < totalHands; i++) {
         hands[i] = new Hand();
      }
      
      
      //Places cards in hand
      deckPosition = 0; 
      for (int i = 0; i < 52; i++) {
         hands[deckPosition].takeCard(testerDeck.dealCard());
         deckPosition++; 
         
         if (deckPosition > totalHands - 1) {
            deckPosition = 0; //resets to first hand, if last hand position is reached
         }
         
      }
      
     
      //Print statement
      System.out.println("");
      System.out.println("Here are our hand(s) from a shuffled deck:");
      for (int i = 0; i< totalHands; i++) {
         System.out.println(hands[i].toString());
       }
 

      
   }
}
