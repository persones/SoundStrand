package harmgen;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import soundstrand.MusicUtil;
import soundstrand.SpeacRule;
import soundstrandutil.XmlFilter;
import util.Util;

@SuppressWarnings("serial")
public class HarmGenGui extends JFrame{
	
	private final String contentDir = "../config";
	private TransitionTable transitionTable = new TransitionTable();
	final JTable transitionView = new JTable(new TransitionViewModel(transitionTable));
	
	public HarmGenGui() {
		initComponents();
	}

	private void initComponents() {
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
	
		JMenuItem fileNewItem = new JMenuItem("New");
		fileMenu.add(fileNewItem);
		fileNewItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				startNewTable();
			}
		});
		
		JMenuItem fileOpenItem = new JMenuItem("Open");
		fileMenu.add(fileOpenItem);		
		fileOpenItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(contentDir));
				fc.setFileFilter(new XmlFilter());
				int returnVal = fc.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	startNewTable();
		            File file = fc.getSelectedFile();
		            XmlReader aXmlReader = new XmlReader();
		            aXmlReader.read(file);
		            aXmlReader.updateTable(transitionTable);
		            transitionView.invalidate();
		            transitionView.revalidate();
		        }
			}
		});
		
		JMenuItem fileSaveItem = new JMenuItem("Save");
		fileMenu.add(fileSaveItem);
		
		fileSaveItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(contentDir));
				fc.setFileFilter(new XmlFilter());
				
				int returnVal = fc.showSaveDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            String ext = Util.getExtension(file);
		            if (ext == null) {
		            	file = new File(contentDir + "/" + file.getName() + ".xml");
		            }
					new XmlBuilder(file, transitionTable);
		        }
			}
		});
		
		JMenu ruleMenu = new JMenu("Rule");
		menuBar.add(ruleMenu);
		JMenuItem ruleNewItem = new JMenuItem("New");
		ruleMenu.add(ruleNewItem);
		
		ruleNewItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] names = new String[14];
				for (int i = 0; i < 7; i++) {
					names[i * 2] = MusicUtil.functionToRoman(i + 1);
					names[i * 2 + 1] = names[i * 2].toLowerCase();
				}
				String s = (String) JOptionPane.showInputDialog(null, "Choose function", "Choose functionCell", JOptionPane.PLAIN_MESSAGE, null, names, null);
				if (s != null) {
					transitionTable.addRule(s);
				}
				transitionView.invalidate();
				transitionView.revalidate();
			}
		});
		
		JMenuItem ruleDeleteItem = new JMenuItem("Delete");
		ruleMenu.add(ruleDeleteItem);
		ruleDeleteItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				transitionTable.deleteRule(transitionView.getSelectedRow());
				transitionView.invalidate();
	            transitionView.revalidate();
			}
		});
		
		

		transitionView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		transitionView.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				int ruleIndex = transitionView.getSelectedRow();
				int tensionIndex = transitionView.getSelectedColumn() - 1;
				if (tensionIndex > -1) {
					String[] names = new String[transitionTable.getNumRules()];
					for (int i = 0; i < transitionTable.getNumRules(); i++) {
						names[i] = transitionTable.getRule(i).state;
					}

					SpeacRule speacRule = transitionTable.getRule(ruleIndex);
					String s = (String) JOptionPane.showInputDialog(null, "Choose function", "Choose functionCell", JOptionPane.PLAIN_MESSAGE, null, names, transitionView.getValueAt(ruleIndex, tensionIndex));
					if (s != null) {	
						speacRule.transitions[transitionView.getSelectedColumn() - 1] = s;
						transitionTable.setRule(speacRule);
					}
				}
			}
		});
		JScrollPane transitionTableScrollPane = new JScrollPane(transitionView);
		transitionTableScrollPane.setPreferredSize(new Dimension(50, 200));
		transitionTableScrollPane.setLocation(0, 0);
		transitionTableScrollPane.setVisible(true);
		this.add(transitionTableScrollPane);
		
		//transitionView.setVisible(true);
		//transitionTableScrollPane.add(transitionView);
				
		setTitle("HTT Editor");
        setPreferredSize(new Dimension(500, 500));
        pack();	
	}
	
	protected void startNewTable() {
		transitionTable = new TransitionTable(); 
		transitionView.setModel(new TransitionViewModel(transitionTable));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HarmGenGui().setVisible(true);
            }
        });

	}

}
