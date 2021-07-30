package phrasegen;

import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soundstrandutil.XmlFilter;
import util.Util;

@SuppressWarnings("serial")
public class MainGui extends JFrame {
	
	private final String contentDir = "../config";
	private PhraseView phraseView;
	private PatternManagerPanel patternManagerPanel;
	
	private CellManager cellManager = new CellManager();
	
	public MainGui() {
		initComponenets();
	}
		
	private void initComponenets() {
		
		JPanel mainPanel = new JPanel();
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem fileOpen = new JMenuItem("Open");
		fileOpen.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(contentDir));
				fc.setFileFilter(new XmlFilter());
				int returnVal = fc.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            XmlReader aXmlReader = new XmlReader();
		            cellManager = new CellManager(aXmlReader.read(file));
		            loadCell(0);
		        }
			}

			
		});
		
		
		fileMenu.add(fileOpen);
		
		JMenuItem fileSave = new JMenuItem("Save");
		fileSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
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
		            
					cellManager.saveCells(file);
				}
			}
		});
		
		fileMenu.add(fileSave);
		
		JMenu cellMenu = new JMenu("Cell");
		
		JMenuItem cellSelect = new JMenuItem("Select");
		cellSelect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] names = cellManager.getCellNames();
				String s = (String) JOptionPane.showInputDialog(null, "Choose cell", "Choose Cell", JOptionPane.PLAIN_MESSAGE, null, names, null);
				if (s != null) {
					loadCell(s);
				}
			}
		});
		
		JMenuItem cellNew = new JMenuItem("New");
		cellNew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = JOptionPane.showInputDialog(null, "Enter cell name:");
				if (s != null) {
					cellManager.addCell(s);
					loadCell(s);
				}
			}
		});
		
		JMenuItem cellDelete = new JMenuItem("Delete");
		cellMenu.add(cellSelect);
		cellMenu.add(cellNew);
		cellMenu.add(cellDelete);
		
		menuBar.add(fileMenu);
		menuBar.add(cellMenu);
		this.setJMenuBar(menuBar);
		
		
		phraseView = new PhraseView(null);
		JScrollPane scrollPane = new JScrollPane(phraseView);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		scrollPane.setLocation(0, 100);
		
		
		mainPanel.add(scrollPane);
		mainPanel.setBackground(new Color(176, 196, 222));
		//mainPanel.setBackground(Color.YELLOW);
		patternManagerPanel = new PatternManagerPanel(null);
		mainPanel.add(patternManagerPanel);
		
		setTitle("Cell Editor");
        setPreferredSize(new Dimension(800, 500));
        this.add(mainPanel);
        pack();	
	}
	
	private void loadCell(String s) {
		Cell c = cellManager.getCell(s);
		phraseView.setPhrase(c.getPhrase());
		patternManagerPanel.setCell(c);
	}
	
	private void loadCell(int i) {
		Cell c = cellManager.getCell(i);
		phraseView.setPhrase(c.getPhrase());
		patternManagerPanel.setCell(c);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGui().setVisible(true);
            }
        });
	}
}
