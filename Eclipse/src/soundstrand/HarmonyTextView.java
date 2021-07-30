package soundstrand;

import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class HarmonyTextView extends JTextArea implements ChangeListener {

	public HarmonyTextView() {
	}
	
	public HarmonyTextView(Strand inStrand) {
		setStrand(inStrand);
	}
	
	public void setStrand(Strand s) {
		if (strand != null) {
            strand.removeHarmonyListener(this);
        }
        
        strand = s;
        
        if (strand != null) {
            strand.addHarmonyChangeListener(this);
        }
        repaint();  
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		selectAll();
    	replaceSelection(null);
    	ArrayList<String> text = strand.getHarmonyUpdater().getOutput();
    	for (int i = 0; i < text.size(); i++) {
    		append(text.get(i));
    	}
    	repaint();
    	
	}
	
	private Strand strand;

}
