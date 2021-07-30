package soundstrand;

import java.util.ArrayList;

import util.Util;

public class SimpleMelodyUpdater implements MelodyUpdater {
	
	@Override
	public void updateMedloy(Strand strand) {
		int lastNote = 60;
		ArrayList<Motif> motifList = strand.getMotifList();
		
		for (int motifIndex = 0; motifIndex < motifList.size(); motifIndex++) {
			Motif aMotif = motifList.get(motifIndex); 
			int[] scaleNotes;
			if (aMotif.getChord().getColor() == ChordColor.MAJOR) {
				scaleNotes = MusicUtil.Scale.DIATONIC_MAJOR.intervals;
			}
			else {
				scaleNotes = MusicUtil.Scale.DIATONIC_NATURAL_MINOR.intervals;
			}
			for (int noteIndex = 0; noteIndex < scaleNotes.length; noteIndex++) {
				scaleNotes[noteIndex] += aMotif.getChord().root % 12;
			}
			int[] chordNotes = aMotif.getChord().getNotes();
			aMotif.getModifiedNote(0).setPitch(Util.getClosestScaleNote(aMotif.getNote(0).getPitch() - 60 + lastNote, chordNotes)); // first note on the chord 
			for (int noteIndex = 1; noteIndex < aMotif.getNumNotes(); noteIndex++) {
				Note srcNote = aMotif.getNote(noteIndex);
				Note dstNote = aMotif.getModifiedNote(noteIndex);

				dstNote.setTime(aMotif.getModifiedNoteTime(noteIndex));
				dstNote.setPitch(srcNote.getPitch()  - 60 + lastNote + (int)(srcNote.getTime() * 4 * aMotif.getBend())); // rough pitch 
				dstNote.setPitch(Util.getClosestScaleNote(dstNote.getPitch(), scaleNotes)); // following notes on the scale
			}
		}
	}
}
