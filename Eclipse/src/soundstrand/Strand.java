/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soundstrand;

import java.util.ArrayList;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import util.Util;

public class Strand {

    public Strand() {

        motifList = new ArrayList<Motif>();
        harmonyChangeListeners = new EventListenerList();
        structureChangeListeners= new EventListenerList();
    }

    public int addMotif(Motif m, int index) { 
        
        motifList.add(index, m);
        m.setStrand(this);
        fireHarmonyChange(harmonyChangeEvent);
        fireStructureChange(structureChangeEvent);
        return (motifList.size());
    }

    public int addMotif(int inMotifType, MotifsDb inMotifsDb, int index) { // TODO : delete
        Motif m = new Motif(inMotifType, inMotifsDb); 
        motifList.add(index, m);
        m.setStrand(this);
        fireHarmonyChange(harmonyChangeEvent);
        fireStructureChange(structureChangeEvent);
        return (motifList.size());
    }
      
    public int addMotif(int inMotifType, MotifsDb inMotifsDb) { // TODO : delete
        Motif m = new Motif(inMotifType, inMotifsDb); 
        motifList.add(m);
        m.setStrand(this);
        fireHarmonyChange(harmonyChangeEvent);
        fireStructureChange(structureChangeEvent);
        return (motifList.size());
    }
    
    public void deleteMotif(int inMotifIndex) {
    	motifList.remove(inMotifIndex);
    	fireHarmonyChange(harmonyChangeEvent);
    	fireStructureChange(structureChangeEvent);
    }
    


    public Motif getSelected() {
    	if (motifList.size() > 0) {
    		return motifList.get(selectedMotifNum);
    	}
    	return (null);
    }
    
    public Motif get(int i) {
    	return motifList.get(i);
    }
    
    public void select(int i) {
    	if ((i  > 0) && (i <  motifList.size())) {
    		selectedMotifNum = i;
    	}
    	else {
    		selectedMotifNum = 0;
    	}
    }

    public void setSelectedMotifProperties(float e, float r, float b) {
        Motif aMotif = motifList.get(selectedMotifNum);
        aMotif.setElongation(e);
        aMotif.setRotation(r);
        aMotif.setBend(b);
        motifList.set(selectedMotifNum, aMotif);
    }
    
    public void setSelectedMotifElongation (float e) {
    	motifList.get(selectedMotifNum).setElongation(e);
    }
    public void setSelectedMotifRotation (float r) {
    	motifList.get(selectedMotifNum).setRotation(r);
    }
    public void setSelectedMotifBend (float b) {
    	motifList.get(selectedMotifNum).setBend(b);
    }
    
    public void setMotifProerties(int motifIndex, int motifId, int potValues[]) {
    	int numMotifs = motifList.size(); // if the motif index is out of bound add motifs to strand 
    	if (motifIndex  >= numMotifs) {
    		// add missing motifs
    		for (int i = numMotifs; i < motifIndex; i++) {
    			addMotif(0, motifsDb);
    		}
    		// now add the new one
        	addMotif(motifId, motifsDb);
    	}
    	
    	if (motifId != motifList.get(motifIndex).getMotifType()) {
    		deleteMotif(motifIndex);
    		addMotif(motifId, motifsDb, motifIndex);
    	}
    	
    	if (motifIndex == selectedMotifNum) {
    		setSelectedMotifProperties(Util.pot2Val(potValues[0]), Util.pot2Val(potValues[1]), Util.pot2Val(potValues[2]));
    	}
    	else {
    		Motif aMotif = motifList.get(motifIndex);
    		aMotif.setMotifProperties(potValues);
    	}
    	fireHarmonyChange(harmonyChangeEvent);
    }
    
    public void updateHarmony() {
    	rythemUpdater.updateRythem(this);
    	harmonyUpdater.updateHarmony(this);
    	melodyUpdater.updateMedloy(this);
    	fireHarmonyChange(harmonyChangeEvent);
    }		
    
    public ArrayList<Motif> getMotifList() {
    	return motifList;
    }
    
    public int getNumMotifs() {
    	return motifList.size();
    }
    
    public void setMotifsDb(MotifsDb inMotifsDb) {
    	motifsDb = inMotifsDb;
    }
    
    public void cutTailToFitLength(int length) {
    	while (motifList.size() > length) {
    		deleteMotif(motifList.size() - 1);
    	}
    }
    
    private void fireHarmonyChange(ChangeEvent e) {
        Object[] listeners = harmonyChangeListeners.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener)(listeners[i + 1])).stateChanged(e);
            }
        }
    }
    
    private void fireStructureChange(ChangeEvent e) {
        Object[] listeners = structureChangeListeners.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener)(listeners[i + 1])).stateChanged(e);
            }
        }
    }
    
    public void addHarmonyChangeListener(ChangeListener l) {
        harmonyChangeListeners.add(ChangeListener.class, l);
    }
    
    public void removeHarmonyListener(ChangeListener l) {
        harmonyChangeListeners.remove(ChangeListener.class, l);
    }
    
    public void addStructureListener(ChangeListener l) {
        structureChangeListeners.add(ChangeListener.class, l);
    }
    
    public void removeStructureListener(ChangeListener l) {
        structureChangeListeners.remove(ChangeListener.class, l);
    }
   
    public void setMelodyUpdater(MelodyUpdater m) {
    	melodyUpdater = m;
    	updateHarmony();
    }
    
    public void setHarmonyUpdater(HarmonyUpdater h) {
    	harmonyUpdater = h;
    	updateHarmony();
    }
    
    public HarmonyUpdater getHarmonyUpdater() {
    	return harmonyUpdater;
    }
    
    private ArrayList<Motif> motifList;
    private int selectedMotifNum;
    private HarmonyUpdater harmonyUpdater = new SpeacHarmonyUpdater();
    private MelodyUpdater melodyUpdater = new PhaseTwoMelodyUpdater();
    private RythemUpdater rythemUpdater = new PatternRythemUpdater();
    private MotifsDb motifsDb;
    
    private EventListenerList harmonyChangeListeners;
    private EventListenerList structureChangeListeners;
    private ChangeEvent harmonyChangeEvent = new ChangeEvent(this);
    private ChangeEvent structureChangeEvent = new ChangeEvent(this);		
}
