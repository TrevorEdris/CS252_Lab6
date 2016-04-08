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
   }
   
   private static void overlayPoints(int[][] array){
      // Paint graphics to image file here
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
        
        try{
           createTestImageFromGraphics();
           buildHeatmap(array);
        }catch(IOException e){
            e.printStackTrace();
        }
        
        return array;
   }
   
   private static void buildHeatmap(int[][] map) throws IOException{
      // Create BufferedImage of 1:1 scale from map
      BufferedImage bf = new BufferedImage(map.length * 10, map[0].length * 10, BufferedImage.TYPE_INT_RGB);
      
      // Create a graphics object used to draw into the buffered image
      Graphics2D g2d = bf.createGraphics();
      
      // Fill image at rect(i, j) with appropriate color
      for(int i = 0; i < map.length; i++){
         for(int j = 0; j < map[i].length; j++){
            g2d.setColor(getColorFromDensity(map[i][j]));
            g2d.fillRect(i * 10, j * 10, 10, 10);
            //print("Filling rect[" + (i * 10) + "][" + (j * 10) + "] with " + map[i][j] + "\n");
         }
      }
      
      // Disposes graphics context
      // Releases system resources
      g2d.dispose();
      
      // Save as png
      File file = new File("map.png");
      ImageIO.write(bf, "png", file);
   }
   
   private static Color getColorFromDensity(int x){
      if(x >= 0 && x <= 51){
         return Color.white;
      }else if(x > 51 && x <= 102){
         return Color.green;
      }else if(x > 102 && x <= 153){
         return Color.yellow;
      }else if(x > 153 && x <= 204){
         return Color.orange;
      }else if(x > 204){
         return Color.red;
      }else{
         return Color.black;
      }
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