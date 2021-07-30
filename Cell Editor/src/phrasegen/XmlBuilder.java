package phrasegen;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlBuilder {
	
	public XmlBuilder(File file, ArrayList<Cell> cells) {
		Document doc = process(cells);
		save(doc, file);
	}
	
	/**
	 * @param cells
	 */
	public Document process(ArrayList<Cell> cells) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();	
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("blocks");
			doc.appendChild(rootElement);
			
			for (int cellIndex = 0; cellIndex < cells.size(); cellIndex++) {
				Cell aCell = cells.get(cellIndex);
				Element block = doc.createElement("block");
				rootElement.appendChild(block);
				block.setAttribute("type", Integer.toString(cellIndex));
				
				Element pitches = doc.createElement("pitches");
				block.appendChild(pitches);
				
				int onsetTime = 0;
				for (int noteIndex = 0; noteIndex < aCell.getPhrase().getNumEnteredNotes(); noteIndex++) {
					Element note = doc.createElement("note");
					pitches.appendChild(note);
					while (aCell.getPhrase().getNoteAtTime(onsetTime) == -1) {
						onsetTime++;
					}
					note.setAttribute("pitch", Integer.toString(aCell.getPhrase().getNoteAtTime(onsetTime)));
					note.setAttribute("time", String.format("%.2f", onsetTime / 4.0f));
					onsetTime++;
				}
				
				Element patterns = doc.createElement("patterns");
				block.appendChild(patterns);
				
				for (int patternIndex = 0; patternIndex < aCell.getPatternlist().size(); patternIndex++) {
					Element pattern = doc.createElement("pattern");
					patterns.appendChild(pattern);
					Pattern aPattern = aCell.getPatternlist().get(patternIndex);
					onsetTime = 0;
					for (int noteIndex = 0; noteIndex < aPattern.getNumEnteredNotes(); noteIndex++) {
						while (!aPattern.getOnset(onsetTime)) {
							onsetTime++;
						}
						Element on = doc.createElement("on");
						pattern.appendChild(on);
						on.setAttribute("time", String.format("%.2f", onsetTime / 4.0f));
						onsetTime++;
					}
				}
			}
			return doc;
		
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private void save(Document doc, File file) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
