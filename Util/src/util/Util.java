/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;

/**
 *
 * @author powers4
 */
public class Util {
    public Util() {
    }
    public static float slider2Val(int i) {
        return i / 50.0f - 1.0f;
    }
    public static int val2Slider(float f) {
        return (int) ((f + 1) * 50); 
    }
    
    public static float pot2Val(int i) {
    	return (i / 64.0f) - 1.0f;
    }
    public static int getClosestScaleNote(int inNote, int[] scale){
        int minIndex = 0;
        int minDist = 13;
        int noteClass = inNote % 12;

        for (int i = 0; i < scale.length; i++) {
            int tDist = Math.min(Math.abs(noteClass - scale[i]), 12 - Math.abs(noteClass - scale[i]));
            if (tDist < minDist ) {
                minDist = tDist;
                minIndex = i;
            }
        }
        if ((scale[minIndex] - noteClass) > 0) {                // moving to a higher class, but :
            if (Math.abs(scale[minIndex] - noteClass) < 7) {    // is up the shortest way ?
                return (inNote + minDist);
            }
            else {
                return (inNote - minDist);
            }
        }
        else {                                                  // moving to a lower class, but :
            if (Math.abs(scale[minIndex] - noteClass) < 7) {    // is down the shortest way ?
                return (inNote - minDist);
            }
            else {
                return (inNote + minDist);
            }
        }
    }
    
    public static boolean findInArray(int[] a, int i) {
    	for (int t = 0; t < a.length; t++) {
    		if (a[t] == i) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public static int findInArray(String[] a, String s) {
    	for (int t = 0; t < a.length; t++) {
    		if (a[t] == s) {
    			return t;
    		}
    	}
    	return -1;
    }
    
	public static String getExtension(File f) {
		String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
 
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
	}
}


 