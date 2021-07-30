package soundstrand;

import java.io.File;
import java.util.ArrayList;

public class SpeacHarmonyUpdater implements HarmonyUpdater {
	
	public SpeacHarmonyUpdater() {
		readRules(new File("../config/dynatest.xml"));
	}
	
	public void readRules(File file) {
		harmonyTransitionRules = new HarmonyTransitionRules(file); 
	}

	@Override
	public void updateHarmony(Strand strand) {
		String lastFunction = "I";
		ArrayList<Motif> motifList = strand.getMotifList();
		outputText.clear();
		
		for (int motifIndex = 0; motifIndex < motifList.size(); motifIndex++) {
			Motif aMotif = motifList.get(motifIndex);   
			int speacIndex = Math.round((aMotif.getRotation() + 1) * 2);
			lastFunction = harmonyTransitionRules.calculateFunction(lastFunction, speacIndex);
			aMotif.setFunction(lastFunction);
			aMotif.setChord(MusicUtil.romanToChord(lastFunction, GlobalParams.gKey));
			//aMotif.setChord(harmonyTransitionRules.calculateChord(lastFunction));
			
			outputText.add(Integer.toString(motifIndex) + ": " 
    				+ Integer.toString(aMotif.getMotifType()) + " "
    				+ HarmonyTransitionRules.SpeacIndexToLetter(speacIndex) + "  " 
    				+ lastFunction + " "
    				+ aMotif.getChord().toString() + "\n");   
		}
	}
	
	@Override
	public ArrayList<String> getOutput() {
		return outputText;
	}
	
	public String toString() {
		return ("Speac");
	}
	
	
	private HarmonyTransitionRules harmonyTransitionRules;
	private ArrayList<String> outputText = new ArrayList<String>();
	
}
