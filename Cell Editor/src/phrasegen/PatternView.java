package phrasegen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class PatternView extends JComponent implements ChangeListener{
	
	private Phrase phrase;

	public PatternView(Pattern inRhythmPattern, Phrase inPhrase) {
		pattern = inRhythmPattern;
		pattern.addChangeListener(this); 
		phrase = inPhrase;
		phrase.addChangeListener(this);
		setPreferredSize(new Dimension(200, 10));
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				float w  = (getWidth() - 1)/ pattern.getLength();
				int i = (int)(e.getX() / w);
				if (i < pattern.getLength()) {
					pattern.toggle(i);
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	public void paintComponent(Graphics g) {
		Color c;
		Graphics2D g2 = (Graphics2D)g;
		
		if (phrase.getNumEnteredNotes() == pattern.getNumEnteredNotes()) {
			c = Color.GREEN;
		}
		else {
			c = Color.PINK;
		}
		float w  = (getWidth() - 1)/ pattern.getLength();
		
		for (int i = 0; i < pattern.getLength(); i++ ) {
			g2.setColor(Color.BLACK);
			g2.drawRect((int)(i * w), 0, (int) w, getHeight() - 1);
			if (pattern.getOnset(i)) {
				g2.setColor(Color.BLACK);
			}
			else {
				g2.setColor(c);
			}
			g2.fillRect((int)(i * w) + 1, 1, (int)w - 1, getHeight() - 2);
		}
	}
	
	private Pattern pattern = null;

	@Override
	public void stateChanged(ChangeEvent e) {
		repaint();
	}
}
