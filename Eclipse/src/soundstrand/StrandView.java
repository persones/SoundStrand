package soundstrand;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class StrandView extends JComponent implements ChangeListener {
	
    public StrandView(Strand s) {
        setStrand(s);
        setPreferredSize(new Dimension(400, 120));
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
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D)g;
    	int numMotifs = strand.getNumMotifs();
    	for (int motifIndex = 0; motifIndex < numMotifs; motifIndex++) {
    		Motif motif = strand.getMotifList().get(motifIndex);
    		for(int i = 0; i < motif.getNumNotes(); i++) {
    			Note note = motif.getModifiedNote(i);
    			g2.setColor(motifColor[motifIndex % motifColor.length]);
    			g2.fillRect((int)(getWidth() / (GlobalParams.BAR_LENGTH * numMotifs) * (note.getTime() + (GlobalParams.BAR_LENGTH * motifIndex))), 
    					getHeight()  - (getHeight() / 48 * (note.getPitch() - 36)),
    					(getWidth() / (16 * numMotifs)),
    					(getHeight() / 48));
    		}
    	}
    	g2.setColor(Color.RED);
    	if (numMotifs > 0) {
    		int linepos = (int)((getWidth() / (GlobalParams.BAR_LENGTH * numMotifs)) * curserLocation);
    		g2.drawLine(linepos, 0, linepos, getHeight());
    	}
    }
	
	public void setCurserLocation(long curserLocationTicks) {
		curserLocation = (float)curserLocationTicks / GlobalParams.PPQ;
		repaint();
	}

	private Strand strand;
    private static final Color motifColor[] = {Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW};
    private float curserLocation = 0;
}
