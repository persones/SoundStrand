package soundstrand;

import javax.sound.midi.InvalidMidiDataException;

public class MotifSequence extends SequencePreparer {

	public MotifSequence(float divisionType, int resolution)
			throws InvalidMidiDataException {
		super(divisionType, resolution);
	}

	public void setMotif(Motif m, int n) {
		if (motif!= null) {
			motif.removeMotifChangeListener(this);
		}

		motif = m;
		MotifIndex = n;

		if (motif != null) {
			motif.addMotifChangeListener(this);
		}
		update();
	}

	public void update() {

		removeAll();

		Chord aChord = motif.getChord();
		// bass
		addNote(new Note(aChord.root + 36, 0, 4, 0), 127, 0, bassChannel);
		// chord
		for (int noteIndex = 0; noteIndex  < aChord.getNotes().length; noteIndex++) {
			addNote(new Note(aChord.getNotes()[noteIndex] + 48, 0, 4, 0), 100, 0, chordChannel);
		}
		// melody
		for (int noteIndex = 0; noteIndex < motif.getNumNotes(); noteIndex++) {
			Note aNote = motif.getModifiedNote(noteIndex);
			addNote(aNote, 127, 0, melodyChannel);
		}
	}
	

	private Motif motif;
	

}
