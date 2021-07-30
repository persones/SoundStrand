package soundstrand;

import java.util.ArrayList;

import util.Util;

public class ChordNotesMelodyUpdater implements MelodyUpdater {
	
	@Override
	public void updateMedloy(Strand strand) {
		int lastNote = 60 + 12 * GlobalParams.melodyOctavesTranspose;
		ArrayList<Motif> motifList = strand.getMotifList();
		
		for (int motifIndex = 0; motifIndex < motifList.size(); motifIndex++) {
			Motif aMotif = motifList.get(motifIndex);    					
			int[] chordNotes = aMotif.getChord().getNotes();
			aMotif.getModifiedNote(0).setPitch(Util.getClosestScaleNote(aMotif.getNote(0).getPitch() - 60 + lastNote, chordNotes)); // first note on the chord 
			for (int noteIndex = 1; noteIndex < aMotif.getNumNotes(); noteIndex++) {
				Note srcNote = aMotif.getNote(noteIndex);
				Note dstNote = aMotif.getModifiedNote(noteIndex);

				dstNote.setTime(aMotif.getModifiedNoteTime(noteIndex));
				dstNote.setPitch(srcNote.getPitch()  - 60 + lastNote + (int)(srcNote.getTime() * 4 * aMotif.getBend())); // rough pitch 
				dstNote.setPitch(Util.getClosestScaleNote(dstNote.getPitch(), chordNotes)); // following notes on the scale
			}
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("Chord Notes");
	}
	
	
}
