/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


package soundstrand;

import util.MusicUtil;


/**
 *
 * @author Person
 */
public class Chord implements Cloneable {
    public Chord(int inRoot, ChordColor inColor, int inInversion) {
        root = inRoot;
        color = inColor;
        inversion = inInversion;
    }
    public int[] getNotes(){
        int notes[] = new int[3];
        notes[0] = (root);
        switch (color) {
            case MAJOR:
                notes[1] = (root + 4) % 12;
                break;
            case MINOR:
                notes[1] = (root + 3) % 12;
        }
        notes[2] = (root + 7) % 12;
        return notes;

    }
    
    public String toString() {
		String chordName = MusicUtil.NOTE_NAMES[root].trim();
		if (color == ChordColor.MINOR) {
			chordName = chordName + "m";
		}
		return chordName;
    	
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public ChordColor getColor() {
		return color;
	}
    
    public int root;
    private ChordColor color;
	public int inversion;
}
