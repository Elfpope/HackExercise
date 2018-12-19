package net.junfeng.hackexercise;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class DiagonalDiffExercise {

	public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        
        int a[][] = new int[n][n];
        for(int a_i=0; a_i < n; a_i++){
            for(int a_j=0; a_j < n; a_j++){
                a[a_i][a_j] = in.nextInt();
            }
        }
        
        int primaryDiagonalSum = 0;
        for(int i = 0; i < n; i++)
        {
        	primaryDiagonalSum += a[i][i];
        }
        
        int secondDiagonalSum = 0;
        int a_i=0;
        int a_j=n-1;
        do{
        	secondDiagonalSum += a[a_i][a_j];
        	 a_i++;
        	 a_j--;
        }while(a_i < n && a_j >=0);
        
        
        System.out.println(Math.abs(primaryDiagonalSum - secondDiagonalSum));
    }

}
