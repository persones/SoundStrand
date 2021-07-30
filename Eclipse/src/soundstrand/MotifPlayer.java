/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soundstrand;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;




/**
 *
 * @author powers4
 */
public class MotifPlayer {
    public MotifPlayer() {
        try {
            synth = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(MotifPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            synth.open();
        } catch (MidiUnavailableException ex) {
            Logger.getLogger(MotifPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        MidiChannel[]	channels = synth.getChannels();
	
        melodyChannel = channels[MELODY_CHANNEL];
        melodyChannel.programChange(GS_ACOUSTIC_GRAND_PIANO);
        
        bassChannel = channels[BASS_CHANNEL];
        bassChannel.programChange(GS_ELECTRIC_BASS);
        
        harmonyChannel = channels[HARMONY_CHANNEL];
        harmonyChannel.programChange(GS_STRING_ENSAMBLE_1);
        
        
    }
    public void playMotif(Motif m) {
        float currentTime = 0;
        int numNotes = m.getNumNotes();
        Note aNote = null;
        int qurterNoteMs = 60000 / GlobalParams.gTempo;
                
        //Chord aChord = m.getHarmonicContext();
        Chord aChord = m.getChord();
        int[] chordNotes = aChord.getNotes();
        
        bassChannel.noteOn(aChord.root + 36, 127);
        for (int i = 0; i < chordNotes.length; i++) {
            harmonyChannel.noteOn(chordNotes[i] + 48, 90);
        }
        
        for (int i = 0; i  < numNotes; i++) {
        	aNote = m.getModifiedNote(i);

        	try {
        		Thread.sleep((int)(qurterNoteMs * (aNote.getTime() - currentTime)));
        	} catch (InterruptedException ex) {
        		Logger.getLogger(MotifPlayer.class.getName()).log(Level.SEVERE, null, ex);
        	}
        	melodyChannel.noteOn(aNote.getPitch(), 127);
        	try {
        		Thread.sleep((int) (500 * aNote.getDuration()));
        	} catch (InterruptedException ex) {
        		Logger.getLogger(MotifPlayer.class.getName()).log(Level.SEVERE, null, ex);
        	}

        	melodyChannel.noteOff(aNote.getPitch());
        	currentTime = aNote.getTime() + aNote.getDuration();
        }
        try {
    		Thread.sleep((int)(qurterNoteMs * (4.0f - currentTime)));
    	} catch (InterruptedException ex) {
    		Logger.getLogger(MotifPlayer.class.getName()).log(Level.SEVERE, null, ex);
    	}
        bassChannel.noteOff(aChord.root + 36);
        for (int i = 0; i < chordNotes.length; i++) {
        	harmonyChannel.noteOff(chordNotes[i] + 48);	
        }
    }
    private Synthesizer synth;
    private MidiChannel melodyChannel;
    private MidiChannel bassChannel;
    private MidiChannel harmonyChannel;
    
    private static final int MELODY_CHANNEL = 0;
    private static final int BASS_CHANNEL = 1;
    private static final int HARMONY_CHANNEL = 2;
    
    private static final int GS_ACOUSTIC_GRAND_PIANO = 0;
    private static final int GS_ELECTRIC_BASS = 33;
    private static final int GS_STRING_ENSAMBLE_1 = 48;
}
