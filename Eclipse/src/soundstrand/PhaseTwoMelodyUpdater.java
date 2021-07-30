package soundstrand;

import java.util.ArrayList;

import util.Util;

public class PhaseTwoMelodyUpdater implements MelodyUpdater {

	@Override
	public void updateMedloy(Strand strand) {
		int lastNote = 60 + 12 * GlobalParams.melodyOctavesTranspose;
		ArrayList<Motif> motifList = strand.getMotifList();
		
		for (int motifIndex = 0; motifIndex < motifList.size(); motifIndex++) {
			Motif aMotif = motifList.get(motifIndex);    					
			int[] scaleNotes;
			if (aMotif.getChord().getColor() == ChordColor.MAJOR) {
				scaleNotes = MusicUtil.Scale.DIATONIC_MAJOR.intervals.clone();
			}
			else {
				scaleNotes = MusicUtil.Scale.DIATONIC_NATURAL_MINOR.intervals.clone();
			}
			for (int noteIndex = 0; noteIndex < scaleNotes.length; noteIndex++) {
				scaleNotes[noteIndex] += aMotif.getChord().root;
				scaleNotes[noteIndex] %= 12; 
			}
			int[] chordNotes = aMotif.getChord().getNotes();
			Note srcNote;
			Note dstNote;
			
			switch (aMotif.getFunction()) {
			default:
				aMotif.getModifiedNote(0).setPitch(Util.getClosestScaleNote(aMotif.getNote(0).getPitch() - 60 + lastNote, chordNotes)); // first note on chord
				lastNote = aMotif.getModifiedNote(0).getPitch();
				for (int noteIndex = 1; noteIndex < aMotif.getNumNotes(); noteIndex++) {
					int interval = aMotif.getNote(noteIndex).getPitch() - aMotif.getNote(noteIndex - 1).getPitch();
					srcNote = aMotif.getNote(noteIndex);
					dstNote = aMotif.getModifiedNote(noteIndex);

					dstNote.setTime(aMotif.getModifiedNoteTime(noteIndex));
					dstNote.setPitch(lastNote + interval + (int)(srcNote.getTime() * 2 * aMotif.getBend())); // rough pitch 
					dstNote.setPitch(Util.getClosestScaleNote(dstNote.getPitch(), scaleNotes)); // following notes on scale
					lastNote = dstNote.getPitch();
				}
				dstNote = aMotif.getModifiedNote(aMotif.getNumNotes() - 1);
				dstNote.setPitch(Util.getClosestScaleNote(dstNote.getPitch(), chordNotes)); // last note on chord
				lastNote = dstNote.getPitch();
				break;
	/*		default:
				aMotif.getModifiedNote(0).setPitch(Util.getClosestScaleNote(aMotif.getNote(0).getPitch() - 60 + lastNote, chordNotes)); // first note on chord
				for (int noteIndex = 0; noteIndex < aMotif.getNumNotes(); noteIndex++) {
					srcNote = aMotif.getNote(noteIndex);
					dstNote = aMotif.getModifiedNote(noteIndex);   

					//dstNote.setTime(aMotif.getModifiedNoteTime(noteIndex));
					dstNote.setPitch(srcNote.getPitch()  - 60 + lastNote + (int)(srcNote.getTime() * 4 * aMotif.getBend())); // rough pitch 
					dstNote.setPitch(Util.getClosestScaleNote(dstNote.getPitch(), scaleNotes));
					lastNote = dstNote.getPitch();
				}
*/
			}
		}
	}

	@Override
	public String toString() {
		return ("Naive");
	}

}
