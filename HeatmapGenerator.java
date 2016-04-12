import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

// Use 2-D array to create heat map

public class HeatmapGenerator{

   private static String filepath;
   //private static 

   public static void generateMap(int[][] array){
      // Use helper functions to overlay points of kills onto map
      // and then generate an image file
      int[][] arr1 = normalizePoints(array);
      int[][] arr2 = normalizePointsWithNeighborAveraging(array);
      
      try{
           createTestImageFromGraphics();
           buildHeatmap(arr1);
           buildHeatmap(arr2, "neighbor_avgs.png");
        }catch(IOException e){
            e.printStackTrace();
        }
   }
   
   private static void overlayPoints(int[][] array){
      // Paint graphics to image file here
      /*
      http://stackoverflow.com/questions/2318020/merging-two-images
      
      File path = ... // base path of the images

      // load source images
      BufferedImage image = ImageIO.read(new File(path, "image.png"));
      BufferedImage overlay = ImageIO.read(new File(path, "overlay.png"));
      
      // create the new image, canvas size is the max. of both image sizes
      int w = Math.max(image.getWidth(), overlay.getWidth());
      int h = Math.max(image.getHeight(), overlay.getHeight());
      BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      
      // paint both images, preserving the alpha channels
      Graphics g = combined.getGraphics();
      g.drawImage(image, 0, 0, null);
      g.drawImage(overlay, 0, 0, null);
      
      // Save as new image
      ImageIO.write(combined, "PNG", new File(path, "combined.png"));
      
      */
   }
   
   // Normalize points to a 255 scale to be used for RGB color intensity
   public static int[][] normalizePoints(int[][] array){
        int max = -1;
        
        // Find max of array in order to normalize the rest of the points
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length; j++){
               if(array[i][j] > max){
                  max = array[i][j];
               }
            }
        }
        
        // Normalize points to a 255 scale
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[i].length; j++){
               array[i][j] = (int)(255 * array[i][j] / max);
            }
        }
        
        return array;
   }
   
   // Normalize values of all points to the relative averages of their closest 8 neighbors
   // Only do a single pass to start off
   public static int[][] normalizePointsWithNeighborAveraging(int[][] array){
      // TODO: Finish this function lol
      int[][] ret = new int[array.length][array[0].length];
      
      int temp = 0;
      int tempAvg = 0;
      int neighbors = 0;
      for(int i = 0; i < array.length; i++){
         for(int j = 0; j < array[0].length; j++){
            tempAvg = 0;
            neighbors = 0;
            // Check all surrounding cells (8 total)
            // Top Left
            temp = arrayIndex(array, i, j, i - 1, j - 1);
            if(temp != -1){tempAvg+=temp; neighbors++;}
            
            // Top Mid
            temp = arrayIndex(array, i, j, i, j - 1);
            if(temp != -1){tempAvg+=temp; neighbors++;}
            
            // Top Right
            temp = arrayIndex(array, i, j, i + 1, j - 1);
            if(temp != -1){tempAvg+=temp; neighbors++;}
            
            
            // Mid Left
            temp = arrayIndex(array, i, j, i - 1, j);
            if(temp != -1){tempAvg+=temp; neighbors++;}
            
            // Mid Right
            temp = arrayIndex(array, i, j, i + 1, j);
            if(temp != -1){tempAvg+=temp; neighbors++;}
            
            
            // Bot Left
            temp = arrayIndex(array, i, j, i - 1, j + 1);
            if(temp != -1){tempAvg+=temp; neighbors++;}
            
            // Bot Mid
            temp = arrayIndex(array, i, j, i, j + 1);
            if(temp != -1){tempAvg+=temp; neighbors++;}
            
            // Bot Right
            temp = arrayIndex(array, i, j, i + 1, j + 1);
            if(temp != -1){tempAvg+=temp; neighbors++;}
            
            tempAvg = (int)(tempAvg / neighbors);
            ret[i][j] = tempAvg;
            
            //print("ret[" + i + "][" + j + "] = " + ret[i][j] + " and has " + neighbors + " neighbors.\n");
         }
      }
      
      return ret;
   }
   
   private static int arrayIndex(int[][] array, int curX, int curY, int neiX, int neiY){
      int ret = -1;
      try{
         ret = array[neiX][neiY];
      }catch(IndexOutOfBoundsException e){
         // Do nothing here
      }
      return ret;
   }
   
   private static void buildHeatmap(int[][] map, String name){
      // Create BufferedImage of 1:1 scale from map
      BufferedImage bf = new BufferedImage(map.length * 5, map[0].length * 5, BufferedImage.TYPE_INT_RGB);
      
      // Create a graphics object used to draw into the buffered image
      Graphics2D g2d = bf.createGraphics();
      
      // Fill image at rect(i, j) with appropriate color
      for(int i = 0; i < map.length; i++){
         for(int j = 0; j < map[i].length; j++){
            g2d.setColor(getColorFromDensity(map[i][j]));
            g2d.fillRect(i * 5, j * 5, 5, 5);
            //print("Filling rect[" + (i * 10) + "][" + (j * 10) + "] with " + map[i][j] + "\n");
         }
      }
      
      // Disposes graphics context
      // Releases system resources
      g2d.dispose();
      
      // Save as png
      File file = new File(name);
      try{
         ImageIO.write(bf, "png", file);
      }catch(IOException e){
         e.printStackTrace();
      }
   }
   
   private static void buildHeatmap(int[][] map) throws IOException{
      buildHeatmap(map, "map.png");
   }
   
   private static Color getColorFromDensity(int x){
      /* List of Colors and respective ranges
            * White        : 0,31
            * Light Gray   : 32,63
            * Green        : 64,95
            * Yellow       : 96,127
            * Orange       : 128,159
            * Red          : 160,191
            * Pink         : 192,223
            * Magenta      : 224,255
      */
   
      Color c = new Color(255,255,255,255);
      if(x >= 0 && x <= 31){
         c = new Color(255,255,255,255);
      }else if(x > 31 && x <= 63){
         c = Color.lightGray;
      }else if(x > 63 && x <= 95){
         c = Color.green;
      }else if(x > 95 && x <= 127){
         c = Color.yellow;
      }else if(x > 127 && x <= 159){
         c =  Color.orange;
      }else if(x > 159 && x <= 191){
         c =  Color.red;
      }else if(x > 191 && x <= 223){
         c = Color.pink;
      }else if(x > 223 && x <= 255){
         c = Color.magenta;
      }
      
      // Set the alpha value
      int alpha = 255 - (255 - x) % 32;
      c = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha); 
      
      return c;
   }
   
   private static void createTestImageFromGraphics() throws IOException{
      int width = 255;
      int height = width;
      
      // Constructs BufferedImage from a predefined image type
      BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      
      // Create a graphics object used to draw into the buffered image
      Graphics2D g2d = bf.createGraphics();
      
      // Fill image with all white
      g2d.setColor(Color.white);
      g2d.fillRect(0, 0, width, height);
      
      // Create a circle with black
      g2d.setColor(Color.black);
      g2d.fillOval(0, 0, width, height);
      
      // Create string with yellow
      g2d.setColor(Color.yellow);
      g2d.drawString("Test String", 50, 120);
      
      // Disposes graphics context
      // Releases sytem resources
      g2d.dispose();
      
      // Save as PNG
      File file = new File("testimage.png");
      ImageIO.write(bf, "png", file);
      
      // Save as JPEG
      file = new File("testimage.jpg");
      ImageIO.write(bf, "jpg", file);
   } 
   
   private static void print(String s){
      System.out.print(s);
   }

}