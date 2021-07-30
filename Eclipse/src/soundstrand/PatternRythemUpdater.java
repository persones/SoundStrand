package soundstrand;

import java.util.ArrayList;

public class PatternRythemUpdater implements RythemUpdater {

	@Override
	public void updateRythem(Strand strand) {
		for (int motifIndex = 0; motifIndex < strand.getMotifList().size(); motifIndex++) {
			System.out.print(motifIndex);
			System.out.print(":");
			Motif aMotif = strand.getMotifList().get(motifIndex);
			ArrayList<Float> aPattern = aMotif.getCurrentPattern();
			for (int noteIndex = 0; noteIndex < aMotif.getNumNotes(); noteIndex++) {
				aMotif.getNote(noteIndex).setTime(aPattern.get(noteIndex));
			}
		}
		System.out.println("");

	}

}
