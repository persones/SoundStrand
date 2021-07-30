package soundstrand;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Util;


@SuppressWarnings("serial")
public class MainGui extends javax.swing.JFrame  {
	
	private final String contentDir = "../config";
	
	public MainGui() {
		
        strand = new Strand();
        
		try {
			strandSequence = new StrandSequence(Sequence.PPQ, GlobalParams.PPQ);
			strandSequence.setStrand(strand);
			motifSequence = new MotifSequence(Sequence.PPQ, GlobalParams.PPQ);
			seq = MidiSystem.getSequencer();
			seq.open();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        initComponents();
        serialManager = new SerialManager();
        if (serialManager.isPortActive()) {
        	serialManager.assignStrand(strand);
        	serialManager.addSlider(elongationSlider);
        	serialManager.addSlider(bendSlider);
        	serialManager.addSlider(rotationSlider);
        }
        else {
        	serialButton.setEnabled(false);
        }
        	
        motifListModel = new MotifSelectorListModel(strand);
        motifList.setModel(motifListModel);
        
        motifDb = new MotifsDb(new File("../config/testing.xml"));
        strand.setMotifsDb(motifDb); // TODO : this is bad, a new strand might wake up and not have a DB
        updateIdSelector();
        
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	serialManager.close();
            	System.exit(0);
            }
        });
    }
	
	private void updateIdSelector() {
		idDropDown.removeAllItems();
		int[] motifTypeList = motifDb.getTypeList();
        for (int i = 0; i < motifTypeList.length; i++) {
        	idDropDown.addItem(motifTypeList[i]);
        }
	}
	
	private void initComponents() {
		
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu cellMenu = new JMenu("Cells");
		menuBar.add(cellMenu);
		
		JMenuItem cellOpenItem = new JMenuItem("Open");
		cellMenu.add(cellOpenItem);
		cellOpenItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(contentDir));
				fc.setFileFilter(new soundstrandutil.XmlFilter());
				int returnVal = fc.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            motifDb = new MotifsDb(file);
		            updateIdSelector();
		        }
			}
		});
		
		JMenu speacMenu = new JMenu("Speac");
		menuBar.add(speacMenu);
		
		JMenuItem openSpeacItem = new JMenuItem("Open");
		speacMenu.add(openSpeacItem);
		openSpeacItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(contentDir));
				fc.setFileFilter(new soundstrandutil.XmlFilter());
				int returnVal = fc.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            speacHarmonyUpdater.readRules(file);
		            harmonyUpdatersDropdown.setSelectedItem(speacHarmonyUpdater);
		        }
			}
		});
		
		JMenu patchMenu = new JMenu("Patch");
		menuBar.add(patchMenu);
		
		JMenuItem melodyPatchItem = new JMenuItem("Melody");
		patchMenu.add(melodyPatchItem);
		melodyPatchItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GlobalParams.melodyTimbre = selectPatch(GlobalParams.melodyTimbre);
				strand.updateHarmony();
			}
		});
		
		JMenuItem harmonyPatchItem = new JMenuItem("Harmony");
		patchMenu.add(harmonyPatchItem);
		harmonyPatchItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GlobalParams.harmonyTimbre= selectPatch(GlobalParams.harmonyTimbre);
				strand.updateHarmony();
			}
		});
		
		JMenuItem bassPatchItem = new JMenuItem("Bass");
		patchMenu.add(bassPatchItem);
		bassPatchItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GlobalParams.bassTimbre = selectPatch(GlobalParams.bassTimbre);
				strand.updateHarmony();
			}
		});
		
		JMenu transposeMenu = new JMenu("Transpose");
		menuBar.add(transposeMenu);
		
		JMenuItem melodyTrasnposeItem = new JMenuItem("Melody");
		transposeMenu.add(melodyTrasnposeItem);
		melodyTrasnposeItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] octaves = {"-2", "-1", "0", "1", "2"};
				String s = (String) JOptionPane.showInputDialog(null, "Choose patch", "Choose patch", JOptionPane.PLAIN_MESSAGE, null, octaves, null);
				int index = Util.findInArray(octaves, s);
				if (index != -1) {
					GlobalParams.melodyOctavesTranspose = index - 2;
					strand.updateHarmony();
				}
			}
		});
				
		
		Color bgColor = new Color(210, 255,210);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		JPanel globalPanel = new JPanel();
		globalPanel.setLayout(new BoxLayout(globalPanel, BoxLayout.Y_AXIS) );
		
		JPanel motifManipPanel = new JPanel();
		motifManipPanel.setLayout(new BoxLayout(motifManipPanel, BoxLayout.X_AXIS));
		
		JPanel motifArrangeButtonPanel = new JPanel();
		motifArrangeButtonPanel.setLayout(new GridLayout(5, 1));
		
		JPanel transportPanel = new JPanel();
		transportPanel.setLayout(new BoxLayout(transportPanel, BoxLayout.X_AXIS));
		
        jScrollPane1 = new JScrollPane();

        elongationSlider = new JSlider();
        rotationSlider = new JSlider();
        bendSlider = new JSlider();
        elongationText = new JTextField();
        rotationText = new JTextField();
        bendText = new JTextField();
        idDropDown = new JComboBox<Integer>();
        mainPanel = new JPanel();
        
        JButton addMotifButton = new JButton();
        JButton deleteMotifButton  = new JButton();
        JButton playMotifButton = new JButton();
        JButton playAllButton = new JButton();
        loopEnableButton = new JButton();
        JButton stopButton = new JButton();
        JButton motifUpButton = new JButton();
        JButton motifDownButton = new JButton();
        
               
        motifListScrollPane = new javax.swing.JScrollPane();
        motifList = new JList<Integer>();
       
        strandView = new StrandView(strand);
		
		addMotifButton.setText("Add");
        addMotifButton.setActionCommand("AddMotifButton");
        addMotifButton.setVisible(true);
        
        deleteMotifButton.setText("Delete");
        deleteMotifButton.setActionCommand("DeleteMotifButton");
        deleteMotifButton.setVisible(true);
        
        motifUpButton.setText("up");
        motifUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                motifUpButtonActionPerformed(evt);
            }
        });
        
        motifDownButton.setText("down");
        motifDownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                motifDownButtonActionPerformed(evt);
            }
        });
        
        playMotifButton.setText("play");
        playMotifButton.setBackground(Color.GREEN);
        playMotifButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                playMotifButtonActionPerformed(evt);
            }
        });
        
        playAllButton.setText("play all");
        playAllButton.setBackground(Color.GREEN);
        playAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                playAllButtonActionPerformed(evt);
            }
        });
        
        stopButton.setText("stop");
        stopButton.setBackground(Color.RED);
        stopButton.setForeground(Color.WHITE);
        
        stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				seq.stop();	
			}
		});
        
        loopEnableButton.setText("Loop");
        loopEnableButton.setBackground(new Color(255, 215, 0));
        loopEnableButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				loopEnable = (!loopEnable);
				if (loopEnable) {
					loopEnableButton.setBackground(new Color(255, 255, 0));
					seq.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
				}
				else {
					loopEnableButton.setBackground(new Color(155, 115, 0));
					seq.setLoopCount(0);
				}
			}
		});
        
       
        serialButton = new JRadioButton("Serial");
        JRadioButton guiButton = new JRadioButton("Manual");
        ButtonGroup modeButtonGroup = new ButtonGroup();
        modeButtonGroup.add(serialButton);
        modeButtonGroup.add(guiButton);
        guiButton.setSelected(true);
        JPanel modePanel = new JPanel(new GridLayout(0, 1));
        modePanel.add(new JLabel("Interface"));
        modePanel.add(serialButton);
        modePanel.add(guiButton);
        modePanel.setBorder(BorderFactory.createBevelBorder(NORMAL));
        
        melodyUpdatersDropdown = new JComboBox<MelodyUpdater>();
        melodyUpdatersDropdown.addItem(new PhaseTwoMelodyUpdater());
        melodyUpdatersDropdown.addItem(new DiatonicMelodyUpdater());
        melodyUpdatersDropdown.addItem(new ChordNotesMelodyUpdater());
        melodyUpdatersDropdown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				strand.setMelodyUpdater((MelodyUpdater)melodyUpdatersDropdown.getSelectedItem());
			}
        });
        
        harmonyUpdatersDropdown = new JComboBox<HarmonyUpdater>();
        harmonyUpdatersDropdown.addItem(speacHarmonyUpdater);
        harmonyUpdatersDropdown.addItem(new ExplicitHarmonyUpdater());
        harmonyUpdatersDropdown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				strand.setHarmonyUpdater((HarmonyUpdater)harmonyUpdatersDropdown.getSelectedItem());
			}
		});
        
        JPanel updatersSelectionPanel = new JPanel(new GridLayout(0, 1));
        updatersSelectionPanel.add(new JLabel("Melody"));
        updatersSelectionPanel.add(melodyUpdatersDropdown);
        updatersSelectionPanel.add(new JLabel("Harmony"));
        updatersSelectionPanel.add(harmonyUpdatersDropdown);
        updatersSelectionPanel.setBorder(BorderFactory.createBevelBorder(NORMAL));
            
        serialButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		serialManager.setMode(true);
        		bendSlider.setEnabled(false);
        		elongationSlider.setEnabled(false);
        		rotationSlider.setEnabled(false);
        	}
        });
        guiButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		serialManager.setMode(false);
        		bendSlider.setEnabled(true);
        		elongationSlider.setEnabled(true);
        		rotationSlider.setEnabled(true);
        	}
        });
        
        harmonyView = new HarmonyTextView(strand);
        harmonyView.setColumns(30);
        harmonyView.setRows(9);
        harmonyView.setFont(new Font("Courier New", Font.PLAIN, 16));
        
        jScrollPane1.setViewportView(harmonyView);
        
        tempoSlider = new javax.swing.JSlider();
        tempoSlider.setOrientation(SwingConstants.VERTICAL);
        tempoSlider.setPreferredSize(new Dimension(20, 150));
        tempoSlider.setMinimum(40);
        tempoSlider.setMaximum(200);
        tempoSlider.setValue(120);
        tempoSlider.setMajorTickSpacing(1);
        tempoSlider.addChangeListener(new javax.swing.event.ChangeListener() {
        	public void stateChanged(javax.swing.event.ChangeEvent evt) {
        		GlobalParams.gTempo = tempoSlider.getValue();
        		Text.setText(Integer.toString(GlobalParams.gTempo));
        		float fCurrent = seq.getTempoInBPM();
        	    float fFactor = GlobalParams.gTempo / fCurrent;
        	    seq.setTempoFactor(fFactor);
        		seq.setTempoInBPM(GlobalParams.gTempo);
        	}
        });
        tempoSlider.setBackground(bgColor);
        Text = new JTextField();
        Text.setColumns(3);
        Text.setBorder(BorderFactory.createEtchedBorder());
        Text.setHorizontalAlignment(JTextField.CENTER);
        Text.setText(Integer.toString(tempoSlider.getValue()));

        globalPanel.add(new JLabel(""));
        globalPanel.add(tempoSlider);
        globalPanel.add(Text);
        globalPanel.setBackground(bgColor);
       
        addMotifButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addMotifButtonActionPerformed(evt);
            }
        });
        
        deleteMotifButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                deleteMotifButtonActionPerformed(evt);
            }
        });
        
        elongationSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                elongationChanged(evt);
            }
        });
        
        rotationSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                rotationChanged(evt);
            }
        });

        bendText.setText("Bend");
        bendSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                bendChanged(evt);
            }
        });

        elongationText.setText("Elongation");
        elongationText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                elongationTextActionPerformed(evt);
            }
        });

        rotationText.setText("Rotation");
        rotationText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                rotationTextActionPerformed(evt);
            }
        });
        
        JPanel motifManipulationSlidersPanel = new JPanel(new GridLayout(0, 1));
        motifManipulationSlidersPanel.add(bendSlider);
        motifManipulationSlidersPanel.add(elongationSlider);
        motifManipulationSlidersPanel.add(rotationSlider);
        
        JPanel motifManipulationTextPanel = new JPanel(new GridLayout(0, 1));
        motifManipulationTextPanel.add(bendText);
        motifManipulationTextPanel.add(elongationText);
        motifManipulationTextPanel.add(rotationText);
        
        JPanel motifManipulationLabelsPanel = new JPanel(new GridLayout(0, 1));
        motifManipulationLabelsPanel.add(new JLabel("Melody direction"));
        motifManipulationLabelsPanel.add(new JLabel("Rythmic distribuation"));
        motifManipulationLabelsPanel.add(new JLabel("Harmonic context"));
        
        motifList.setModel(new DefaultListModel<Integer>() {
            String[] strings = { "" };
            public int getSize() { return strings.length; }
            public Integer getElementAt(int i) { return this.getElementAt(i); }
        });

        motifList.setFixedCellHeight(20);
        motifList.setFixedCellWidth(10);
        motifList.setRequestFocusEnabled(false);
        motifList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
            	if (!evt.getValueIsAdjusting()) 
            	{
            		motifListValueChanged(evt);
            	}
            }
        });
        
        idDropDown.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				motifTypeChanged();				
			}
		});
        motifListScrollPane.setViewportView(motifList);
        motifListScrollPane.setPreferredSize(new Dimension(50, 200));
        
        strandView.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        strandView.setForeground(Color.BLACK);
        
        strandView.setMinimumSize(new Dimension(400, 200));
        strandView.setPreferredSize(new Dimension(400,200));
        
        mainPanel.add(jScrollPane1);
        motifArrangeButtonPanel.add(motifUpButton);
        motifArrangeButtonPanel.add(motifDownButton);
        motifArrangeButtonPanel.add(addMotifButton);
        motifArrangeButtonPanel.add(deleteMotifButton);
        motifArrangeButtonPanel.setBackground(bgColor);
        transportPanel.add(playMotifButton);
        transportPanel.add(playAllButton);
        transportPanel.add(stopButton);
        transportPanel.add(loopEnableButton);
        motifManipPanel.add(motifManipulationSlidersPanel);
        motifManipPanel.add(motifManipulationTextPanel);
        motifManipPanel.add(motifManipulationLabelsPanel);
        motifManipPanel.setBorder(BorderFactory.createEtchedBorder());
        motifManipPanel.setBackground(bgColor);
        mainPanel.add(globalPanel);
        mainPanel.add(motifArrangeButtonPanel);
        mainPanel.add(motifListScrollPane);
        mainPanel.add(idDropDown);
        mainPanel.add(updatersSelectionPanel);
        mainPanel.add(modePanel);
        mainPanel.add(motifManipPanel);
        mainPanel.add(strandView);
        mainPanel.add(transportPanel);
        
        
        setTitle("SoundStrand");
        setPreferredSize(new Dimension(500, 800));
		mainPanel.setBackground(bgColor);
        this.add(mainPanel);
        pack();
	}
	
	private void addMotifButtonActionPerformed(ActionEvent evt) {
        strand.addMotif(0, motifDb);
        motifList.setSelectedIndex(motifListModel.getSize()-1);
        elongationSlider.setValue(50);
        bendSlider.setValue(50);
        rotationSlider.setValue(0);
    }                                
	
	private void deleteMotifButtonActionPerformed(ActionEvent evt) {
		int aSelectedMotif = motifList.getSelectedIndex();
		strand.deleteMotif(aSelectedMotif);
		if (aSelectedMotif == motifListModel.getSize()) {
			motifList.setSelectedIndex(motifListModel.getSize() - 1);
		}
		else {
			motifList.setSelectedIndex(aSelectedMotif);
		}
		strand.updateHarmony();
	}
	
    private void rotationTextActionPerformed(ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void elongationTextActionPerformed(ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    private void elongationChanged(ChangeEvent evt) {                                   
        elongationText.setText(Float.toString(Util.slider2Val(elongationSlider.getValue())));
        if (null != currMotif) {
        	currMotif.setElongation(Util.slider2Val(elongationSlider.getValue()));
        }
    }                                  

    private void rotationChanged(ChangeEvent evt) {                                 
       rotationText.setText(Float.toString(Util.slider2Val(rotationSlider.getValue())));
       if (null != currMotif) {
    	   currMotif.setRotation(Util.slider2Val(rotationSlider.getValue()));
       }  
    }

    private void bendChanged(ChangeEvent evt) {                             
       bendText.setText(Float.toString(Util.slider2Val(bendSlider.getValue())));
       if (null != currMotif) {
    	   currMotif.setBend(Util.slider2Val(bendSlider.getValue()));
       }
    }

    private void playMotifButtonActionPerformed(ActionEvent evt) {  
    	seq.stop();
    	strandSequence.setSeq(null);
    	if (null != currMotif) {
    		try {
        		seq.setSequence(motifSequence);				
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
    		
    		CurserUpdaterThread t = new CurserUpdaterThread();
    		t.setS(seq);
    		t.setSv(strandView);
    		   		
    		seq.setLoopStartPoint(0);
    		seq.setLoopEndPoint(4 * GlobalParams.PPQ - 1);
    		seq.setTickPosition(0);
    		seq.setTempoInBPM(GlobalParams.gTempo);
    		seq.start();
    		t.start();
    	}
    }                                               

    private void motifUpButtonActionPerformed(ActionEvent evt) {                                              
        int currIndex = motifList.getSelectedIndex();
        if (currIndex > 0) {
        	Motif m = strand.getSelected();
            strand.deleteMotif(currIndex);
            strand.addMotif(m, currIndex - 1);
            strand.select(currIndex - 1);
            motifList.setSelectedIndex(currIndex - 1);
        }
    }                                             

    private void motifDownButtonActionPerformed(ActionEvent evt) {                                                
        int currIndex = motifList.getSelectedIndex();
        if (currIndex < (motifListModel.getSize() - 1)) {
        	Motif m = strand.getSelected();
            strand.deleteMotif(currIndex);
            strand.addMotif(m, currIndex + 1);
            strand.select(currIndex + 1);
            motifList.setSelectedIndex(currIndex + 1);
        }
    }
        private void motifListValueChanged(javax.swing.event.ListSelectionEvent evt) {                                       
        	selectMotif();
    }   

    private void playAllButtonActionPerformed(ActionEvent evt) {                                              
    	seq.stop();
    	strandSequence.setSeq(seq);
    	try {
    		seq.setSequence(strandSequence);				
    	} catch (InvalidMidiDataException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	CurserUpdaterThread t = new CurserUpdaterThread();
		t.setS(seq);
		t.setSv(strandView);
		
    	seq.setLoopStartPoint(0);
    	seq.setLoopEndPoint(4 * strand.getNumMotifs() * GlobalParams.PPQ - 1);
    	seq.setTickPosition(0);
    	seq.setTempoInBPM(GlobalParams.gTempo);
    	seq.start();
    	t.start();
    }
    
    private void motifTypeChanged() {
    	if ((null != currMotif) && (null != idDropDown.getSelectedItem())) {
    		int motifToReplaceIndex = motifList.getSelectedIndex();
    		if (currMotif.getMotifType() != Integer.parseInt(idDropDown.getSelectedItem().toString())) {
    			strand.deleteMotif(motifToReplaceIndex);
    			strand.addMotif(Integer.parseInt(idDropDown.getSelectedItem().toString()), motifDb, motifToReplaceIndex);
    			motifList.setSelectedIndex(motifToReplaceIndex);
    		}
    	}
    }
    
    private void selectMotif() {
        int aSelectedMotif = motifList.getSelectedIndex();
        strand.select(aSelectedMotif);
        currMotif = strand.getSelected();
        motifSequence.setMotif(currMotif, aSelectedMotif);
        if (null != currMotif) {
        	elongationSlider.setValue(Util.val2Slider(currMotif.getElongation()));
        	rotationSlider.setValue(Util.val2Slider(currMotif.getRotation()));
        	bendSlider.setValue(Util.val2Slider(currMotif.getBend()));
        	idDropDown.setSelectedItem(Integer.toString(currMotif.getMotifType()));
        	elongationText.setText(Float.toString(currMotif.getElongation()));     
        	rotationText.setText(Float.toString(currMotif.getRotation()));   
        	bendText.setText(Float.toString(currMotif.getBend()));  
        	motifSequence.setMotif(currMotif, aSelectedMotif);
        }
    }
    
    public int selectPatch(int patchNumber) {
    	String s = (String) JOptionPane.showInputDialog(null, "Choose patch", "Choose patch", JOptionPane.PLAIN_MESSAGE, null, PatchSelectorTools.patchList, PatchSelectorTools.patchList[patchNumber]);
		return Util.findInArray(PatchSelectorTools.patchList, s);
    	
    }
    
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGui().setVisible(true);
            }
        });
    }

	private JSlider tempoSlider;
	private JTextField Text;
    private JSlider bendSlider;
    private JTextField bendText;
    private JSlider elongationSlider;
    private JTextField elongationText;
    private JPanel mainPanel;
    private JScrollPane jScrollPane1;
    private JScrollPane motifListScrollPane;
    private JList<Integer> motifList;
    private HarmonyTextView harmonyView; 
    private JRadioButton serialButton;
    private JButton loopEnableButton;
    private javax.swing.JComboBox<Integer> idDropDown;
    JComboBox<MelodyUpdater> melodyUpdatersDropdown;
    JComboBox<HarmonyUpdater> harmonyUpdatersDropdown;
    
    private SpeacHarmonyUpdater speacHarmonyUpdater = new SpeacHarmonyUpdater();
    
    private JSlider rotationSlider;
    private JTextField rotationText;                
    private Strand strand;
    private Motif currMotif;

    private StrandView strandView;
    private DefaultListModel<Integer> motifListModel;
    private SerialManager serialManager;
    private MotifsDb motifDb; 

    private StrandSequence strandSequence = null;
    private MotifSequence motifSequence = null;
    private Sequencer seq = null;
    private Boolean loopEnable = false;
    
   
}
