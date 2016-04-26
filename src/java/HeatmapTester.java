import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class HeatmapTester{

   static int lengthX = 60;
   static int lengthY = 30;
   static int xOffset = 30;
   static int yOffset = 15;

   public static void test(String[] args){
        
        int[][] arr = new int[20][6];
        
        arr = addKillDeath(arr, 0, 29, -2, 0, 29, -2, 0);
        arr = addKillDeath(arr, 1, 28, 6, 0, 28, 6, 0);        
        arr = addKillDeath(arr, 2, 21, 7, 0, 21, 7, 0);        
        arr = addKillDeath(arr, 3, 21, 10, 0, 21, 10, 0);        
        arr = addKillDeath(arr, 4, 19, 12, 0, 19, 12, 0);
        
        arr = addKillDeath(arr, 5, -19, 12, 0, -19, 12, 0);          
        arr = addKillDeath(arr, 6, -21, 10, 0, -21, 10, 0);        
        arr = addKillDeath(arr, 7, -21, 7, 0, -21, 7, 0);        
        arr = addKillDeath(arr, 8, -28, 6, 0, -28, 6, 0);
        arr = addKillDeath(arr, 9, -29, -2, 0, 29, -2, 0);
        
        arr = addKillDeath(arr, 10, -21, -2, 0, -21, -2, 0);
        arr = addKillDeath(arr, 11, -21, -5, 0, -21, -5, 0);                
        arr = addKillDeath(arr, 12, -19, -7, 0, -19, -7, 0);        
        arr = addKillDeath(arr, 13, 19, -7, 0, 19, -7, 0);        
        arr = addKillDeath(arr, 14, 21, 5, 0, 21, 5, 0);
        
        arr = addKillDeath(arr, 15, 21, -2, 0, 21, -2, 0);
        arr = addKillDeath(arr, 16, 1, 0, 0, 1, 0, 0);
        arr = addKillDeath(arr, 17, 2, 0, 0, 2, 0, 0);
        arr = addKillDeath(arr, 18, 3, 0, 0, 3, 0, 0);
        arr = addKillDeath(arr, 19, 4, 0, 0, 4, 0, 0);
                
        // Hard code in the kills for now
        /*
        arr = setIndexWithOffset(arr,29,-2,temp);
        arr = setIndexWithOffset(arr,28,6,temp);
        arr = setIndexWithOffset(arr,21,7,temp);
        arr = setIndexWithOffset(arr,21,10,temp);
        arr = setIndexWithOffset(arr,19,12,temp);
        
        arr = setIndexWithOffset(arr,-19,12,temp);
        arr = setIndexWithOffset(arr,-21,10,temp);
        arr = setIndexWithOffset(arr,-21,7,temp);
        arr = setIndexWithOffset(arr,-28,6,temp);
        arr = setIndexWithOffset(arr,-29,-2,temp);
        
        arr = setIndexWithOffset(arr,-21,-2,temp);
        arr = setIndexWithOffset(arr,-21,-5,temp);
        arr = setIndexWithOffset(arr,-19,-7,temp);
        arr = setIndexWithOffset(arr,19,-7,temp);
        arr = setIndexWithOffset(arr,21,-5,temp);
        
        arr = setIndexWithOffset(arr,21,-2,temp);
        arr = setIndexWithOffset(arr,0,0,temp);
        */
        /*int[][] placeholder = new int[arr.length][arr[0].length];
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
               placeholder[i][j] = arr[i][j];
            }
        }
        
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
               if(placeholder[i][j] >= 3){
                  print("Updating neighbors for (" + i + ", " + j + ")\n");
                  updateNeighbors(arr, i, j);
               }
            }
        }*/
        
        // Generate heatmap corresponding to the array
        byte[] imgAsBytes = HeatmapGenerator.generateMap(arr, "Trev 5612", HeatmapGenerator.Map.Crossfire.id(),
        		HeatmapGenerator.MapOptions.DEATHS);
        
   }
   
   public static int[][] addKillDeath(int[][] arr, int i, int a, int b, int c, int x, int y, int z){
      // Kill x,y,z
         arr[i][0] = a;
         arr[i][1] = b;
         arr[i][2] = c;
         
         // Death x,y,z
         arr[i][3] = x;
         arr[i][4] = y;
         arr[i][5] = z;
         
         return arr;
   }

   private static void print(String s){
      System.out.print(s);
   }
}