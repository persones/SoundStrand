package phrasegen;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class Pattern {
	
	public Pattern(int inNoteDivision) {
		setLength(inNoteDivision);
		onsets = new boolean[length];
	}
	
	public void setOnset(int i, boolean b) {
		onsets[i] = b;
		fireChange(changeEvent);
	}
	
	public void toggle(int i) {
		onsets[i] = !onsets[i];
		fireChange(changeEvent);
	}
	
	public boolean getOnset(int i) {
		return onsets[i];
	}
	
	public int getLength() {
		return length;
	}
	public void setLength(int inLength) {
		this.length = inLength;
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
    
    public void removeAllChangeListeners() {
    	eventListenersList = new EventListenerList();
    }
    
    public int getNumEnteredNotes() {
    	int numNotes = 0;
    	for (int i = 0; i < length; i++) {
    		if (onsets[i]) {
    			numNotes++;
    		}
    	}
    	return numNotes;
    }
   
	private boolean[] onsets;
	private int length;
	private EventListenerList eventListenersList = new EventListenerList();
	private ChangeEvent changeEvent = new ChangeEvent(this);
	
}
