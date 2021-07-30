package phrasegen;

import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PatternPanel extends JPanel {
	public PatternPanel(Pattern p, PatternManagerPanel m, Phrase inPhrase) {
		patternManagerPanel = m;
		
		setLayout((LayoutManager) new BoxLayout(this, BoxLayout.X_AXIS));
		add(new PatternView(p, inPhrase));
		
		JButton addButton = new JButton();
		addButton.setText("+");
		add(addButton);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addThis();
			}
		});
		
		JButton removeButton = new JButton();
		removeButton.setText("-");
		add(removeButton);
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeThis();
			}
		});
		
		JButton upButton = new JButton();
		upButton.setText("up");
		add(upButton);
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				upThis();	
			}
		});
		
		JButton downButton = new JButton();
		downButton.setText("dn");
		add(downButton);
		downButton.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				downthis();
			}
		});
	}
	
	protected void downthis() {
		patternManagerPanel.downButtonPressed(this);
		
	}

	protected void upThis() {
		patternManagerPanel.upButtonPressed(this);
		
	}

	protected void removeThis() {
		patternManagerPanel.removeButtonPressed(this);
		
	}

	private void addThis() {
		patternManagerPanel.addButtonPressed(this);
	}
	
	private PatternManagerPanel patternManagerPanel;
}
