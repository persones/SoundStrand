package phrasegen;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PatternManagerPanel extends JPanel {
	
	
	private ArrayList<PatternPanel> patternPanelList = new ArrayList<PatternPanel>();
	private ArrayList<Pattern> patternList;
	private Cell cell;
	
	public PatternManagerPanel (Cell c) {
		setCell(c);
		initComponents();
	}
	
	private void initComponents() {
		setPreferredSize(new Dimension(400, 200));
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		addButtonPressed(null);
	}
	
	public void setCell(Cell c) {
		if (cell != null) {
			for (int i = 0; i < patternList.size(); i++) {
				patternList.get(i).removeAllChangeListeners();
			}
		}
		if (c != null) {
			cell = c;
			patternList = c.getPatternlist();
			patternPanelList = new ArrayList<PatternPanel>();

			for (int i = 0; i < patternList.size(); i++) {
				PatternPanel v = new PatternPanel(patternList.get(i), this, cell.getPhrase());
				patternPanelList.add(v);
			}
		}
		refreshPanels();
		validate();
		repaint();
	}
	
	public void addButtonPressed(PatternPanel patternPanel) {
		
		int i;
		if (patternPanel == null) {
			i = -1;
		}
		else {
			i = patternPanelList.lastIndexOf(patternPanel);
		}
		if (patternList != null) {
			Pattern p = new Pattern(16);
			PatternPanel v = new PatternPanel(p, this, cell.getPhrase());
			patternList.add(i + 1,p);
			patternPanelList.add(i + 1, v);
		}
		refreshPanels();
		revalidate();
	}
	
	public void removeButtonPressed(PatternPanel patternPanel) {
		if (patternList.size() == 1) {
			return;
		}
		
		int i = patternPanelList.lastIndexOf(patternPanel);
		patternList.remove(i);
		patternPanelList.remove(i);
		remove(patternPanel);
		revalidate();
	}

	public void downButtonPressed(PatternPanel patternPanel) {
		int i = patternPanelList.lastIndexOf(patternPanel);
		if (i < (patternPanelList.size() - 1)) {
			
			Pattern p = patternList.get(i);
			PatternPanel v = patternPanelList.get(i);
			
			patternList.remove(i);
			patternPanelList.remove(i);
			
			patternList.add(i + 1, p);
			patternPanelList.add(i + 1, v);
			refreshPanels();
			revalidate();
		}
	}

	public void upButtonPressed(PatternPanel patternPanel) {
		int i = patternPanelList.lastIndexOf(patternPanel);
		if (i > 0) {
			
			Pattern p = patternList.get(i);
			PatternPanel v = patternPanelList.get(i);
			
			patternList.remove(i);
			patternPanelList.remove(i);
			
			patternList.add(i - 1, p);
			patternPanelList.add(i - 1, v);
			refreshPanels();
			revalidate();
		}
	}

	private void refreshPanels() {
		Component comps[] = getComponents();		
		for (int i = 0; i < comps.length; i++) {
			remove(comps[i]);
		}
		for (int i = 0; i < patternPanelList.size(); i++) {
			add(patternPanelList.get(i));
		}
	}
}
