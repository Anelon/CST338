/*
Team SAGA - Shelly Sun, Andrew Bell, Greg Brown, Andrew Terrado
5-28-2019

The following program contains the tools for a basic
barcode scanner. The BarcodeImage class functions as
an advanced 2d array to hold data corresponding
to a string. The DataMatrix class functions as the 
core of the tool, giving functionality to scan
and decode barcode images to text as well as 
the reverse case.


 */

interface BarcodeIO
{
   public boolean scan(BarcodeImage bc);

   public boolean readText(String text);

   public boolean generateImageFromText();
   public boolean translateImageToText();
   public void displayTextToConsole();
   public void displayImageToConsole();
}






/**
 * Class that holds the pixel-based data.  Functions
 * as an enhanced 2d array.
 */
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






/**
 * Combined class that holds a BarcodeImage and String
 * as primary data. Only one is needed as a source to
 * create the other.  Contains public methods to allow for
 * 'scanning' to decode BarcodeImages and 'reading' to
 * create BarcodeImages from text.
 */
class DataMatrix implements BarcodeIO
{

   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth;
   private int actualHeight;


   /**
    * Empty constructor
    */
   DataMatrix()
   {
      actualHeight = 0;
      actualWidth = 0;
      text = "";
   }


   /**
    * Constructor to create an object from a
    * single BarcodeImage source.
    * @param image BarcodeImage object to use as
    *              the base for the DataMatrix
    */
   DataMatrix(BarcodeImage image)
   {
      if (!scan(image))
      {

         System.out.println("BarcodeImage failed to scan properly. " +
            "Proceed with caution.");
      }

   }


   /**
    * Constructor to create an object from a single
    * single String source.
    * @param text String object to use as
    *             the base for the DataMatrix
    */
   DataMatrix(String text)
   {
      if (!readText(text))
      {
         System.out.println("String failed to scan properly. " +
            "Proceed with caution.");
      }

   }


   /**
    * Copy constructor
    * @param copy DataMatrix object to be copied.
    */
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
    * Takes a barcode and stores the data in the object's
    * image variable. The image is "cleaned" for conversion
    * to text. Returns false if cloneNotSupportedException
    * is thrown.
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
      cleanImage();
      return true;
   }


   /**
    * Reads a string and stores it in the DataMatrix'
    * text variable.
    * @param text String to be read and stored.
    * @return False is returned if the string
    * is too large to be stored.
    */
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
    * Uses the string value stored in the object's text
    * variable to generate the corresponding BarcodeImage.
    * Assumes text is initialized to the desired string value.
    *
    * @return Returns false if any exception is thrown.
    */
   public boolean generateImageFromText()
   {
      try {
         int charValue;
         String[] arrayOfStrings = new String[10];
         char[][] arrayOfArraysUndersized = new char[8][text.length()];
         for (int letterIndex = 0; letterIndex < text.length(); ++letterIndex) {
            charValue = text.charAt(letterIndex);

            for (int bitIndex = 0; bitIndex < 8; ++bitIndex) {
               int bitPosition = (int) (128 / Math.pow(2, bitIndex));

               if (charValue / bitPosition == 1)
                  arrayOfArraysUndersized[bitIndex][letterIndex] = '*';
               else
                  arrayOfArraysUndersized[bitIndex][letterIndex] = ' ';
               charValue %= bitPosition;
            }
         }

         for (int i = 1; i < 9; ++i) {
            arrayOfStrings[i] = new String(arrayOfArraysUndersized[i - 1]);
         }

         addBorders(arrayOfStrings);
         image = new BarcodeImage(arrayOfStrings);

         return true;
      }
      catch (Exception e)
      {
         return false;
      }

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
    * Sets the object's text variable by reading
    * the stored BarcodeImage.  text will be set
    * to the BarcodeImage's corresponding string.
    * Assumes BarcodeImage is set to the correct
    * value.
    * @return Returns false if the barcode's width
    * is less than 1.
    */
   public boolean translateImageToText()
   {
      if (actualWidth < 1)
      {
         return false;
      }
      int startingX = 1;
      char[] arrayOfChars = new char[actualWidth];
      cleanImage();
      for (int x = startingX; x < startingX + actualWidth; ++x)
      {
         arrayOfChars[x-1] = readCharFromCol(x);
      }
      text = new String(arrayOfChars);
      return true;
   }


   /**
    * Displays the object's text variable
    * to the console.
    */
   public void displayTextToConsole()
   {
      System.out.println(text);

   }


   /**
    * Displays the object's BarcodeImage to
    * the console as a 2d array
    */
   public void displayImageToConsole()
   {
      int startingX = getLeftColumn();
      int startingY = getTopLineOfImage();
      int imageWidth = computeSignalWidth();
      int imageHeight = computeSignalHeight();
      for (int y = startingY; y <= imageHeight + startingY; ++y)
      {
         for (int x = startingX; x <= imageWidth + startingX; ++x)
         {
            System.out.print(boolToChar(image.getPixel(y, x)));
         }
         System.out.println();
      }

   }






   /**
    * Returns the height of
    * the BarcodeImage's 2d array
    *
    * @return the height of the height of
    *     * the BarcodeImage's 2d array
    */
   public int getActualHeight()
   {
      return actualHeight;
   }






   /**
    * Returns the width of
    * the BarcodeImage's 2d array
    *
    * @return the width of the height of
    *     * the BarcodeImage's 2d array
    */
   public int getActualWidth()
   {
      return actualWidth;
   }






   /**
    * Displays the "raw" image of the BarcodeImage
    * object, excess whitespace included.
    */
   public void displayRawImage()
   {
      for (int x = 0; x < BarcodeImage.MAX_HEIGHT; ++x)
      {
         for (int y = 0; y < BarcodeImage.MAX_WIDTH; ++y)
         {
            if(image.getPixel(x,y))
               System.out.print(BLACK_CHAR);
            else
               System.out.print(WHITE_CHAR);
         }
         System.out.println();
      }

   }


   /**
    * Converts "true" values to '*' and
    * "false" values to ' ' for display.
    * @param value Any boolean value
    * @return '*' if true, ' ' if false
    */
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






   /**
    * Calculates the signal width of the BarcodeImage
    * based on the bottom "spine" of the signal.  The
    * width includes the border.
    * @return The width of the signal including border
    */
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
      return highest -1;
   }






   /**
    * Calculates the signal height of the BarcodeImage
    * based on the bottom "spine" of the signal.  The
    * width includes the border.
    * @return The height of the signal including border
    */
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
            }
            else
            {
               continuousPixels = 0;
            }
            if (continuousPixels > highest)
               highest = continuousPixels;

         }
      }
      return highest;
   }






   /**
    * Moves the given BarcodeImage signal
    * to the lower left of the available space.
    */
   private void moveImageToLowerLeft()
   {
      if (getBottomLineOfImage() < BarcodeImage.MAX_HEIGHT-1)
      {
         shiftImageDown();
      }

      if (getLeftColumn() > 0)
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
      for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
      {
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
         {
            if (image.getPixel(y, x))
               return y;
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

         for (int y = endingRow; y > startingRow - 1; --y)
         {
            for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
            {
               //drop image all the way to the bottom
               boolean pixel = image.getPixel(y, x);
               image.setPixel(y,x,false);
               //add 2 for the borders
               int shift = actualHeight+2 - (y - startingRow);
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

         for (int x = startingColumn; x < endingColumn + 1; ++x)
         {
            for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
            {
               //drop image all the way to the bottom
               boolean pixel = image.getPixel(y, x);
               //clear current pixel
               image.setPixel(y, x, false);
               //add 2 for the borders
               int shift = x - startingColumn;
               image.setPixel(y, shift, pixel);
            }
         }
      }
   }


   //Takes the col to read from and returns a character
   //based on the true or false values

   /**
    * Takes the BarcodeImage column to read from and
    * returns a  character based on the true/false values
    * @param col The column to be read from
    * @return A character based on the data in the column
    */
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
}







public class Main
{
   public static void main(String[] args)
   {

      //used for testing encoding of text
      String testText = "Hello World";
      DataMatrix test = new DataMatrix(testText);
      //Convert text to image
      test.generateImageFromText();
      test.displayImageToConsole();
      //convert image back to text
      test.translateImageToText();
      test.displayTextToConsole();


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
      System.out.println();
      System.out.println("First secret message");
      //dm.displayRawImage();
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

