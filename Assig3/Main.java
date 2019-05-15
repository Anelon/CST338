/** Team SAGA */
/** Card Deck */
import java.util.Scanner;
import java.text.DecimalFormat;

class Card
{
   enum Suit {spade, heart, club, diamond};
   private char value;
   private Suit suit;

   //default constructor
   public Card() 
   {
      value = '0';
      suit = Suit.spade;
   }

   //card to string converter
   public String toString()
   {
      return "Card";
   }
}

class Hand
{
   //need to change the ammount of cards (or use an arrayList?)
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
