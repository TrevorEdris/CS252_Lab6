import java.util.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.nio.file.*;
import java.awt.*;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import javax.imageio.ImageIO;

/*
      At each kill index, increase the count at the index by 3
      At each neighboring index, increase the count at the index by 2
      At each neighbor's neighbor index, increase the count at the index by 1
      
      Normalize with normalize functions
      
*/
public class HeatmapGenerator{


//****************************************************************************************************************************
//                      GLOBAL VARIABLES
//****************************************************************************************************************************
   // Offsets used in the array to map kills to correct locations
   private static int xOffset, yOffset;
   
   // Length of the 2-D array
   private static int lengthX, lengthY;
   
   public enum Map{
      //Map       (ID,maxX,maxY,minX,minY,xOff,yOff,
	  // 		   refImgName,refImgW,refImgH,refImgScaleX,refImgScaleY,refImgOffX,refImgOffY)
      Altitude    ("ID", 28,10,-28,-10,28,10, 
    		  "altitude.jpg",1796,724,5,5,188,176), 
      
      Coliseum    ("cebd854f-f206-11e4-b46e-24be05e24f7e", 26,18,-26,-13,26,13,
    		  "coliseum.jpg",1868,1326,34,38,280,110), 
      
      Crossfire   ("c7edbf0f-f206-11e4-aa52-24be05e24f7e", 30,15,-30,-15,30,15,
    		  "crossfire.jpg",1716,1065,28,27,287,300), 
      
      Eden        ("cd844200-f206-11e4-9393-24be05e24f7e", 23,22,-11,-22,11,22,
    		  "eden.jpg",1379,1359,40,30,396,153), 
      
      Empire      ("cdb934b0-f206-11e4-8810-24be05e24f7e", 24,19,-11,-19,11,19,
    		  "empire.jpg",1432,1261,40,35,300,154), 
      
      Fathom      ("cc040aa1-f206-11e4-a3e0-24be05e24f7e", 20,17,-20,-17,20,17,
    		  "fathom.jpg",1552,1250,38,37,212,185),
      
      Gambol      ("c7edbf0f-f206-11e4-aa52-24be05e24f7e", 31,15,-31,-12,31,12,
    		  "gambol.jpg",1000,1000,25,25,0,0), 
      
      Orion       ("c74c9d0f-f206-11e4-8330-24be05e24f7e", 10,10,0,0,5,5,
    		  "orion.jpg",1000,1000,25,25,0,0), 
      
      Pegasus     ("ID", 10,10,0,0,5,5,
    		  "pegasus.jpg",1000,1000,25,25,0,0), 
      
      Plaza       ("caacb800-f206-11e4-81ab-24be05e24f7e", 25,15,-13,-18,13,18,
    		  "plaza.jpg",1328,1122,35,35,307,133), 
      
      Regret      ("cdee4e70-f206-11e4-87a2-24be05e24f7e", 10,20,-19,-20,19,20,
    		  "regret.jpg",1380,1170,48,30,320,210), 
      
      The_Rig     ("cb914b9e-f206-11e4-b447-24be05e24f7e", 11,29,-24,-5,24,5,
    		  "the_rig.jpg",1391,1138,38,35,324,172), 
      
      Trench      ("ID", 10,10,0,0,5,5,
    		  "trench.jpg",1000,1000,25,25,0,0),
      
      Trident     ("ID", 10,10,0,0,5,5,
    		  "trident.jpg",1000,1000,25,25,0,0), 
      
      Truth       ("ce1dc2de-f206-11e4-a646-24be05e24f7e", 20,19,-5,-19,5,19,
    		  "truth.jpg",1050,1185,41,38,565,240),
      
      DEFAULT     ("DEFAULT", 10,10,0,0,5,5,
    		  "empire.jpg",1000,1000,5,5,0,0);
      
      private final String ID;
      private final int maxX, maxY, minX, minY;
      private final int xOff, yOff;
      private final String refImgName;
      private final int refImgW, refImgH, refImgScaleX, refImgScaleY;
      private final int refImgOffX, refImgOffY;
      
      Map(String ID, int maxX, int maxY, int minX, int minY,
                     int xOff, int yOff,
          String refImgName, int refImgW, int refImgH,
          int refImgScaleX, int refImgScaleY,
          int refImgOffX, int refImgOffY){
         this.ID = ID;
         this.maxX = maxX;
         this.maxY = maxY;
         this.minX = minX;
         this.minY = minY;
         this.xOff = xOff;
         this.yOff = yOff;
         
         this.refImgName = refImgName;
         this.refImgW = refImgW;
         this.refImgH = refImgH;
         this.refImgScaleX = refImgScaleX;
         this.refImgScaleY = refImgScaleY;
         this.refImgOffX = refImgOffX;
         this.refImgOffY = refImgOffY;
      }
      
      // Get functions 
      String id(){ return ID; }
      int maxX(){ return maxX; }
      int maxY(){ return maxY; }
      int minX(){ return minX; }
      int minY(){ return minY; }
      int xOffset(){ return xOff; }
      int yOffset(){ return yOff; }
      
      String refImgName(){ return refImgName; }
      int refImgW(){ return refImgW; }
      int refImgH(){ return refImgH; }
      int refImgScaleX(){ return refImgScaleX; }
      int refImgScaleY(){ return refImgScaleY; }
      int refImgOffX(){ return refImgOffX; }
      int refImgOffY(){ return refImgOffY; }
   }
   private static Map map;
   
   public enum MapOptions{
	   KILLS, DEATHS;
   }
   private static MapOptions mOpt;
   
   
   // Relative file path to the images
   private static String orig_path = "";//"C:\\Users\\tedris\\workspace\\Halo5Heatmaps\\WebContent\\images\\Maps\\Originals\\";//"..\\..\\WebContent\\images\\Maps\\Originals\\";
   private static String overlay_path = "";// "C:\\Users\\tedris\\workspace\\Halo5Heatmaps\\WebContent\\images\\Maps\\Heatmaps\\";//"..\\..\\WebContent\\images\\Maps\\Heatmaps\\";
   
   // String to store image as a string 
   private static String imgAsStr = "";
   private static byte[] imgAsBytes = null;

//****************************************************************************************************************************
//                      PUBLIC FUNCTIONS
//****************************************************************************************************************************
   /*public static void generateMap(int[][] array){
      generateMap(array, "Trev 5612", "DEFAULT", MapOptions.DEATHS);
   }*/
   
   public static byte[] generateMap(int[][] listOfKills, String gamertag, String mapID, MapOptions m){
      setMap(mapID);
      setMapOptions(m);
   
      gamertag = gamertag.replace(" ", "_");
      
      // Use helper functions to overlay points of kills onto map
      // and then generate an image file
      int[][] array = buildArrayFromList(listOfKills);
      array = fillArrayFromKills(array);
      
      int[][] arr1 = normalizePoints(array);
      int[][] arr2 = normalizePointsWithNeighborAveraging(array);
      
      String finalpath = overlay_path + gamertag + "_"+ map.refImgName();
      //String rawpath = overlay_path + "raw\\" + gamertag + "_" + map.refImgName();
      String rawpath = overlay_path + gamertag + "_" + map.refImgName();
      
      try{
           createTestImageFromGraphics();
           
           // TODO: Send full file path here
           // Maybe change String filename to String gamertag
           String raw = rawpath.substring(0, rawpath.length() - 4) + "-raw.jpg";
           BufferedImage rawImg = buildHeatmap(arr1, raw);
           
           //String name = finalpath.substring(0, finalpath.length() - 4) + "_neighbor_avgs.jpg";
           //print("Filename: " + name + "\n");
           BufferedImage overlayImg = buildHeatmap(arr2, finalpath);
           /*
           BufferedImage baseImg = null;
           if(map.id().equals(Map.Coliseum.id())){
        	   baseImg = buildBfFromBytes(BaseImageHolder.getColiseum());
           }else if(map.id().equals(Map.Eden.id())){
        	   baseImg = buildBfFromBytes(BaseImageHolder.getEden());
           }else if(map.id().equals(Map.Empire.id())){
        	   baseImg = buildBfFromBytes(BaseImageHolder.getEmpire());
           }else if(map.id().equals(Map.Fathom.id())){
        	   baseImg = buildBfFromBytes(BaseImageHolder.getFathom());
           }else if(map.id().equals(Map.Plaza.id())){
        	   baseImg = buildBfFromBytes(BaseImageHolder.getPlaza());
           }else if(map.id().equals(Map.Regret.id())){
        	   baseImg = buildBfFromBytes(BaseImageHolder.getRegret());
           }else if(map.id().equals(Map.The_Rig.id())){
        	   baseImg = buildBfFromBytes(BaseImageHolder.getThe_Rig());
           }else if(map.id().equals(Map.Truth.id())){
        	   baseImg = buildBfFromBytes(BaseImageHolder.getTruth());
           }*/
           
           overlayPoints(orig_path + map.refImgName(),finalpath,null, overlayImg);
        }catch(IOException e){
            e.printStackTrace();
        }
      
      return imgAsBytes;
      
   }   
   

//****************************************************************************************************************************
//                      GET/SET FUNCTIONS
//****************************************************************************************************************************
   public static void setMap(String mapID){
      // Set the Map enum based on the mapID
      int setFlag = -1;
      for(Map m : Map.values()){
         if( mapID.equals(m.id()) ){
            map = m;
            setFlag = 1;
            break;
         }
      }
      // Set map to DEFAULT if no matching map was found
      if(setFlag < 0){  map = Map.DEFAULT; }
      setOffsets(map);
      setLengths(map);
   }

//****************************************************************************************************************************
//                      PRIVATE FUNCTIONS
//****************************************************************************************************************************
   
   private static BufferedImage buildBfFromBytes(byte[] b){
	   BufferedImage bf = null;
	   
	   print("b.length: " + b.length + "\n");
	   
	   
	   InputStream in = new ByteArrayInputStream(b);
	   try{
		   bf = ImageIO.read(in);
		   print("Read bytes into bf\n");
	   }catch(Exception e){print("buildBfFromBytes failed\n");e.printStackTrace();}
	   
	   if(bf == null){
		   print("You lyin ass bitch\n");
	   }
	   return bf;
   }
   
   // Sets the xOffset and yOffset for the array based off the current map
   private static void setOffsets(Map m){
      xOffset = m.xOffset();
      yOffset = m.yOffset();
      //print("******************************************\nyOffset is " + yOffset + " when it should be 15\n");
   }
   
   // Set the length of the array based off the maximum and minimum values for x and y
   //       (0,0) is in the center of each map so MAX-MIN will always be a positive number
   private static void setLengths(Map m){
      lengthX = m.maxX() - m.minX();
      lengthY = m.maxY() - m.minY();
   }
   
   private static void setMapOptions(MapOptions m){
	   mOpt = m;
   }
   
   // TODO: NEED WAY TO DIFFERENTIATE IF HEATMAP IS KILLS OR DEATHS
   private static int[][] buildArrayFromList(int[][] array){
      int[][] ret = new int[lengthX][lengthY];
      
      for(int i = 0; i < lengthX; i++){
         for(int j = 0; j < lengthY; j++){
            ret[i][j] = 0;
         }
      }
      
      int killX, killY, killZ;
      int deathX, deathY, deathZ;
      for(int i = 0; i < array.length; i++){
         if(mOpt == MapOptions.KILLS){
        	 //print("mOpt == KILLS\n");
	         killX = array[i][0];
	         killY = array[i][1];
	         killZ = array[i][2];
	         
	        if(killX < lengthX && killY < lengthY){
	           ret = setIndexWithOffset(ret, killX, killY, getIndexWithOffset(ret, killX, killY) + 1);
	        }
         }else if(mOpt == MapOptions.DEATHS){
        	 //print("mOpt == DEATHS\n");
	         deathX = array[i][3];
	         deathY = array[i][4];
	         deathZ = array[i][5];
	         
	         if(deathX < lengthX && deathY < lengthY){
	            // Set index with offset here
	            ret = setIndexWithOffset(ret, deathX, deathY, getIndexWithOffset(ret, deathX, deathY) + 1);
	         }
         }
      }
      
      return ret;
   }
   
   // array Param first filled with flag at each
   private static int[][] fillArrayFromKills(int[][] array){
      int[][] ret = new int[array.length][array[0].length];
      
      for(int i = 0; i < ret.length; i++){
         for(int j = 0; j < ret[0].length; j++){
            ret[i][j] = array[i][j];
            
            for(int k = 0; k < array[i][j]; k++){
               // Increase neighboring cell values for ret
               // Make sure to update for the number of kills 
               // at the specified index 
               ret = updateNeighbors(ret, i, j);
            }
         }
      }
      
      return ret;
   }
   
   private static int[][] updateNeighbors(int[][]arr, int x, int y){
      // Update 8 direct neighbors
      // Top Left
      arr = setIndex(arr, x - 1, y - 1, getIndex(arr, x - 1, y - 1) + 2);
      // Top Mid
      arr = setIndex(arr, x, y - 1, getIndex(arr, x, y - 1) + 2);
      // Top Right
      arr = setIndex(arr, x + 1, y - 1, getIndex(arr, x + 1, y - 1) + 2);
      
      // Mid Left
      arr = setIndex(arr, x - 1, y , getIndex(arr, x - 1, y) + 2);
      // Mid Right
      arr = setIndex(arr, x + 1, y , getIndex(arr, x + 1, y) + 2);
      
      // Bot Left
      arr = setIndex(arr, x - 1, y + 1, getIndex(arr, x - 1, y + 1) + 2);
      // Bot Mid
      arr = setIndex(arr, x , y + 1, getIndex(arr, x, y + 1) + 2);
      // Bot Right
      arr = setIndex(arr, x + 1, y + 1, getIndex(arr, x + 1, y + 1) + 2);
      
      
      // Update 16 outer neighbors
      // Top Left
      arr = setIndex(arr, x - 2, y - 2, getIndex(arr, x - 2, y - 2) + 1);
      // Top Mid Left
      arr = setIndex(arr, x - 1, y - 2, getIndex(arr, x - 1, y - 2) + 1);
      // Top Mid Mid
      arr = setIndex(arr, x, y - 2, getIndex(arr, x, y - 2) + 1);
      // Top Mid Right
      arr = setIndex(arr, x + 1, y - 2, getIndex(arr, x + 1, y - 2) + 1);
      // Top Right
      arr = setIndex(arr, x + 2, y - 2, getIndex(arr, x + 2, y - 2) + 1);
      
      // Mid Left Top
      arr = setIndex(arr, x - 2, y - 1, getIndex(arr, x - 2, y - 1) + 1);
      // Mid Left Mid
      arr = setIndex(arr, x - 2, y, getIndex(arr, x - 2, y) + 1);
      // Mid Left Bot
      arr = setIndex(arr, x - 2, y + 1, getIndex(arr, x - 2, y + 1) + 1);
      // Mid Right Top
      arr = setIndex(arr, x + 2, y - 1, getIndex(arr, x + 2, y - 1) + 1);
      // Mid Right Mid
      arr = setIndex(arr, x + 2, y, getIndex(arr, x + 2, y) + 1);
      // Mid Right Bot
      arr = setIndex(arr, x + 2, y + 1, getIndex(arr, x + 2, y + 1) + 1);
      
      // Bot Left
      arr = setIndex(arr, x - 2, y + 2, getIndex(arr, x - 2, y + 2) + 1);
      // Bot Mid Left
      arr = setIndex(arr, x - 1, y + 2, getIndex(arr, x - 1, y + 2) + 1);
      // Bot Mid Mid
      arr = setIndex(arr, x, y + 2, getIndex(arr, x, y + 2) + 1);
      // Bot Mid Right
      arr = setIndex(arr, x + 1, y + 2, getIndex(arr, x + 1, y + 2) + 1);
      // Bot Right
      arr = setIndex(arr, x + 2, y + 2, getIndex(arr, x + 2, y + 2) + 1);
      
      return arr;
   }
   
   private static int[][] setIndexWithOffset(int[][] arr, int x, int y, int val){
      try{
    	  int tmpX = xOffset + x;
    	  int tmpY = yOffset + y;
         arr[tmpX][tmpY] = val;
      }catch(IndexOutOfBoundsException e){
         //e.printStackTrace();
         print("Index (" + x + ", " + y + ") is out of bounds.\nxOffset is " + xOffset + " and yOffset is " + yOffset + "\n");
      }
      return arr;
   }
   
   private static int getIndexWithOffset(int[][] arr, int x, int y){
      try{
         return arr[xOffset + x][yOffset + y];
      }catch(IndexOutOfBoundsException e){
         //e.printStackTrace();
          print("Index (" + x + ", " + y + ") is out of bounds.\nxOffset is " + xOffset + " and yOffset is " + yOffset + "\n");
      }
      return -1;
   }
   
   private static int[][] setIndex(int[][] arr, int x, int y, int val){
      try{
         arr[x][y] = val;
      }catch(IndexOutOfBoundsException e){
      
      }
      return arr;
   }
   
   private static int getIndex(int[][] arr, int x, int y){
      try{
         return arr[x][y];
      }catch(IndexOutOfBoundsException e){
      
      }
      return -1;
   }
   
   // Normalize points to a 255 scale to be used for RGB color intensity
   private static int[][] normalizePoints(int[][] array){
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
            	if(max != 0){
            		array[i][j] = (int)(255 * array[i][j] / max);
            	}else{
            		array[i][j] = (int)(255 * array[i][j] / 1);
            	}
            }
        }
        
        return array;
   }
   
   // Normalize values of all points to the relative averages of their closest 8 neighbors
   // Only do a single pass to start off
   private static int[][] normalizePointsWithNeighborAveraging(int[][] array){
      // TODO: Finish this function lol
      int[][] ret = new int[array.length][array[0].length];
      
      int temp = 0;
      int tempAvg = 0;
      int neighbors = 0;
      for(int i = 0; i < array.length; i++){
         for(int j = 0; j < array[0].length; j++){
            tempAvg = 0;
            neighbors = 0;
            
            // Average in the cell itself as well
            temp = arrayIndex(array, i, j, i, j);
            if(temp != -1){tempAvg+=temp; neighbors++;}
            
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
   
   private static BufferedImage buildHeatmap(int[][] arr, String name){
	   
	   /*
	   BufferedImage image = null;
	   
	   try{
		   image = ImageIO.read(new File(orig_path, map.refImgName()));
	   }catch(IOException e){ e.printStackTrace(); }*/
	   
	   // OFFSETS: x = 260 y = 182
	   // DIMENSIONS: x = 1716 y = 1065 
	   int width = map.refImgW;
	   int height = map.refImgH;
	   // Create BufferedImage of 1:1 scale from map image
	   //BufferedImage bf = new BufferedImage(arr.length * 5, arr[0].length * 5, BufferedImage.TYPE_INT_RGB);
	   BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      
	   
	   int scaleX = map.refImgScaleX;//(int)(width / arr.length);
	   int scaleY = map.refImgScaleY;//(int)(height / arr[0].length);
	     
	   
      // Create a graphics object used to draw into the buffered image
      Graphics2D g2d = bf.createGraphics();
      
      // Fill image at rect(i, j) with appropriate color
      for(int i = 0; i < arr.length; i++){
         for(int j = 0; j < arr[i].length; j++){
	        g2d.setColor(getColorFromDensity(arr[i][j]));
        
            //g2d.fillRect(i * 5, j * 5, 5, 5);
            g2d.fillRect(i * scaleX, j * scaleY, scaleX, scaleY);
         }
      }
      
      // Disposes graphics context
      // Releases system resources
      g2d.dispose();
      
      //print("NAME IN BUILDHEATMAP IS " + name + "\n");
      
      // Save as jpg
      File file = new File(name);
      try{
         ImageIO.write(bf, "jpg", file);
         print("WROTE bf TO " + name + "\n");
      }catch(IOException e){
         e.printStackTrace();
      }
      
      return bf;
   }
   
   /*
   private static void buildHeatmap(int[][] map) throws IOException{
      buildHeatmap(map, "map.png");
   }*/
   
   private static void overlayPoints(String baseName, String overlayName, BufferedImage image, BufferedImage overlay){
	      // Paint graphics to image file here
	      
	      //http://stackoverflow.com/questions/2318020/merging-two-images
	      
	      //String path = "C:\\Users\\TheMaster\\workspace\\Halo5Heatmaps-LocalTester\\"; // base path of the images

	      // load source images
	      //BufferedImage image = null;
	      //BufferedImage overlay = null;
	      try{
	    	  //String filepath = orig_path + baseName;
	    	  //print("Attempting to read filepath: " + filepath + "\n");
	    	  
	    	  //File temp = new File(baseName);
	    	  //image = ImageIO.read(temp);
	    	  //print("Read image\n");
	    	  
	    	  //filepath = overlay_path + overlayName;
	    	  //print("Attempting to read filepath: " + filepath + "\n");
	    	  
	    	  //temp = new File(overlayName);
	    	  //print("OverlayName: " + overlayName + "\n");
	    	  //overlay = ImageIO.read(temp);
	    	  //print("Read overlay\n");
	      }catch(Exception e){ e.printStackTrace(); }
	    	  
	      int w = image.getWidth();//Math.max(image.getWidth(), overlay.getWidth());
	      int h = image.getHeight();//Math.max(image.getHeight(), overlay.getHeight());
	      
	      int paddingX = map.refImgOffX;
	      int paddingY = map.refImgOffY;
	      
	      // Create a scaled up version of the overlay image
	      /*Image tmp = overlay.getScaledInstance(w, h, Image.SCALE_DEFAULT);
	      overlay = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	      
	      Graphics2D g2d = overlay.createGraphics();
	      g2d.drawImage(tmp, 0, 0, null);
	      g2d.dispose();*/
	      
	      Image tmp = makeColorTransparent(overlay, Color.white);
	      overlay = imageToBufferedImage(tmp);
	      
	      // Combine the two images
	      BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	      
	      // paint both images, preserving the alpha channels
	      Graphics2D g = combined.createGraphics();
	      
	      final AlphaComposite OVER_HALF = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);
	      g.setComposite(AlphaComposite.Src);
	      g.drawImage(image, 0, 0, null);
	      
	      g.setComposite(OVER_HALF);
	      g.drawImage(overlay, paddingX, paddingY, null);
	      
	      // Save as new image
	      ByteArrayOutputStream baos = null;
	      byte[] bytes = null;
	      try{
	    	  baos = new ByteArrayOutputStream();
	    	  ImageIO.write(combined, "jpg", new File(overlayName));
	    	  ImageIO.write(combined, "jpg", baos);
	    	  baos.flush();
	    	  bytes = baos.toByteArray();
	      }catch(IOException e){ e.printStackTrace(); }
	      finally{
	    	  try{
	    		  baos.close();
	    	  }catch(Exception e){ e.printStackTrace(); }
	      }
	      
	      
	      if(bytes.length > 0){
	    	  print("Success?\n");
	    	  // Get the blob
	    	  //byte[] blobAsBytes = blob.getBytes(1,blobLength);
	    	  //String bytesAsStr = Base64.getEncoder().encodeToString(blobAsBytes);
	    	  imgAsBytes = bytes;
	      }else{
	    	  print("Fail\n");
	      }
	      
	      
	      /* HOW TO SEND byte[] TO SQL DATABASE
	       * 
	       * PreparedStatement stmt = connection.prepareStatement(sqlStmt);
	       * 
	       * stmt.setBinaryStream([BLOB COL INDEX], new ByteArrayInputStream(bytes), bytes.length)
	       * rowsAffected = stmt.executeUpdate();	// Should only be 1
	       * 
	       */
	      
	      
	      /* HOW TO GET byte[] FROM SQL DATABASE
	       * 
	       * stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	       * ResultSet result = stmt.executeQuery(sqlStmt);
	       * if(result.next()){
	       * 	Blob blob = result.getBlob("blobcolumnname");
	       * }
	       * byte[] blobBytes = blob.getBytes(1L, (int)blob.length());
	       * 
	       */
	      
	      /* CONVERT byte[] TO STRING
	       * 
	       * String strBytes = new String(bytes);
	       * 
	       */
	      //print("BYTES AS A STRING\n\n" + (new String(bytes)) );
	      
	      
	      
	      /*
	      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	      StringBuffer strOut;
	      String finalImgStr = "";
	      try{
	    	  BufferedReader br = new BufferedReader(new InputStreamReader(bais));
	    	  String aux;
	    	  strOut = new StringBuffer();
	    	  while((aux=br.readLine()) != null){
	    		  strOut.append(aux);
	    	  }
	    	  finalImgStr = strOut.toString();
	    	  
	      }catch(Exception e){ e.printStackTrace(); }
	      
	      print("\n\n" + finalImgStr + "\n");
	      */
   }
   
   private static BufferedImage imageToBufferedImage(Image image) {

   	BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
   	Graphics2D g2 = bufferedImage.createGraphics();
   	g2.drawImage(image, 0, 0, null);
   	g2.dispose();

   	return bufferedImage;

   }

   public static Image makeColorTransparent(BufferedImage im, final Color color) {
   	ImageFilter filter = new RGBImageFilter() {

   		// the color we are looking for... Alpha bits are set to opaque
   		public int markerRGB = color.getRGB() | 0xFF000000;

   		public final int filterRGB(int x, int y, int rgb) {
   			if ((rgb | 0xFF000000) == markerRGB) {
   				// Mark the alpha bits as zero - transparent
   				return 0x00FFFFFF & rgb;
   			} else {
   				// nothing to do
   				return rgb;
   			}
   		}
   	};

   	ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
   	return Toolkit.getDefaultToolkit().createImage(ip);
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
         //c = new Color(255,255,255,255);
    	  c = Color.white;
      }else if(x > 31 && x <= 63){
         c = Color.cyan;
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
      int alpha = 255 - (255 - x) % 32 * 2;
      /*if(c == new Color(255,255,255,255)){
    	 alpha = 0;
      }*/
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