import javax.swing.*;
import java.awt.*;

public class Main
{
   // static for the 57 icons and their corresponding labels
   // normally we would not have a separate label for each card, but
   // if we want to display all at once using labels, we need to.

   static final int NUM_CARD_IMAGES = 57; // 52 + 4 jokers + 1 back-of-card image
   static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];

   static void loadCardIcons()
   {
      int iconIndex = 0;
      for (int cardNum = 1; cardNum < 15; ++cardNum)
      {
         for (int suitNum = 0; suitNum < 4; ++suitNum)
         {
            String filename = turnIntIntoCardValue(cardNum) +
               turnIntIntoCardSuit(suitNum) + ".gif";
            ImageIcon cardPic = new ImageIcon("images\\"+filename);
            icon[iconIndex] = cardPic;
            ++iconIndex;
         }
      }
      icon[NUM_CARD_IMAGES-1] = new ImageIcon("images\\BK.gif");
   }

   // turns 0 - 13 into "A", "2", "3", ... "Q", "K", "X"
   static String turnIntIntoCardValue(int cardNum)
   {
      char cardValue = (char)('0' + cardNum);
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
         case 14: {
            cardValue = 'X';
            break;
         }
      }
      return new String(new char[]{cardValue});
   }

   // turns 0 - 3 into "C", "D", "H", "S"
   static String turnIntIntoCardSuit(int toBeConverted)
   {
      switch (toBeConverted)
      {
         case 0:
            return "H";
         case 1:
            return "C";
         case 2:
            return "D";
         default:
            return "S";
      }   }

   // a simple main to throw all the JLabels out there for the world to see
   public static void main(String[] args)
   {
      int k;

      // prepare the image icon array
      loadCardIcons();

      // establish main frame in which program will run
      JFrame frmMyWindow = new JFrame("Card Room");
      frmMyWindow.setSize(1150, 650);
      frmMyWindow.setLocationRelativeTo(null);
      frmMyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // set up layout which will control placement of buttons, etc.
      FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 20);
      frmMyWindow.setLayout(layout);

      // prepare the image label array
      JLabel[] labels = new JLabel[NUM_CARD_IMAGES];
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         labels[k] = new JLabel(icon[k]);

      // place your 3 controls into frame
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         frmMyWindow.add(labels[k]);

      // show everything to the user
      frmMyWindow.setVisible(true);
   }
}
