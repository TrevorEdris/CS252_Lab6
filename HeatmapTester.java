import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class HeatmapTester{

   public static void main(String[] args){
        int length = 50;
        int[][] arr = new int[length][length];
        Random rand = new Random();
        
        // Fill array with values from 0 to 50
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
               arr[i][j] = rand.nextInt(length + 1);
            }
        }
        
        // Generate heatmap corresponding to the array
        HeatmapGenerator.generateMap(arr);
        
   }

   private static void print(String s){
      System.out.print(s);
   }
}