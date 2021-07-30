package phrasegen;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlReader {
	private ArrayList<Cell> cellList;
	
	public ArrayList<Cell> read(File inFile) {
	
		cellList = new ArrayList<Cell>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inFile);
			doc.getDocumentElement().normalize();

			NodeList blockTypeList = doc.getElementsByTagName("block");


			for (int i = 0; i < blockTypeList.getLength(); i++) {
				Node block = blockTypeList.item(i);
				if (block.getNodeType() == Node.ELEMENT_NODE) {
					String cellName = ((Element) block).getAttribute("type");
					Cell aCell = new Cell(cellName); // TODO : name is name, type is type. fix !!
					
					// add notes
					NodeList pitchesListNode = ((Element)block).getElementsByTagName("pitches");
					NodeList notes = ((Element)pitchesListNode.item(0)).getElementsByTagName("note");
					int numNotes = notes.getLength();
					for (int y = 0; y < numNotes; y++ ) {
						Element xmlNote = (Element)notes.item(y);
						int noteTime = Math.round(Float.parseFloat(xmlNote.getAttribute("time")) * 4) ;
						int notePitch = Integer.parseInt(xmlNote.getAttribute("pitch"));
						aCell.getPhrase().setNoteAtTime(noteTime, notePitch);
					}
					// add rythmic patrterns
					NodeList patternListNode = ((Element)block).getElementsByTagName("patterns");
					NodeList patterns = ((Element)patternListNode.item(0)).getElementsByTagName("pattern");
					for (int patternIndex = 0; patternIndex < patterns.getLength(); patternIndex++ ) {
						Node pattern = patterns.item(patternIndex);
						NodeList onsList = ((Element)pattern).getElementsByTagName("on");
						
						Pattern aPattern = new Pattern(16); //  TODO magic number OMG !!!
						for (int onIndex = 0; onIndex < onsList.getLength(); onIndex++) {
							Element on = (Element)onsList.item(onIndex);
							aPattern.setOnset(Math.round(Float.parseFloat(on.getAttribute("time")) * 4), true);
						}
						aCell.getPatternlist().add(aPattern);
					}
					aCell.getPatternlist().remove(0);
					
					// add harmonic contextlist
					/*NodeList contextListNode = ((Element)block).getElementsByTagName("contexts");
					NodeList contexts = ((Element)contextListNode.item(0)).getElementsByTagName("context");
					for (int y = 0; y < contexts.getLength(); y++ ) {
						Element xmlContext = (Element)contexts.item(y);
						motif.addContext(new Chord(Integer.parseInt(xmlContext.getAttribute("root")), ChordColor.valueOf(xmlContext.getAttribute("color")), Integer.parseInt(xmlContext.getAttribute("inversion")))); 
					}
			*/
					cellList.add(aCell);
				}
			} 
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cellList;
	}
}
