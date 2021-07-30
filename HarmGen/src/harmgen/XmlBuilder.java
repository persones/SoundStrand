package harmgen;

import java.io.File;
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

import soundstrand.SpeacRule;

public class XmlBuilder {
	
	public XmlBuilder(File file, TransitionTable inTransitionTable) {
		Document doc = process(inTransitionTable);
		save(doc, file);
	}
	
	/**
	 * @param rules
	 */
	public Document process(TransitionTable tt) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();	
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("speac");
			doc.appendChild(rootElement);
			
			for (int ruleIndex = 0; ruleIndex < tt.getNumRules(); ruleIndex++) {
				SpeacRule aRule = tt.getRule(ruleIndex);
				Element transition = doc.createElement("transition");
				rootElement.appendChild(transition);
				transition.setAttribute("function", aRule.state);
				transition.setAttribute("s", aRule.transitions[0]);
				transition.setAttribute("p", aRule.transitions[1]);
				transition.setAttribute("e", aRule.transitions[2]);
				transition.setAttribute("a", aRule.transitions[3]);
				transition.setAttribute("c", aRule.transitions[4]);
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
