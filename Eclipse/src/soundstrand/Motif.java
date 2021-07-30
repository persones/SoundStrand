/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soundstrand;


import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import util.Util;



/**
 *
 * @author powers4
 */
public class Motif implements Cloneable{
    public Motif() {
        init();
    }
    
    public Motif(int blockType, MotifsDb inMotifDb) {
    	init();
        inMotifDb.populateMotif(this, blockType);
        motifType = blockType;
    } 
    
    public void addNote(Note n) {
    	int noteIndex = 0;
    	while ((noteIndex < noteList.size()) && (noteList.get(noteIndex).getTime() < n.getTime())) { //TODO : wtf
    		noteIndex++;
    	}
    	noteList.add(noteIndex, n);
    	try {
			modifiedNoteList.add(noteIndex, (Note)n.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void addContext(Chord c) {
    	harmonicContext.add(c);
    }
    
    public void addPattern(ArrayList<Float> inPattern) {
    	patterns.add(inPattern);
    }
    
    public void setType(int inType) {
    	motifType = inType;
    }
    
    public void setMotifProperties(int potValues[]) {
    	elongation = Util.pot2Val(potValues[0]);
        rotation = Util.pot2Val(potValues[1]);
        bend = Util.pot2Val(potValues[2]);
        
        currentPattern = Math.round((elongation + 1) / 2 * (patterns.size() - 1));
       // System.out.println(Integer.toString(potValues[0]) + " " + Integer.toString(potValues[1]) + " " + Integer.toString(potValues[2]));
        
        fireMotifChange(changeEvent);
    }
    
    public void updateView() {
    	fireMotifChange(changeEvent);
    }
    
    public float getElongation() {
        return elongation;
    }
    
    public float getRotation() {
        return rotation;
    }
    
    public float getBend() {
        return bend;
    }
    
    public void setElongation(float e) {
        elongation = e;
        currentPattern = Math.round((elongation + 1) / 2 * (patterns.size() - 1));
        if (null != strand) {
        	strand.updateHarmony();
        }
        fireMotifChange(changeEvent);
    }
    
    public void setRotation(float r) {
        rotation = r;
        if (null!=strand) {
        	strand.updateHarmony();
        	return;
        }
        
		fireMotifChange(changeEvent);
    }
    
    public void setBend(float b) {
        bend = b;
        if (null != strand) {
        	strand.updateHarmony();
        }
        fireMotifChange(changeEvent);
    }
    
    public void addMotifChangeListener(ChangeListener l) {
        eventListeners.add(ChangeListener.class, l);
    }
    
    public void removeMotifChangeListener(ChangeListener l) {
        eventListeners.remove(ChangeListener.class, l);
    }
    
    public int getMotifType() {
    	return motifType;
    }
    
    public int getNumNotes() {
        return noteList.size();
    }
    
    public Note copyNote(int i) {
        try {
            return (Note)(noteList.get(i).clone());
        }
        catch (CloneNotSupportedException e) {
            assert(true);
        }
        return null;
    }
    
    public Note getNote(int i) {
    	return noteList.get(i);
    }
    
    public Note getModifiedNote(int i) {
    	return modifiedNoteList.get(i);
    }
    
    public int getNoteInTimeIndex(float t) {
    	for (int i = 0; i < noteList.size(); i++) {
    		if (noteList.get(i).getTime() == t) {
    			return i;
    		}
    	}
    	return -1; 		
    }
    
    private void fireMotifChange(ChangeEvent e) {
        Object[] listeners = eventListeners.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener)(listeners[i + 1])).stateChanged(e);
            }
        }
    }
   
    public  ArrayList<Chord> getHarmonicContextList() {
        return harmonicContext;
    }
    
    public Object clone() throws CloneNotSupportedException {
    	return super.clone();
    }
    
    public Motif cloneMotif() {
    	Motif aMotif = new Motif(); // TODO : copy original harmonic context
    	for (int i = 0; i < noteList.size(); i++) {
    		try {
				aMotif.addNote((Note)(noteList.get(i).clone()));
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	for (int i = 0; i < harmonicContext.size(); i++) {
    		aMotif.addContext(harmonicContext.get(i)); // TODO : clone ?
    	}
    	for (int i = 0; i < patterns.size(); i++) {
    		aMotif.addPattern(patterns.get(i));
    	}
    	
		aMotif.setElongation(elongation);
    	aMotif.setRotation(rotation);
		aMotif.setBend(bend);
		
		aMotif.setType(motifType);
		aMotif.setStrand(strand);
		aMotif.setFunction(function);
		aMotif.setChord(chord);
		return aMotif;
    }
   
    public void copyToMotif(Motif inMotif) {
    	
    	for (int i = 0; i < noteList.size(); i++) {
    		try {
				inMotif.addNote((Note)(noteList.get(i).clone()));
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	for (int i = 0; i < harmonicContext.size(); i++) {
    		inMotif.addContext(harmonicContext.get(i));
    	}
    	for (int i = 0; i < patterns.size(); i++) {
    		inMotif.addPattern(patterns.get(i));
    	}
		inMotif.setElongation(elongation);
    	inMotif.setRotation(rotation);
		inMotif.setBend(bend);
		inMotif.setType(motifType);
		inMotif.setStrand(strand);
    }
    
    private void init() {
    	elongation = 0;
        rotation = 0;
        bend = 0;
        
        noteList = new ArrayList<Note>(); 
        modifiedNoteList = new ArrayList<Note>();
        harmonicContext = new ArrayList<Chord>();
        patterns = new ArrayList<ArrayList<Float>>();
        eventListeners = new EventListenerList();
        chord = new Chord(0, ChordColor.MAJOR, 0);
    }
    
    public void setStrand(Strand inStrand) {
    	strand = inStrand;
    }
    
    public String getFunction() {
    	return function;
    }
    
    public void setFunction(String inFucntion) {
    	function = inFucntion;
    }
    
    public void setChord(Chord inChord) {
    	chord = inChord;
    	fireMotifChange(changeEvent);
    }
    
    public Chord getChord() {
    	return chord;
    }
    
    public float getModifiedNoteTime(int i) {
    	return (patterns.get(currentPattern)).get(i);
    }
    
    //public int[] getScaleNotes() {
    //	return scale.intervals;
    //}
        
    public ArrayList<Float> getCurrentPattern() {
    	return patterns.get(currentPattern);
    }
    
    
    private ArrayList<Note> noteList;
    private ArrayList<Note> modifiedNoteList;
    private ArrayList<Chord> harmonicContext;
    private ArrayList<ArrayList<Float>> patterns;
    private int motifType;
    private float elongation;
    private float rotation;
    private float bend;
    private int currentPattern;
    
    private Chord chord;
    private String function;
   // private MusicUtil.Scale scale = MusicUtil.Scale.DIATONIC_MAJOR; // todo : i don't like this

    private Strand strand = null;
    
    private EventListenerList eventListeners;
    private ChangeEvent changeEvent = new ChangeEvent(this);

}
