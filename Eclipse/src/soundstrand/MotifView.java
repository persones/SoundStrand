/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package soundstrand;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author powers4
 */
@SuppressWarnings("serial")
public class MotifView extends JComponent implements ChangeListener {
    
    public MotifView() {
    }
    
    public MotifView(Motif m) {
        setMotif(m);
        setPreferredSize(new Dimension(400, 400));
    }
    
    public void setMotif(Motif m) {
        if (motif != null) {
            motif.removeMotifChangeListener(this);
        }
        
        motif = m;
        
        if (motif != null) {
            motif.addMotifChangeListener(this);
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
    	if (motif != null) {
    		
    		for(int i = 0; i < motif.getNumNotes(); i++) {
    			Note note = motif.getModifiedNote(i);
    			g2.setColor(new Color(motif.getBend() / 2f + 0.5f, motif.getElongation() / 2f + 0.5f, motif.getRotation() / 2f + 0.5f));
    			g2.fillRect((int)(getWidth() / 4 * note.getTime()), 
    					getHeight()  - (getHeight() / 24 * (note.getPitch() - 48)),
    					(getWidth() / 16),
    					(getHeight() / 24));
    		}
    	}
    }

    private Motif motif;
    
}
