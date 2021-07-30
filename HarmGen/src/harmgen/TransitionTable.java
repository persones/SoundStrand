package harmgen;	

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import soundstrand.SpeacRule;

public class TransitionTable {
	private ArrayList<SpeacRule> rules = new ArrayList<SpeacRule>();
	private EventListenerList eventListenersList = new EventListenerList();
	private ChangeEvent changeEvent = new ChangeEvent(this);
	
	public TransitionTable() {
	}
	
	public void reset() {
		// TODO : what do we do on reset ?
	}
	
	public void setRule(SpeacRule sr) {
		int i = findIndexOfFunction(sr.state);
		if (i == -1) {
			rules.add(sr);
		}
		else {
			rules.set(i, sr);
		}
		fireChange(changeEvent);
	}
	
	public void addRule(String s) {
		int i = findIndexOfFunction(s);
		if (i > -1) {
			return;
		}
		else {
			String[] transitions = new String[5];
			for (int t = 0; t < 5; t++) {
				transitions[t] = s; 
			}
			SpeacRule sr = new SpeacRule(s, transitions); 
			rules.add(sr);
		}
		fireChange(changeEvent);
	}
		
	public SpeacRule getRule(int i) {
		return rules.get(i);
	}
	
	public int getNumRules() {
		return rules.size();
	}	
	
	private int findIndexOfFunction(String s) {
		for (int i = 0; i < rules.size(); i++) {
			if (rules.get(i).state == s) {
				return i;
			}
		}
		return -1;
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
    
    private void fireChange(ChangeEvent e) {
		Object[] listeners = eventListenersList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener)(listeners[i + 1])).stateChanged(e);
            }
        }	
	}

	public void deleteRule(int ruleIndex) {
		for (int i = 0; i < rules.size(); i++) {
			if (i != ruleIndex) {
				for (int tension = 0; tension < 5; tension++) {
					if (rules.get(i).transitions[tension].equals(rules.get(ruleIndex).state)) {
						rules.get(i).transitions[tension] = rules.get(i).state;
					}
				}
			}	
		}
		rules.remove(ruleIndex);
		fireChange(changeEvent);
	}	
}
