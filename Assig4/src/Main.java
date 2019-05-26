

interface BarcodeIO
{
   boolean scan(BarcodeImage bc);

   boolean readText(String text);

   boolean generateImageFromText();
   boolean translateImageToText();
   void displayTextToConsole();
   void displayImageToConsole();
}






class BarcodeImage implements Cloneable
{

   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   private boolean[][] imageData;


   /*Instantiates a 2D array (MAX_HEIGHT x MAX_WIDTH)
    * that sets the array as false. */
   public BarcodeImage()
   {
      this.imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];


   }

   /*Converts 1d string array to 2d array of booleans
    * that sets all the WHITE_CHAR to false and all the BLACK_CHAR to true
    * in the 2d boolean array.*/
   BarcodeImage(String[] strData)
   {
      this();
      //System.out.println("words = " + strData.length);
      for(int i = 0; i<strData.length; i++)
      {
         for (int j = 0; j<strData[i].length(); j++)
         {
            if(strData[i].charAt(j)== DataMatrix.WHITE_CHAR)
            {
               setPixel(j, i, false);
            }
            else if (strData[i].charAt(j)== DataMatrix.BLACK_CHAR)
            {
               setPixel(j, i, true);
            }
         }
      }
   }



   /*Accessor method for each bit in the image. */
   public boolean getPixel(int row, int col)
   {
      if(( row>= 0 && row<MAX_HEIGHT) && ( col>= 0 && col <MAX_WIDTH))
      {
         return imageData[row][col];
      }
      return false;


   }

   /*Mutator method for each bit in the image. */
   public boolean setPixel(int row, int col, boolean value)
   {
      if((row>= 0 && row<MAX_HEIGHT) && (col>= 0 && col <MAX_WIDTH))
      {
         imageData[row][col] = value;
         return true;
      }
      return false;


   }


   /*This method overwrites the clone() method in the
    * Cloneable interface. This method casts the the clone()
    * method to the superclass method BarcodeImage.*/
   public BarcodeImage clone()
   {
      try
      {
         BarcodeImage cloned = (BarcodeImage) super.clone();
         for(int i = 0; i<MAX_HEIGHT; i++)
         {
            for(int j = 0; j<MAX_WIDTH; j++)
            {
               cloned.imageData[i][j] = this.imageData[i][j];

            }
         }
         return (BarcodeImage) cloned;

      }
      catch(CloneNotSupportedException e)
      {
         throw new InternalError();
      }

   }

}





class DataMatrix implements BarcodeIO
{

   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth;
   private int actualHeight;

   DataMatrix()
   {
      actualHeight = 0;
      actualWidth = 0;
      text = "";
   }

   DataMatrix(BarcodeImage image)
   {
      if (!scan(image))
      {
         System.out.println("BarcodeImage failed to scan properly. " +
            "Proceed with caution.");
      }
      ;
   }

   DataMatrix(String text)
   {
      if (!readText(text))
      {
         System.out.println("String failed to scan properly. " +
            "Proceed with caution.");
      }
      ;
   }

   DataMatrix(DataMatrix copy)
   {
      for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
      {
         for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
         {
            image.setPixel(y, x, copy.image.getPixel(y, x));
         }
      }
      actualWidth = copy.getActualWidth();
      actualHeight = copy.getActualHeight();
      text = copy.text;
   }


   /**
    * needs to handle cloneNotSupported exception in addition to normal duties.
    * See assignment spec for details
    * @param bc
    * @return
    */
   public boolean scan(BarcodeImage bc)
   {

      actualHeight = 8;
      //actualWidth = computed width value?
      return false;
   }


   public boolean readText(String text)
   {
      //check if the text size could be encoded into a BarcodeImage
      //    -2 to account for needed space for border characters
      if (text.length() > BarcodeImage.MAX_WIDTH - 2)
         return false;
      this.text = text;
      actualHeight = 8;
      actualWidth = text.length();
      return true;
   }


   /**
    * needs to have return value set properly
    * @return
    */
   public boolean generateImageFromText()
   {
      int stringLength = text.length();
      int max = 0;
      String[] arrayOfStrings = new String[stringLength + 2];
      for (int i = 0; i < stringLength; ++i)
      {
         String temp = Integer.toBinaryString(text.charAt(i));
         //replace 1s and 0s in string with proper characters
         temp = temp.replace('0', WHITE_CHAR);
         temp = temp.replace('1', BLACK_CHAR);
         //System.out.println(temp.length() + " " + temp);
         arrayOfStrings[i + 1] = temp;
         //get max string length to encode
         if(temp.length() > max)
            max = temp.length();
      }



      addBorders(arrayOfStrings, max);


      this.image = new BarcodeImage(arrayOfStrings);
      return true;
   }


   /**
    * Adds "borders" around a 2d array consisting of "solid lines"
    * on the far left and bottom borders and "dotted lines" on the
    * far right and top borders.
    *
    * The array of strings is assumed to be initialized with
    * values that correspond to the string whose length is passed.
    * @param arrayOfStrings an array of strings holding the
    *                       binary data for the a string of text
    * @param max size of the largest string within the array
    */
   private void addBorders(String[] arrayOfStrings, int max)
   {
      int stringLength = text.length();
      //add borders around the main string
      for (int i = 0; i < stringLength + 2; i++)
      {
         //border on left solid
         if (i == 0)
         {
            arrayOfStrings[i] = "";
            for(int j = 0; j < max + 2; j++)
               arrayOfStrings[i] += BLACK_CHAR;
         }
         //border on right put black everyother
         else if (i == stringLength + 1)
         {
            arrayOfStrings[i] = "";
            for(int j = 0; j < max + 2; j++)
               if(j % 2 == 0)
                  arrayOfStrings[i] += BLACK_CHAR;
               else
                  arrayOfStrings[i] += WHITE_CHAR;
         }
         //Put border on top and bottom
         else
         {
            //normalize the string lengths by adding leading spaces
            for(int j = arrayOfStrings[i].length(); j < max; j++)
            {
               arrayOfStrings[i] = " " + arrayOfStrings[i];
            }
            //if every other column
            if(i % 2 == 0)
               arrayOfStrings[i] = "*" + arrayOfStrings[i] + "*";
            else
               arrayOfStrings[i] = " " + arrayOfStrings[i] + "*";
         }
      }
   }


   /**
    * needs to have return value set properly
    * @return
    */
   public boolean translateImageToText()
   {
      //read char from column for each actualHeight (-2? for border characters)
      //put chars into string text
      //readCharFromCol(int)
      int startingX = getLeftColumn()+1;
      //remove below when above function is fixed
      startingX = 1;
      int startingY = getTopLineOfImage()+1;
      char[] arrayOfChars = new char[actualWidth];
      int index = 0;
      cleanImage();
      for (int x = startingX; x < startingX + actualWidth; ++x)
      {

         arrayOfChars[x-1] = readCharFromCol(x);
         //System.out.println(x + " " + arrayOfChars[x-1]);
         ++index;
      }
      text = new String(arrayOfChars);
      System.out.println(arrayOfChars.length);
      return false;
   }


   public void displayTextToConsole()
   {
      System.out.println(text);

   }

   public void displayImageToConsole()
   {
      int startingX = getLeftColumn();
      int startingY = getTopLineOfImage();
      //System.out.println(startingX + " " + startingY);
      for (int y = startingY; y <= computeSignalHeight() + startingY; ++y)
      {
         for (int x = startingX; x <= computeSignalWidth() + startingX; ++x)
         {
            System.out.print(boolToChar(image.getPixel(y, x)));
         }
         System.out.println();
      }

   }

   public int getActualHeight()
   {
      return actualHeight;
   }

   public int getActualWidth()
   {
      return actualWidth;
   }

   public void displayRawImage()
   {
      System.out.println("actualHeight = " + actualHeight);
      System.out.println("actualWidth = " + actualWidth);
      for (int x = 0; x < BarcodeImage.MAX_HEIGHT; ++x)
      {
         for (int y = 0; y < BarcodeImage.MAX_WIDTH; ++y)
         {
            if(image.getPixel(x,y))
               System.out.print(BLACK_CHAR);
            else
               System.out.print(WHITE_CHAR);
            //System.out.print(image.getPixel(x, y));
         }
         System.out.println();
      }

   }

   private char boolToChar(boolean value)
   {
      if (value == true)
         return BLACK_CHAR;
      else
         return WHITE_CHAR;
   }

   private void cleanImage()
   {
      moveImageToLowerLeft();
   }





   private int computeSignalWidth()
   {
      return getRightColumn()-getLeftColumn();
   }

   private int computeSignalHeight()
   {
      return getBottomLineOfImage() - getTopLineOfImage();
   }


   /**
    * Moves the given data matrix to the lower left of the
    * available space.
    * <p>
    * NEEDS TESTING
    */
   private void moveImageToLowerLeft()
   {
      System.out.println(computeSignalHeight());

      System.out.println(computeSignalWidth());
      while (getBottomLineOfImage() < BarcodeImage.MAX_HEIGHT-1)
      {
         System.out.println("Shifting Down");
         shiftImageDown();
      }

      while (getLeftColumn() > 0)
      {
         shiftImageLeft();
      }

   }


   /**
    * Finds the first/top row of valid data from the object's BarcodeImage object
    * named image. The object must be a valid, initialized object. If no data
    * is found, -1 is returned to indicate this.
    *
    * @return The first/top row index with valid data.
    */
   private int getTopLineOfImage()
   {
      int minimumNumberAlternatingPixels = 3;
      int alternatingPixels = 0;
      for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
      {
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
         {

            if ( x > 0 && image.getPixel(y, x) != image.getPixel(y, x-1))
            {
               alternatingPixels++;
            }
            if (alternatingPixels == minimumNumberAlternatingPixels)
            {
               return y;
            }
         }
      }
      return -1;
   }


   /**
    * Finds the last/bottom row of valid data from the object's BarcodeImage object
    * named image. The object must be a valid, initialized object. If no data
    * is found, -1 is returned to indicate this.
    *
    * @return The last/bottom row index with valid data.
    */
   private int getBottomLineOfImage()
   {
      int uninterruptedPixels = 0;
      for (int y = BarcodeImage.MAX_HEIGHT; y > 0; --y)
      {
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
         {
            if (image.getPixel(y, x))
            {
               ++uninterruptedPixels;
            }
            else
            {
               uninterruptedPixels = 0;
            }
            if (uninterruptedPixels == computeSignalWidth())
            {
               return y;
            }
         }
      }
      return -1;
   }


   /**
    * Finds the far left/first column of valid data from the object's BarcodeImage object
    * named image. The object must be a valid, initialized object. If no data
    * is found, -1 is returned to indicate this.
    *
    * @return The left/first column index with valid data.
    */
   private int getLeftColumn()
   {
      int minimumValidUninterruptedPixels = 9;
      int uninterruptedPixels = 0;
      for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
      {
         for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
         {
            if (image.getPixel(y, x))
            {
               ++uninterruptedPixels;
            }
            else
            {
               uninterruptedPixels = 0;
            }
            if (uninterruptedPixels >= minimumValidUninterruptedPixels)
               return x;
         }
      }
      return -1;
   }


   /**
    * Finds the far right/last column of valid data from the object's BarcodeImage object
    * named image. The object must be a valid, initialized object. If no data
    * is found, -1 is returned to indicate this.
    *
    * @return The far right/last column index with valid data.
    */
   private int getRightColumn()
   {
      int minimumNumberAlternatingPixels = 3;
      int alternatingPixels = 0;
      for (int x = BarcodeImage.MAX_WIDTH; x > 0; --x)
      {
         for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
         {

            if ( y > 0 && image.getPixel(y, x) != image.getPixel(y-1, x))
            {
               alternatingPixels++;
            }
            if (alternatingPixels >= minimumNumberAlternatingPixels)
            {
               return x;
            }
         }
      }
      return -1;
   }


   /**
    * Shifts the valid "image" down in the "frame" one row if space
    * is available, where the image is a visual representation of the
    * BarcodeImage object and the frame is a grid of
    * BarcodeImage.MAX_WIDTH x BarcodeImage.MAX_HEIGHT
    */
   private void shiftImageDown()
   {
      int endingRow = getBottomLineOfImage();
      if (endingRow < BarcodeImage.MAX_HEIGHT)
      {
         int startingRow = getTopLineOfImage();

         for (int y = endingRow; y > startingRow - 1; --y)
         {
            for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
            {
               //drop image all the way to the bottom
               boolean pixel = image.getPixel(y, x);
               image.setPixel(y,x,false);
               int shift = actualHeight - y + 2;
               image.setPixel(BarcodeImage.MAX_HEIGHT - shift, x, pixel);
            }
         }

         /*
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x) {
            image.setPixel(startingRow, x, false);
         }
         */
      }
   }


   /**
    * Shifts the valid "image" left in the "frame" one column if space
    * is available, where the image is a visual representation of the
    * BarcodeImage object and the frame is a grid of
    * BarcodeImage.MAX_WIDTH x BarcodeImage.MAX_HEIGHT
    */
   private void shiftImageLeft()
   {

      int startingColumn = getLeftColumn();
      if (startingColumn > 0)
      {
         int endingColumn = getRightColumn();

         for (int x = startingColumn; x < endingColumn; ++x)
         {
            for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
            {
               image.setPixel(y, x, image.getPixel(y, x + 1));
            }
         }

         for (int y = 0; y < BarcodeImage.MAX_WIDTH; ++y)
         {
            image.setPixel(y, endingColumn, false);
         }
      }
   }


   //Takes the col to read from and returns a character based on the true or false values
   private char readCharFromCol(int col)
   {
      char temp = 0;
      char one = 1;
      //for all of the spots in that col starting at the
      //bottom because thats the ones bit (-2 to account for border)
      for (int y = BarcodeImage.MAX_HEIGHT - 2;
           y > (BarcodeImage.MAX_HEIGHT - actualHeight) - 2; --y)
      {
         //if the pixel is true add to temp char
         if (image.getPixel(y, col))
            temp |= one;
         //left shift the one bit
         one <<= 1;
      }
      return temp;
   }


   /**
    * needs to have return value checked better
    * @param col
    * @param charCodeValue
    * @return
    */
   private boolean writeCharToCol(int col, int charCodeValue)
   {
      try
      {
         int currentBinaryPosition = 128;

         for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
         {

            image.setPixel(y, col, charCodeValue / currentBinaryPosition == 1);
            charCodeValue %= currentBinaryPosition;
            currentBinaryPosition /= 2;
         }
      }
      catch (Exception e)
      {
         return false;
      }
      return true;
   }

   private void clearImage()
   {
      for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
      {
         for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
         {
            image.setPixel(y, x, false);
         }
      }

   }
}

public class Main
{
   public static void main(String[] args)
   {
      //not used right now can be used for testing decoding
      String[] scanForText =
         {
            "* * * * * * * * * * * * * * * * * *",
            "*                                 *",
            "***** ** * **** ****** ** **** **  ",
            "* **************      *************",
            "**  *  *        *  *   *        *  ",
            "* **  *     **    * *   * ****   **",
            "**         ****   * ** ** ***   ** ",
            "*   *  *   ***  *       *  ***   **",
            "*  ** ** * ***  ***  *  *  *** *   ",
            "***********************************"
         };
      //used for testing encoding of text
      String testText = "Hello World";
      DataMatrix test = new DataMatrix(testText);
      //Convert text to image
      test.generateImageFromText();
      //test.displayRawImage();
      test.displayImageToConsole();
      //convert image back to text
      //System.out.println("image:");
      test.translateImageToText();
      //test.displayRawImage();
      //test.displayImageToConsole();
      test.displayTextToConsole();

      /*
      //his main to be implemented later when functions are more complete
      String[] sImageIn =
      {
         "                                               ",
         "                                               ",
         "                                               ",
         "     * * * * * * * * * * * * * * * * * * * * * ",
         "     *                                       * ",
         "     ****** **** ****** ******* ** *** *****   ",
         "     *     *    ****************************** ",
         "     * **    * *        **  *    * * *   *     ",
         "     *   *    *  *****    *   * *   *  **  *** ",
         "     *  **     * *** **   **  *    **  ***  *  ",
         "     ***  * **   **  *   ****    *  *  ** * ** ",
         "     *****  ***  *  * *   ** ** **  *   * *    ",
         "     ***************************************** ",
         "                                               ",
         "                                               ",
         "                                               "

      };



      String[] sImageIn_2 =
      {
         "                                          ",
         "                                          ",
         "* * * * * * * * * * * * * * * * * * *     ",
         "*                                    *    ",
         "**** *** **   ***** ****   *********      ",
         "* ************ ************ **********    ",
         "** *      *    *  * * *         * *       ",
         "***   *  *           * **    *      **    ",
         "* ** * *  *   * * * **  *   ***   ***     ",
         "* *           **    *****  *   **   **    ",
         "****  *  * *  * **  ** *   ** *  * *      ",
         "**************************************    ",
         "                                          ",
         "                                          ",
         "                                          ",
         "                                          "

      };

      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);

      // First secret message
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // second secret message
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // create your own message
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();
      */

   }
}

