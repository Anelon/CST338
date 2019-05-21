





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
      BarcodeImage(String[] strData)
      {

      }

      boolean getPixel(int row, int col)
      {
         return false;
      }

      boolean setPixel(int row, int col, boolean value)
      {
         return false;
      }


      private int checkSize(String[] data)
      {
         return 0;
      }

      public BarcodeImage clone()
      {
         return new BarcodeImage(new String[]{"", ""});
      }

      public static final int MAX_HEIGHT = 30;
      public static final int MAX_WIDTH = 65;
      private boolean[][] imageData;

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

      }

      DataMatrix(BarcodeImage image)
      {

      }

      DataMatrix(String text)
      {

      }

      DataMatrix(DataMatrix copy)
      {

      }

      public boolean scan(BarcodeImage bc) {
         return false;
      }


      public boolean readText(String text) {
         return false;
      }


      public boolean generateImageFromText() {
         return false;
      }


      public boolean translateImageToText() {
         return false;
      }


      public void displayTextToConsole() {

      }

      public void displayImageToConsole() {

      }

      public int getActualHeight() {
         return actualHeight;
      }

      public int getActualWidth() {
         return actualWidth;
      }
      
      public void displayRawImage()
      {
         
      }

      private void cleanImage()
      {

      }

      private char readCharFromCol(int col)
      {
         return '\0';
      }

      private boolean WriteCharToCol(int col, int charCode)
      {
         return false;
      }

      private void clearImage()
      {
         
      }
   }


public class Main {
   public static void main(String[] args)
   {

   }
}
