package soundstrand;

import java.util.ArrayList;

public interface HarmonyUpdater {
	void updateHarmony(Strand strand);
	ArrayList<String> getOutput();
}
