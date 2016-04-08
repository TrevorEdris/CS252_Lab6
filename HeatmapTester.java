import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class HeatmapTester{

   public static void main(String[] args){
        int[][] arr = new int[50][50];
        Random rand = new Random();
        
        // Fill array with values from 0 to 50
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
               arr[i][j] = rand.nextInt(51);
            }
        }
        
        // Normalize points to a 255 scale
        arr = HeatmapGenerator.normalizePoints(arr);
        
        /*for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
               print("arr[" + i + "][" + j + "]: " + arr[i][j] + "\n");
            }
        }*/
   }

   private static void print(String s){
      System.out.print(s);
   }
}