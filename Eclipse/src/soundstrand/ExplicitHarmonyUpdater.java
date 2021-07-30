package soundstrand;

import java.util.ArrayList;

public class ExplicitHarmonyUpdater implements HarmonyUpdater {
	
	public ExplicitHarmonyUpdater() {
	}

	@Override
	public void updateHarmony(Strand strand) {
		ArrayList<Motif> motifList = strand.getMotifList();
		outputText.clear();
		
		for (int motifIndex = 0; motifIndex < motifList.size(); motifIndex++) {
			Motif aMotif = motifList.get(motifIndex);
			ArrayList<Chord> HarmonicContextList = aMotif.getHarmonicContextList();
			int currentHarmonyContextIndex = Math.round((aMotif.getRotation()+ 1) / 2 * (HarmonicContextList.size() - 1));
			aMotif.setFunction("I"); // TODO : 0 is not a function !
			aMotif.setChord(HarmonicContextList.get(currentHarmonyContextIndex));
			
			outputText.add(Integer.toString(motifIndex) + ": " 
    				+ Integer.toString(aMotif.getMotifType()) + " "
    				+ aMotif.getChord().toString() + "\n");   
		}
	}
	
	@Override
	public ArrayList<String> getOutput() {
		return outputText;
	}
	
	public String toString() {
		return ("Explicit");
	}
	
	private ArrayList<String> outputText = new ArrayList<String>();
	
}
