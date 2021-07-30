package phrasegen;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class Phrase {
	
	private EventListenerList eventListenersList = new EventListenerList();
	private ChangeEvent changeEvent = new ChangeEvent(this);
	private int length;
	private int[] notes;
	
	public Phrase(int inLength) {
		length = inLength;
		notes = new int[length];
		for (int i = 0; i < length; i++) {
			notes[i]= -1;
		}
	}
	
	public void mousePressed(int time, int inPitch) {
		if (notes[time] == inPitch) {
			notes[time] = -1;
		}
		else {
			setNoteAtTime(time, inPitch);
		}
		fireChange(changeEvent);
	}
	
	private void fireChange(ChangeEvent e) {
		Object[] listeners = eventListenersList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener)(listeners[i + 1])).stateChanged(e);
            }
        }	
	}
	
	public void addChangeListener(ChangeListener l) {
        eventListenersList.add(ChangeListener.class, l);
    }
    
    public void removeChangeListener(ChangeListener l) {
    	eventListenersList.remove(ChangeListener.class, l);
    }

	public int getLength() {		
		return length;
	}
	
	public int getNoteAtTime(int t) {
		return notes[t];
	}
	
	public void setNoteAtTime(int t, int inPitch) {
		notes[t] = inPitch;
	}
	
	public int getNumEnteredNotes() {
		int numNotes = 0;
		for (int i = 0; i < length; i++) {
			if (notes[i] != -1) {
				numNotes++;
			}
		}
		return numNotes;
	}
}
