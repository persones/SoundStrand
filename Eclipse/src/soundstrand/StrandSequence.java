package soundstrand;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequencer;

public class StrandSequence extends SequencePreparer {
	
	public StrandSequence(float divisionType, int resolution)
			throws InvalidMidiDataException {
		super(divisionType, resolution);
	}
	
	public void setStrand(Strand s) {
		if (strand!= null) {
			strand.addHarmonyChangeListener(this);
		}

		strand = s;

		if (s != null) {
			s.addHarmonyChangeListener(this);
		}
	}
	
	public void setSeq(Sequencer seq) {
		this.seq = seq;
	}
		
	public void update() {
		removeAll();
		
		for (int motifIndex = 0; motifIndex < strand.getNumMotifs(); motifIndex++) {
			Motif aMotif = strand.getMotifList().get(motifIndex);
			Chord aChord = aMotif.getChord();
			
			// bass
			addNote(new Note(aChord.root + 36, 0, 4, 0), 127, motifIndex, bassChannel);
			// chord
			for (int noteIndex = 0; noteIndex  < aChord.getNotes().length; noteIndex++) {
				addNote(new Note(aChord.getNotes()[noteIndex] + 48, 0, 4, 0), 100, motifIndex, chordChannel);
			}
			// melody
			for (int noteIndex = 0; noteIndex < aMotif.getNumNotes(); noteIndex++) {
				Note aNote = aMotif.getModifiedNote(noteIndex);
				addNote(aNote, 127, motifIndex, melodyChannel);
			}
		}
		getTracks()[utilChannel].remove(endEvent);
		endEvent.setTick((long) (strand.getNumMotifs() * 4 * getResolution()));
		getTracks()[utilChannel].add(endEvent);
		
		if (null != seq) {
			if (seq.getTickPosition()  > 4 * strand.getNumMotifs() * GlobalParams.PPQ) {
				seq.setTickPosition(seq.getTickPosition() % (4 * GlobalParams.PPQ));
			}
			seq.setLoopStartPoint(0);
			seq.setLoopEndPoint(4 * strand.getNumMotifs() * GlobalParams.PPQ - 1);
		}
	}
	
	private Strand strand;
	private Sequencer seq;
}
