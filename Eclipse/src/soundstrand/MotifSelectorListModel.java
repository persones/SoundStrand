package soundstrand;

import javax.swing.DefaultListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MotifSelectorListModel extends DefaultListModel<Integer> implements ChangeListener {

	
	public MotifSelectorListModel() {
	}
	
	public MotifSelectorListModel(Strand inStrand) {
		setStrand(inStrand);
	}
	
	public void setStrand(Strand s) {
		if (strand != null) {
            strand.removeHarmonyListener(this);
        }
        
        strand = s;
        
        if (strand != null) {
            strand.addStructureListener(this);
        }
	}
	@Override
	public void stateChanged(ChangeEvent e) {
		clear();
		clear();
		
		for (int i = 0; i < strand.getNumMotifs(); i++ ) {
			add(i, i);
		}
	}
	
	private Strand strand;

}
