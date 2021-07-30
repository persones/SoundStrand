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

import util.MusicUtil.Scale;


@SuppressWarnings("serial")
public class PhraseView extends JComponent implements ChangeListener {	

	private int phraseLength;
	private int noteRange = 24;
	private Phrase phrase;
	private int numNotesEntered;
	
	public PhraseView(Phrase p) {
		
		setPhrase(p);
		setPreferredSize(new Dimension(300, 300));
		addMouseListener(new MouseListener() {
		
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				int time = (int) (e.getX() / ((getWidth() - 1.0) / phraseLength));
				int pitch =  (int) ((getHeight() - e.getY()) / ((getHeight() - 1.0) / noteRange) + 48);
				phrase.mousePressed(time, pitch);
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
	}
	
	public void paintComponent(Graphics g) {
		
		if (phrase == null) {
			return;
		}
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.YELLOW);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
		
		//horizontal lines
		
		float verSpacing = (float) ((getHeight() - 1.0) / noteRange);
		
		int[] whites = Scale.DIATONIC_MAJOR.intervals;
		
		g2.setColor(Color.GRAY);
		for (int i = 0; i < noteRange; i++) {
			if(i % 12 == 0)
			{
				g2.setColor(Color.YELLOW);
			}
			else if (util.Util.findInArray(whites, i % 12)) {
				g2.setColor(Color.WHITE);
			}
			else
			{
				g2.setColor(Color.GRAY);
			}
			g2.fillRect(0, getHeight() - (int)((i + 1) * verSpacing) - 1, getWidth(), (int) verSpacing);
		}
		
		g2.setColor(Color.GRAY);
		for (int i = 0; i < (noteRange + 2); i++) {
			g2.drawLine(0, (int)(i * verSpacing), getWidth(), (int)(i * verSpacing));
			
		}
		
		// vertical lines
		float horSpacing = (float) ((getWidth() - 1.0) / phraseLength);
		for (int i = 0; i < (phraseLength + 2); i++) {
			g2.drawLine((int)(i * horSpacing), 0, (int)(i * horSpacing), getHeight());
			
		}
		
		numNotesEntered = 0;
		// notes
		g2.setColor(Color.BLACK);
		for (int i = 0; i < phraseLength; i++) {
			int p = phrase.getNoteAtTime(i) ;
			if (p != -1) {
				g2.fillRect((int)(i * horSpacing) + 1, getHeight() - (int)((p - 48 + 1) * verSpacing) - 1, (int)horSpacing, (int)verSpacing);
				numNotesEntered++;
			}
		}
	}
	
	public void setPhrase(Phrase inPhrase) {
		if (phrase != null) {
			phrase.removeChangeListener(this);
			
		}
		phrase = inPhrase;
		if (phrase != null) {
			phraseLength = phrase.getLength();
			phrase.addChangeListener(this);
		}
		repaint();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		repaint();	
	}

	public int getNumEnteredNotes() {
		return numNotesEntered;
	}

	
}
