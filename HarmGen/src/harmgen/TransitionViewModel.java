package harmgen;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import soundstrand.SpeacRule;

@SuppressWarnings("serial")
class TransitionViewModel extends AbstractTableModel implements ChangeListener {
	
	private TransitionTable transitionTable = new TransitionTable();
	private ArrayList<String[]> data = new ArrayList<String[]>(); 
	
	public TransitionViewModel(TransitionTable tt) {
		setTransitionTabel(tt);
	}
	
	public void setTransitionTabel(TransitionTable tt) {
		transitionTable = tt;
		transitionTable.addChangeListener(this);
	}
	

	private String[] columnNames = {
			"Previous",
			"Statement",
			"Preperation",
			"Extension",
			"Antecedent",
			"Consequence"
			};
	private String[] rowNames = {
	};	

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	public String getRowName(int row) {
		return rowNames[row];
	}

	public Object getValueAt(int row, int col) {
		return (data.get(row))[col];
	}

 
    public boolean isCellEditable(int row, int col) {
    	return false;
    
    }
    public void setValueAt(Object value, int row, int col) {
    	while (row >= data.size()) {
    		data.add(new String[6]);
    	}
        (data.get(row))[col] = (String) value;
        fireTableCellUpdated(row, col);
    }

	@Override
	public void stateChanged(ChangeEvent arg0) {
		for (int i = 0; i < transitionTable.getNumRules(); i++) {
			SpeacRule sr = transitionTable.getRule(i);
			setValueAt(sr.state, i, 0);
			for (int t = 0; t <  5; t++) {
				setValueAt(sr.transitions[t], i, t + 1);
			}
		}
		while (data.size() > transitionTable.getNumRules()) {
			data.remove(data.size() - 1);
		}
	}
}
