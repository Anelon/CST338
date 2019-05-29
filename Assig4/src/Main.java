

interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);

   public boolean readText(String text);

   public boolean generateImageFromText();
   public boolean translateImageToText();
   public void displayTextToConsole();
   public void displayImageToConsole();
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
               setPixel(i, j, false);
            }
            else if (strData[i].charAt(j)== DataMatrix.BLACK_CHAR)
            {
               setPixel(i, j, true);
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
   public BarcodeImage clone() throws CloneNotSupportedException
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
    *
    *
    * SPEC NOTES, WILL DELETE
    * scan(BarcodeImage image) - a mutator for image.
   Like the constructor;  in fact it is called by the constructor.

   Besides calling the clone() method of the BarcodeImage class,
   this method will do a couple of things including calling

   cleanImage() and then set the actualWidth and actualHeight.
   Because scan() calls clone(), it should deal with the


   CloneNotSupportedException by embeddingthe clone() call within
   a try/catch block.

   using a "throws" clause in the function header since that will
   Don't attempt to hand-off the exception
   not be compatible with the underlying BarcodeIO interface.
   The catches(...) clause can have an empty body that does nothing.
    */
   public boolean scan(BarcodeImage bc)
   {
      //Attempts to clone and catches if clone not supported (uses try catch)
      try {
         this.image = (BarcodeImage) bc.clone();
      }
      catch(CloneNotSupportedException e) {
         return false;
      }

      actualWidth = computeSignalWidth() -2 ;
      actualHeight = 8;
      //testing delete below
      displayImageToConsole();
      //Calls cleanImage
      cleanImage();

      //Sets actualWidth and actualHeight ?



      //actualHeight = 8;
      //computeSignalWidth
      //actualWidth/Height = computed width/height value?


      return true;
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
      int charValue = 0;
      String[] arrayOfStrings = new String[10];
      char[][] arrayOfArraysUndersized = new char[8][text.length()];
      for (int letterIndex = 0; letterIndex < text.length(); ++ letterIndex)
      {
         charValue = text.charAt(letterIndex);

         for (int bitIndex = 0; bitIndex < 8; ++bitIndex)
         {
            int bitPosition = (int)(128/Math.pow(2, bitIndex));

            if (charValue/bitPosition == 1)
               arrayOfArraysUndersized[bitIndex][letterIndex] = '*';
            else
               arrayOfArraysUndersized[bitIndex][letterIndex] = ' ';
            charValue%=bitPosition;
         }
      }

      for (int i = 1; i < 9; ++i)
      {
         arrayOfStrings[i] = new String(arrayOfArraysUndersized[i-1]);
      }

      addBorders(arrayOfStrings);
      image = new BarcodeImage(arrayOfStrings);

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
    */
   private void addBorders(String[] arrayOfStrings)
   {
      int stringLength = text.length();
      char[] topLine = new char[stringLength];
      char[] bottomLine = new char[stringLength];
      for (int i = 0; i < stringLength; ++i)
      {
         if (i%2 == 1)
            topLine[i] = '*';
         else
            topLine[i] = ' ';

         bottomLine[i] = '*';
      }
      arrayOfStrings[0] = new String(topLine);
      arrayOfStrings[9] = new String(bottomLine);

      for (int i = 0; i < 10; ++i)
      {
         if (i % 2 == 0 || i == 9)
            arrayOfStrings[i] = "*" + arrayOfStrings[i] + "*";
         else
            arrayOfStrings[i]= "*" + arrayOfStrings[i] + ' ';
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
      System.out.println("actualWidth " + actualWidth);
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
      int imageWidth = computeSignalWidth();
      int imageHeight = computeSignalHeight();
      //System.out.println(startingX + " " + startingY);
      for (int y = startingY; y <= imageHeight + startingY; ++y)
      {
         for (int x = startingX; x <= imageWidth + startingX; ++x)
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
      //System.out.println("actualHeight = " + actualHeight);
      //System.out.println("actualWidth = " + actualWidth);
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
      int continuousPixels = 0;
      int highest = 0;
      for (int y = BarcodeImage.MAX_HEIGHT-1; y >=0; --y)
      {
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
         {
            if (image.getPixel(y, x))
            {
               ++continuousPixels;
               if (continuousPixels > highest)
                  highest = continuousPixels;
            }
            else
            {
               continuousPixels = 0;
            }
         }
      }
      //hopefully fixes off by one
      return highest -1;
   }

   private int computeSignalHeight()
   {
      int highest = 0;
      for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
      {
         int continuousPixels = 0;
         for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
         {
            if (image.getPixel(y, x))
            {
               ++continuousPixels;
               //System.out.println("pixels " + continuousPixels);
            }
            else
            {
               continuousPixels = 0;
            }
            if (continuousPixels > highest)
               highest = continuousPixels;

         }
      }
      System.out.println("Hightest " + highest);
      return highest;
   }


   /**
    * Moves the given data matrix to the lower left of the
    * available space.
    * <p>
    * NEEDS TESTING
    */
   private void moveImageToLowerLeft()
   {
      System.out.println("Move Lower Left");
      /*
      int startingX = getLeftColumn();
      int startingY = getTopLineOfImage();
      int imageWidth = computeSignalWidth();
      int imageHeight = computeSignalHeight();
      //System.out.println(startingX + " " + startingY);
      for (int y = imageHeight + startingY; y >= startingY; --y)
      {
         for (int x = imageWidth + startingX; x >= startingX; --x)
         {
            boolean pixel = image.getPixel(y,x);
            System.out.print(boolToChar(image.getPixel(y, x)));
            image.setPixel(y,x,false);
            //add 2 for the borders
            int shift = imageHeight - y;
            image.setPixel(BarcodeImage.MAX_HEIGHT - shift, x, pixel);

         }
         System.out.println();
      }
      */

      //System.out.println("Height " + computeSignalHeight());

      //System.out.println("Width " + computeSignalWidth());
      displayRawImage();
      if (getBottomLineOfImage() < BarcodeImage.MAX_HEIGHT-1)
      {
         System.out.println("Shifting Down");
         shiftImageDown();
         System.out.println("Done Shifting");
      }

      displayRawImage();
      while (getLeftColumn() > 0)
      {
         shiftImageLeft();
      }
      System.out.println("Shifted Left");
      displayRawImage();

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
            if (image.getPixel(y, x))
               return y;
/*
            if ( x > 0 && image.getPixel(y, x) != image.getPixel(y, x-1))
            {
               alternatingPixels++;
            }
            if (alternatingPixels == minimumNumberAlternatingPixels)
            {
               return y;
            }*/
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


      for (int y = BarcodeImage.MAX_HEIGHT-1; y > 0; --y) {
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
         {
            if (image.getPixel(y, x)) {
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
         System.out.println("End " + endingRow);
         System.out.println("Start " + startingRow);
         System.out.println("height " + actualHeight);

         for (int y = endingRow; y > startingRow - 1; --y)
         {
            for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
            {
               //drop image all the way to the bottom
               boolean pixel = image.getPixel(y, x);
               image.setPixel(y,x,false);
               //add 2 for the borders
               int shift = actualHeight+2 - (y - startingRow);
               //System.out.println("Shift " + shift);
               image.setPixel(BarcodeImage.MAX_HEIGHT - shift, x, pixel);
            }
         }

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
         int endingColumn = startingColumn + computeSignalWidth();

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


   //Takes the col to read from and returns a character
   //based on the true or false values
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

      
      System.out.println("thing");
      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);

      // First secret message
      System.out.println();
      System.out.println("First secret message");
      dm.displayRawImage();
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // second secret message
      System.out.println();
      System.out.println("Next secret message");
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();

      // create your own message
      System.out.println();
      System.out.println("Our secret message");
      dm.readText("What a great resume builder this is!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      dm.displayImageToConsole();



   }
}

