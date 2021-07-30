package soundstrand;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class SequencePreparer extends Sequence implements ChangeListener {

	public SequencePreparer(float divisionType, int resolution)
			throws InvalidMidiDataException {
		super(divisionType, resolution);
		for (int i = 0; i < 4; i++) {
			createTrack();
		}
		getTracks()[melodyChannel].add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE + melodyChannel, GlobalParams.melodyTimbre, 0), 0));
		getTracks()[bassChannel].add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE + bassChannel, GlobalParams.bassTimbre, 0), 0));
		getTracks()[chordChannel].add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE + chordChannel, GlobalParams.harmonyTimbre, 0), 0));
		endEvent = new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON + utilChannel, 0, 0), 4 * getResolution());
		getTracks()[utilChannel].add(endEvent);
		
	}

	protected void addNote(Note n, int velocity, int measure, int channel) {
	
		try {
			MidiEvent noteonEvent = new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON + channel, n.getPitch(), velocity), 
					(long) (((measure * 4) + n.getTime()) * (float)getResolution()));
			MidiEvent noteoffEvent = new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF + channel, n.getPitch(), 64), 
					(long) (((measure * 4) + n.getTime() + n.getDuration()) * (float)getResolution()) - 1);
			getTracks()[channel].add(noteonEvent);
			getTracks()[channel].add(noteoffEvent);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getMotifIndex() {
		return MotifIndex;
	}

	protected abstract void update();
	
	protected void removeAll() {
		for (int trackIndex = 0; trackIndex < 3; trackIndex++) {
			Track aTrack = getTracks()[trackIndex];
			while(aTrack.size() > 0) {
				aTrack.remove(aTrack.get(0));
			}
		}
		try {
			getTracks()[melodyChannel].add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE + melodyChannel, GlobalParams.melodyTimbre, 0), 0));
			getTracks()[bassChannel].add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE + bassChannel, GlobalParams.bassTimbre, 0), 0));
			getTracks()[chordChannel].add(new MidiEvent(new ShortMessage(ShortMessage.PROGRAM_CHANGE + chordChannel, GlobalParams.harmonyTimbre, 0), 0));
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		update();	
	}

	protected static final int melodyChannel = 0;
	protected static final int bassChannel = 1;
	protected static final int chordChannel = 2;
	protected static final int utilChannel = 3;
	protected MidiEvent endEvent;
	protected MidiEvent tempoEvent;
	protected int MotifIndex = 0;
	

}
