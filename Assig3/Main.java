/** Team SAGA -
 * Shelly Sun, Andrew Bell, Greg Brown, Andrew Terrado
 * CST 338
 * 5-21-2019
 *
 * The following set of classes form a basic API
 * for use with card-based games that require hands to be
 * dealt from traditional playing cards.  The class set
 * includes basic functionality for shuffling, deck-sizing,
 * dealing, and drawing.
 *
 * Classes:
 * Deck - Core deck class. Holds static card data for available
 *    cards. Drawing/dealing/shuffling handled here.
 * Hand - Hand class. Holds data for each hand/player. Minimal functionality.
 *
 * Card - Card class. Holds one of the standard playing card values after
 *    initialization. Holds card-identifying data (Suite, value).
 */

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
 * Class containing data for a deck of cards.  Contains static 
 * information of all possible cards as well as current in-use 
 * cards/order within the Deck object.
 *
 * public static int MAX_CARDS - Maximum number of cards allowed in play at 
 *    once.
 * private Card[] cards - current "in use" cards for playing
 * private int topCard - top card of the deck, next to be "dealt".
 * private static Card[] masterPack - static representation of all possible 
 *    cards
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
    * @param numPacks - number of traditional 52 card packs to be used in 
    *                 deck creation
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
    * Empty constructor. Initializes the deck with a default of a single 
    * 52 card pack.
    */
   public Deck()
   {
      this(1);
   }






   /**
    * initializes the playable cards in cards[] based on the number of packs.
    * The resulting cards[] object will contain ((int)numPacks) copies of the 
    * masterPack.
    * Called by the constructors.
    *
    * @param numPacks number of 52 card packs to be used
    */
   public void init(int numPacks)
   {
      int totalCardIndex = 0;
      for (int packNumber = 0; packNumber < numPacks; ++packNumber)
      {
         for (int masterIndex = 0; masterIndex < masterPack.length; 
              ++masterIndex)
         {
            cards[totalCardIndex] = new Card(masterPack[masterIndex]);
            ++totalCardIndex;
         }
      }
   }






   /**
    * Randomizes the cards within the in-use cards[] object and resets 
    * the topCard to the first position.
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
    * "Deals" the top card from the deck. The next card in cards[] becomes 
    * the new topCard.
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
    * Functions similarly to inspectCard, but only inspects the current 
    * topCard.
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
    * Handles creation of the masterPack.  Called by init and 
    * indirectly by constructors.
    * Note that this only occurs once per object lifecycle. 
    * Repeated calls from the same object
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

               masterPack[masterIndex] = 
                  new Card(cardValue, intToSuit(suitInt));
               ++masterIndex;
            }
         }
      }
   }
}






/*This is the hand class. It holds data for the player's hand.*/
class Hand
{

   /*Keeps track the numbers of cards. */
   private int numCards;
   public static int MAX_CARDS = 100;
   private Card[] myCards = new Card[MAX_CARDS];

   /*Default constructor for the hand class.*/
   public Hand()
   {
      this.myCards= new Card[MAX_CARDS];
   }

   /*This function remove all cards from the hand. */
   public void resetHand() {

      myCards = new Card[MAX_CARDS];
      numCards = 0;

   }

   /*This is a helper function to take a newCard
    * from the table and add to myCards */
   public boolean takeCard(Card newCard)
   {
      myCards[numCards] = newCard;
      ++numCards;

      return true;
   }

   /*This function remove card from hand and
    *  return that card. */

   public Card playCard()
   {
      --numCards;
      Card cardDrawn = myCards[numCards];
      myCards[numCards] = null;
      return cardDrawn;
   }

   /*This function is used prior to displaying
    *the entire hand.*/
   public String toString() {

      String cards = "";
      for(int i = 0; i<numCards; i++) {
         cards += myCards[i].getValue() + " of " + myCards[i].getSuit() + ", ";
      }
      return "Hand = (" + cards + ") ";
   }
   /* This is the accessor method for numCards. */

   public int getnumCards() {

      return numCards;
   }

   /* This is accessor for an individual card and
    * it returns a card with errorFalg if K is not valid.*/

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
         //fix the bad one, break one of the good ones
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
      //myHand.resetHand();
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





      //Deck and Hand Testing, Phase 4
      //Asks user for number of hands, between 1-10
      //Creates a deck (unshuffled) and distributes it evenly between hands
      //Resets hands and deck and shuffles cards
      //Again, distributes cards evenly between hands.
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
      int deckPosition = 0; // initial starting point for array of hands
      for (int i = 0; i < 52; i++) {
         if (deckPosition > totalHands - 1) {
            deckPosition = 0; //resets to first hand, 
                              // if last hand position is reached
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
            deckPosition = 0; //resets to first hand, 
                              // if last hand position is reached
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


/*
   Test output:

 Q of hearts
[Invalid]
A of spades

Q of hearts
6 of hearts
[Invalid]

**Testing the hand class**
Hand = (A of hearts, Q of clubs, 9 of spades, 2 of diamonds, A of hearts, Q of clubs, 9 of spades, 2 of diamonds, A of hearts, Q of clubs, 9 of spades, 2 of diamonds, ) 

Testing inspectCard()
** legal **
A of hearts
9 of spades
9 of spades
** illegal **
T of hearts
T of hearts
playing 2 of diamonds
playing 9 of spades
playing Q of clubs
playing A of hearts
playing 2 of diamonds
playing 9 of spades
playing Q of clubs
playing A of hearts
playing 2 of diamonds
playing 9 of spades
playing Q of clubs
playing A of hearts

After playing all cards
Hand = () 

####################
### Deck Testing ###
####################


A of hearts
2 of hearts
3 of hearts
4 of hearts
5 of hearts
6 of hearts
7 of hearts
8 of hearts
9 of hearts
T of hearts
J of hearts
Q of hearts
K of hearts
A of clubs
2 of clubs
3 of clubs
4 of clubs
5 of clubs
6 of clubs
7 of clubs
8 of clubs
9 of clubs
T of clubs
J of clubs
Q of clubs
K of clubs
A of diamonds
2 of diamonds
3 of diamonds
4 of diamonds
5 of diamonds
6 of diamonds
7 of diamonds
8 of diamonds
9 of diamonds
T of diamonds
J of diamonds
Q of diamonds
K of diamonds
A of spades
2 of spades
3 of spades
4 of spades
5 of spades
6 of spades
7 of spades
8 of spades
9 of spades
T of spades
J of spades
Q of spades
K of spades
4 of hearts
K of diamonds
2 of diamonds
6 of clubs
T of clubs
2 of hearts
7 of spades
4 of clubs
J of spades
Q of spades
3 of spades
A of clubs
Q of hearts
A of spades
6 of hearts
5 of diamonds
J of hearts
Q of clubs
6 of spades
4 of diamonds
A of diamonds
J of diamonds
7 of hearts
9 of diamonds
3 of clubs
K of hearts
8 of spades
T of diamonds
8 of clubs
9 of hearts
4 of spades
8 of hearts
9 of spades
J of clubs
2 of spades
K of clubs
5 of hearts
6 of diamonds
T of hearts
5 of clubs
A of hearts
3 of hearts
T of spades
7 of diamonds
9 of clubs
5 of spades
Q of diamonds
K of spades
2 of clubs
7 of clubs
3 of diamonds
8 of diamonds
A of hearts
2 of hearts
3 of hearts
4 of hearts
5 of hearts
6 of hearts
7 of hearts
8 of hearts
9 of hearts
T of hearts
J of hearts
Q of hearts
K of hearts
A of clubs
2 of clubs
3 of clubs
4 of clubs
5 of clubs
6 of clubs
7 of clubs
8 of clubs
9 of clubs
T of clubs
J of clubs
Q of clubs
K of clubs
A of diamonds
2 of diamonds
3 of diamonds
4 of diamonds
5 of diamonds
6 of diamonds
7 of diamonds
8 of diamonds
9 of diamonds
T of diamonds
J of diamonds
Q of diamonds
K of diamonds
A of spades
2 of spades
3 of spades
4 of spades
5 of spades
6 of spades
7 of spades
8 of spades
9 of spades
T of spades
J of spades
Q of spades
K of spades
A of hearts
2 of hearts
3 of hearts
4 of hearts
5 of hearts
6 of hearts
7 of hearts
8 of hearts
9 of hearts
T of hearts
J of hearts
Q of hearts
K of hearts
A of clubs
2 of clubs
3 of clubs
4 of clubs
5 of clubs
6 of clubs
7 of clubs
8 of clubs
9 of clubs
T of clubs
J of clubs
Q of clubs
K of clubs
A of diamonds
2 of diamonds
3 of diamonds
4 of diamonds
5 of diamonds
6 of diamonds
7 of diamonds
8 of diamonds
9 of diamonds
T of diamonds
J of diamonds
Q of diamonds
K of diamonds
A of spades
2 of spades
3 of spades
4 of spades
5 of spades
6 of spades
7 of spades
8 of spades
9 of spades
T of spades
J of spades
Q of spades
K of spades
A of hearts
2 of hearts
3 of hearts
4 of hearts
5 of hearts
6 of hearts
7 of hearts
8 of hearts
9 of hearts
T of hearts
J of hearts
Q of hearts
K of hearts
A of clubs
2 of clubs
3 of clubs
4 of clubs
5 of clubs
6 of clubs
7 of clubs
8 of clubs
9 of clubs
T of clubs
J of clubs
Q of clubs
K of clubs
A of diamonds
2 of diamonds
3 of diamonds
4 of diamonds
5 of diamonds
6 of diamonds
7 of diamonds
8 of diamonds
9 of diamonds
T of diamonds
J of diamonds
Q of diamonds
K of diamonds
A of spades
2 of spades
3 of spades
4 of spades
5 of spades
6 of spades
7 of spades
8 of spades
9 of spades
T of spades
J of spades
Q of spades
K of spades
A of hearts
2 of hearts
3 of hearts
4 of hearts
5 of hearts
6 of hearts
7 of hearts
8 of hearts
9 of hearts
T of hearts
J of hearts
Q of hearts
K of hearts
A of clubs
2 of clubs
3 of clubs
4 of clubs
5 of clubs
6 of clubs
7 of clubs
8 of clubs
9 of clubs
T of clubs
J of clubs
Q of clubs
K of clubs
A of diamonds
2 of diamonds
3 of diamonds
4 of diamonds
5 of diamonds
6 of diamonds
7 of diamonds
8 of diamonds
9 of diamonds
T of diamonds
J of diamonds
Q of diamonds
K of diamonds
A of spades
2 of spades
3 of spades
4 of spades
5 of spades
6 of spades
7 of spades
8 of spades
9 of spades
T of spades
J of spades
Q of spades
K of spades
A of hearts
2 of hearts
3 of hearts
4 of hearts
5 of hearts
6 of hearts
7 of hearts
8 of hearts
9 of hearts
T of hearts
J of hearts
Q of hearts
K of hearts
A of clubs
2 of clubs
3 of clubs
4 of clubs
5 of clubs
6 of clubs
7 of clubs
8 of clubs
9 of clubs
T of clubs
J of clubs
Q of clubs
K of clubs
A of diamonds
2 of diamonds
3 of diamonds
4 of diamonds
5 of diamonds
6 of diamonds
7 of diamonds
8 of diamonds
9 of diamonds
T of diamonds
J of diamonds
Q of diamonds
K of diamonds
A of spades
2 of spades
3 of spades
4 of spades
5 of spades
6 of spades
7 of spades
8 of spades
9 of spades
T of spades
J of spades
Q of spades
K of spades
A of hearts
2 of hearts
3 of hearts
4 of hearts
5 of hearts
6 of hearts
7 of hearts
8 of hearts
9 of hearts
T of hearts
J of hearts
Q of hearts
K of hearts
A of clubs
2 of clubs
3 of clubs
4 of clubs
5 of clubs
6 of clubs
7 of clubs
8 of clubs
9 of clubs
T of clubs
J of clubs
Q of clubs
K of clubs
A of diamonds
2 of diamonds
3 of diamonds
4 of diamonds
5 of diamonds
6 of diamonds
7 of diamonds
8 of diamonds
9 of diamonds
T of diamonds
J of diamonds
Q of diamonds
K of diamonds
A of spades
2 of spades
3 of spades
4 of spades
5 of spades
6 of spades
7 of spades
8 of spades
9 of spades
T of spades
J of spades
Q of spades
K of spades
6 of clubs
T of hearts
5 of hearts
8 of clubs
7 of diamonds
T of hearts
7 of diamonds
K of clubs
T of clubs
4 of clubs
9 of diamonds
8 of spades
3 of clubs
K of diamonds
6 of diamonds
3 of clubs
7 of spades
T of clubs
5 of clubs
3 of spades
Q of hearts
5 of spades
K of clubs
9 of spades
T of spades
A of clubs
2 of hearts
9 of hearts
9 of clubs
Q of diamonds
5 of spades
5 of hearts
3 of clubs
8 of diamonds
T of diamonds
5 of diamonds
4 of hearts
J of spades
8 of hearts
8 of diamonds
7 of hearts
2 of clubs
2 of clubs
8 of clubs
8 of diamonds
2 of diamonds
K of spades
Q of clubs
K of diamonds
4 of clubs
A of hearts
T of clubs
8 of diamonds
6 of diamonds
6 of diamonds
J of spades
A of hearts
T of spades
A of spades
A of hearts
Q of hearts
3 of hearts
K of diamonds
2 of spades
2 of spades
4 of diamonds
8 of spades
9 of hearts
9 of diamonds
4 of clubs
K of spades
Q of clubs
J of hearts
8 of spades
J of diamonds
4 of spades
8 of hearts
9 of clubs
A of spades
9 of spades
4 of diamonds
5 of clubs
T of diamonds
6 of clubs
7 of clubs
2 of diamonds
4 of hearts
9 of hearts
J of clubs
Q of diamonds
K of clubs
7 of clubs
2 of diamonds
8 of diamonds
7 of hearts
9 of spades
Q of clubs
A of spades
4 of diamonds
8 of clubs
8 of hearts
9 of diamonds
6 of clubs
7 of clubs
2 of spades
9 of hearts
T of hearts
5 of spades
K of clubs
7 of hearts
Q of diamonds
3 of spades
6 of spades
J of hearts
T of hearts
A of diamonds
2 of hearts
2 of diamonds
8 of hearts
6 of spades
K of clubs
A of clubs
4 of spades
5 of clubs
7 of spades
3 of hearts
2 of diamonds
4 of diamonds
T of diamonds
8 of clubs
6 of spades
A of spades
6 of clubs
9 of spades
4 of clubs
Q of hearts
J of clubs
Q of spades
6 of diamonds
8 of spades
5 of clubs
5 of spades
J of hearts
Q of spades
K of hearts
7 of diamonds
3 of diamonds
3 of diamonds
6 of hearts
9 of hearts
2 of hearts
8 of clubs
6 of spades
A of diamonds
Q of hearts
J of clubs
8 of hearts
A of clubs
A of hearts
A of clubs
Q of diamonds
T of hearts
4 of hearts
5 of diamonds
5 of clubs
5 of spades
4 of clubs
4 of spades
6 of hearts
5 of clubs
J of diamonds
7 of hearts
J of clubs
Q of clubs
4 of hearts
2 of hearts
4 of spades
T of spades
3 of hearts
K of clubs
Q of clubs
Q of spades
A of diamonds
K of hearts
6 of clubs
K of spades
3 of diamonds
7 of spades
Q of spades
7 of hearts
6 of hearts
K of hearts
4 of clubs
6 of spades
7 of diamonds
K of hearts
A of diamonds
3 of spades
9 of spades
2 of clubs
5 of diamonds
2 of spades
4 of hearts
A of diamonds
8 of hearts
K of hearts
Q of clubs
3 of spades
6 of diamonds
3 of spades
J of clubs
7 of clubs
9 of diamonds
A of clubs
5 of diamonds
A of spades
J of spades
K of spades
5 of hearts
Q of hearts
4 of spades
3 of diamonds
2 of spades
J of hearts
7 of hearts
3 of clubs
Q of diamonds
4 of diamonds
T of clubs
T of diamonds
T of spades
J of spades
7 of diamonds
J of spades
J of diamonds
T of diamonds
2 of hearts
9 of hearts
7 of diamonds
J of hearts
5 of hearts
A of spades
2 of clubs
K of diamonds
8 of diamonds
K of spades
3 of clubs
7 of clubs
Q of hearts
3 of spades
2 of diamonds
7 of spades
K of spades
6 of hearts
3 of diamonds
4 of spades
9 of clubs
6 of clubs
T of clubs
9 of clubs
A of clubs
T of spades
A of diamonds
J of clubs
2 of clubs
T of spades
6 of hearts
J of diamonds
7 of spades
2 of spades
6 of spades
J of spades
6 of diamonds
Q of diamonds
3 of clubs
J of diamonds
Q of spades
3 of hearts
9 of diamonds
8 of spades
5 of hearts
3 of hearts
K of diamonds
8 of clubs
6 of hearts
2 of hearts
9 of clubs
9 of clubs
4 of hearts
K of diamonds
J of diamonds
7 of clubs
5 of diamonds
7 of spades
2 of clubs
A of hearts
5 of hearts
A of hearts
5 of diamonds
4 of diamonds
T of diamonds
J of hearts
9 of diamonds
9 of spades
K of hearts
Q of spades
8 of spades
5 of spades
T of clubs
T of hearts
3 of diamonds
3 of hearts
T of clubs
8 of spades
T of clubs
8 of spades

Deck + Hand Testing
How many hands, 1-10:
0
Invalid number. How many hands, 1-10:
-1
Invalid number. How many hands, 1-10:
20
Invalid number. How many hands, 1-10:
38
Invalid number. How many hands, 1-10:
10
Here are our hand(s) from an unshuffled deck:
Hand = (A of hearts, J of hearts, 8 of clubs, 5 of diamonds, 2 of spades, Q of spades, ) 
Hand = (2 of hearts, Q of hearts, 9 of clubs, 6 of diamonds, 3 of spades, K of spades, ) 
Hand = (3 of hearts, K of hearts, T of clubs, 7 of diamonds, 4 of spades, ) 
Hand = (4 of hearts, A of clubs, J of clubs, 8 of diamonds, 5 of spades, ) 
Hand = (5 of hearts, 2 of clubs, Q of clubs, 9 of diamonds, 6 of spades, ) 
Hand = (6 of hearts, 3 of clubs, K of clubs, T of diamonds, 7 of spades, ) 
Hand = (7 of hearts, 4 of clubs, A of diamonds, J of diamonds, 8 of spades, ) 
Hand = (8 of hearts, 5 of clubs, 2 of diamonds, Q of diamonds, 9 of spades, ) 
Hand = (9 of hearts, 6 of clubs, 3 of diamonds, K of diamonds, T of spades, ) 
Hand = (T of hearts, 7 of clubs, 4 of diamonds, A of spades, J of spades, ) 

Here are our hand(s) from a shuffled deck:
Hand = (6 of hearts, J of hearts, 8 of hearts, A of clubs, 5 of hearts, 8 of clubs, ) 
Hand = (7 of diamonds, 4 of diamonds, 4 of clubs, 6 of clubs, T of clubs, 7 of hearts, ) 
Hand = (3 of clubs, 5 of spades, 9 of hearts, 4 of hearts, K of hearts, ) 
Hand = (8 of diamonds, Q of diamonds, 9 of clubs, A of spades, 9 of spades, ) 
Hand = (K of diamonds, 4 of spades, 3 of spades, 2 of spades, K of spades, ) 
Hand = (5 of diamonds, 6 of diamonds, 7 of clubs, T of diamonds, Q of hearts, ) 
Hand = (J of spades, J of diamonds, 3 of diamonds, A of diamonds, 6 of spades, ) 
Hand = (3 of hearts, 9 of diamonds, K of clubs, 2 of hearts, 2 of clubs, ) 
Hand = (Q of clubs, J of clubs, 2 of diamonds, 7 of spades, 8 of spades, ) 
Hand = (A of hearts, 5 of clubs, Q of spades, T of spades, T of hearts, ) 
 */
