

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
   public BarcodeImage() {
      this.imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
      this.imageData[MAX_HEIGHT][MAX_WIDTH] = false;
      
   }
 
   /*Converts 1d string array to 2d array of booleans
    * that sets all the WHITE_CHAR to false and all the BLACK_CHAR to true 
    * in the 2d boolean array.*/   
   BarcodeImage(String[] strData)
   {
      for(int i = 0; i<strData.length; i++) {
         for (int j = 0; j<strData[i].length(); j++) {
            if(strData[i].charAt(j)== DataMatrix.WHITE_CHAR) {
               setPixel(i, j, false);
            }else if (strData[i].charAt(j)== DataMatrix.BLACK_CHAR) {
               setPixel(i, j, true);
            }
         }
      }
   }
   
      

   /*Accessor method for each bit in the image. */
   public boolean getPixel(int row, int col)
   {
      if((row<0 || row>MAX_HEIGHT) && (col<0 || col >MAX_WIDTH)) {
         return this.imageData[row][col];
      }else {
         return false;         
      }
      
   }
   
   /*Mutator method for each bit in the image. */
   public boolean setPixel(int row, int col, boolean value)
   {
      if((row<0 ||row>MAX_HEIGHT) && (col<0 || col >MAX_WIDTH)) {
         this.imageData[row][col] = value;
      }
         return false;             
      
   }


   /*This method overwrites the clone() method in the
    * Cloneable interface.*/
   public BarcodeImage clone()
   {
      BarcodeImage cloned = new BarcodeImage();
      for(int i = 0; i<MAX_WIDTH; i++)
      {
         for(int j= 0; j<MAX_HEIGHT; j++) {
            this.imageData[i][j] = cloned.imageData[i][j];
            
         }
         
      }
      return cloned;
      
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
      this.image = image;
      if (!scan(image))
      {
         System.out.println("BarcodeImage failed to scan properly. " +
                            "Proceed with caution.");
      };
   }

   DataMatrix(String text)
   {
      this.text = text;
      if (!readText(text))
      {
         System.out.println("String failed to scan properly. " +
                            "Proceed with caution.");
      };
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

   public boolean scan(BarcodeImage bc)
   {
      return false;
   }


   public boolean readText(String text)
   {
      //check if the text size could be encoded into a BarcodeImage
      //    -2 to account for needed space for border characters
      if (text.length() > BarcodeImage.MAX_WIDTH - 2)
         return false;
      this.text = text;
		return true;
   }


   public boolean generateImageFromText()
   {
      //for each char in text write data to col, place black on left and bottom
      //also put every other on top and right
      //WriteCharToCol(int, char)
      return false;
   }


   public boolean translateImageToText()
   {
      //read char from column for each actualHeight (-2? for border characters)
      //put chars into string text
      //readCharFromCol(int)
      return false;
   }


   public void displayTextToConsole()
   {

   }

   public void displayImageToConsole()
   {

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

   }

   private void cleanImage()
   {

   }


   /**
    * Moves the given data matrix to the lower left of the
    * available space.
    *
    * NEEDS TESTING
    *
    */
   private void moveImageToLowerLeft()
   {
      while (getBottomLineOfImage() < BarcodeImage.MAX_HEIGHT)
      {
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
      for (int y = 0; y < BarcodeImage.MAX_HEIGHT; ++y)
      {
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
         {
            if (image.getPixel(y, x))
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
      for (int y = BarcodeImage.MAX_HEIGHT-1; y > 0; --y)
      {
         for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
         {
            if (image.getPixel(y, x))
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
      for (int x = 0; x < BarcodeImage.MAX_HEIGHT; ++x)
      {
         for (int y = 0; y < BarcodeImage.MAX_WIDTH; ++y)
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
    * Finds the far right/last column of valid data from the object's BarcodeImage object
    * named image. The object must be a valid, initialized object. If no data
    * is found, -1 is returned to indicate this.
    *
    * @return The far right/last column index with valid data.
    */
   private int getRightColumn()
   {
      for (int x = BarcodeImage.MAX_HEIGHT-1; x > 0; --x)
      {
         for (int y = 0; y < BarcodeImage.MAX_WIDTH; ++y)
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
      if (endingRow < BarcodeImage.MAX_HEIGHT-1)
      {
         int startingRow = getTopLineOfImage();

         for (int y = endingRow; y > startingRow; --y)
         {
            for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
            {
               image.setPixel(y, x, image.getPixel(y - 1, x));
            }
         }

         for (int x = 0; x < BarcodeImage.MAX_WIDTH; ++x)
         {
            image.setPixel(startingRow, x, false);
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


	//Takes the col to read from and returns a character baised on the true or false values
   private char readCharFromCol(int col)
   {
		char temp = 0;
		char one = 1;
		//for all of the spots in that col starting at the bottom because thats the ones bit
		for (int y = BarcodeImage.MAX_HEIGHT; y > BarcodeImage.MAX_HEIGHT - actualHeight; --y)
		{
			//if the pixel is true add to temp char
			if(image.getPixel(y,col))
				temp |= one;
			//left shift the one bit
			one <<= 1; 
		}
		return temp;
   }

   private boolean WriteCharToCol(int col, int charCode)
   {
      return false;
   }

   private void clearImage()
   {

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
