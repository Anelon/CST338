/** Team SAGA */
/** Card Deck */
import java.util.Scanner;
import java.text.DecimalFormat;

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
