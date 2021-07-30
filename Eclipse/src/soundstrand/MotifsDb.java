package soundstrand;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MotifsDb {
	public MotifsDb(File file) {
		
		motifPrototypeList = new ArrayList<Motif>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList blockTypeList = doc.getElementsByTagName("block");

			for (int i = 0; i < blockTypeList.getLength(); i++) {
				Node block = blockTypeList.item(i);
				if (block.getNodeType() == Node.ELEMENT_NODE) {
										
					Motif motif = new Motif();
					motif.setType(Integer.parseInt(((Element)block).getAttribute("type")));
					
					Node blockChildNode = block.getFirstChild();
					
					while (blockChildNode != null) {
					
						// add notes
						if (blockChildNode.getNodeName().equals("pitches")) {
							NodeList pitchesListNode = ((Element)block).getElementsByTagName("pitches");
							NodeList notes = ((Element)pitchesListNode.item(0)).getElementsByTagName("note");
							int numNotes = notes.getLength();
							for (int y = 0; y < numNotes; y++ ) {
								Element xmlNote = (Element)notes.item(y);
								motif.addNote(new Note(Integer.parseInt(xmlNote.getAttribute("pitch")), Float.parseFloat(xmlNote.getAttribute("time")), 0.25f, 0.5f));
							}
						}
						
						else if (blockChildNode.getNodeName().equals("patterns")) {
							// add rythmic patrterns
							NodeList patternListNode = ((Element)block).getElementsByTagName("patterns");
							NodeList patterns = ((Element)patternListNode.item(0)).getElementsByTagName("pattern");
							for (int patternIndex = 0; patternIndex < patterns.getLength(); patternIndex++ ) {
								Node pattern = patterns.item(patternIndex);
								NodeList onsList = ((Element)pattern).getElementsByTagName("on");
								
								ArrayList<Float> ons = new ArrayList<Float>();
								for (int onIndex = 0; onIndex < onsList.getLength(); onIndex++) {
									Element on = (Element)onsList.item(onIndex);
									ons.add((Float)Float.parseFloat(on.getAttribute("time")));	
								}
								motif.addPattern(ons); 
							}
							
						}
						// add harmonic contextlist
						else if (blockChildNode.getNodeName().equals("contexts")) {
							NodeList contextListNode = ((Element)block).getElementsByTagName("contexts");
							if (contextListNode != null) {
								
								NodeList contexts = ((Element)contextListNode.item(0)).getElementsByTagName("context");
								for (int y = 0; y < contexts.getLength(); y++ ) {
									Element xmlContext = (Element)contexts.item(y);
									motif.addContext(new Chord(Integer.parseInt(xmlContext.getAttribute("root")), ChordColor.valueOf(xmlContext.getAttribute("color")), Integer.parseInt(xmlContext.getAttribute("inversion")))); 
								}
							}
						}
						
						blockChildNode = blockChildNode.getNextSibling();
					}
					motifPrototypeList.add(motif);
				}
				
			} 
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateMotif(Motif inMotif, int inBlockType) {
		for (int i = 0; i < motifPrototypeList.size(); i++) {		
				if (motifPrototypeList.get(i).getMotifType() == inBlockType) {
				motifPrototypeList.get(i).copyToMotif(inMotif);
				return;
			}
		}
		// if motif prototype not found return first prototype on list
		motifPrototypeList.get(0).copyToMotif(inMotif);
	}
	
	public int[] getTypeList() {
		int motifTypeList[] = new int[motifPrototypeList.size()];
		for (int i = 0; i < motifTypeList.length; i++) {
			motifTypeList[i] = motifPrototypeList.get(i).getMotifType();
		}
		return motifTypeList;
	}
	
	private ArrayList<Motif> motifPrototypeList;
}
