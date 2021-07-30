package harmgen;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import soundstrand.Chord;
import soundstrand.ChordColor;
import soundstrand.SpeacRule;


public class XmlReader {
	
	private ArrayList<SpeacRule> rules;
	
	public void updateTable(TransitionTable table) {
		table.reset();
		for (int i = 0; i < rules.size(); i++) {
			table.setRule(rules.get(i));
		}
	}
	
	public ArrayList<SpeacRule> read(File file) {
		rules = new ArrayList<SpeacRule>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList transitionList = doc.getElementsByTagName("transition");

			int len = transitionList.getLength();

			for (int i = 0; i < len; i++) {

				SpeacRule rule = new SpeacRule();
				Node transition = transitionList.item(i);
				rule.state = ((Element)transition).getAttribute("function");
				rule.transitions[0] = ((Element)transition).getAttribute("s");
				rule.transitions[1] = ((Element)transition).getAttribute("p");
				rule.transitions[2] = ((Element)transition).getAttribute("e");
				rule.transitions[3] = ((Element)transition).getAttribute("a");
				rule.transitions[4] = ((Element)transition).getAttribute("c");
				rules.add(rule);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rules;
	}	


	public Chord calculateChord(int function) {
		switch (function) {
		case 1: return (new Chord(0, ChordColor.MAJOR, 0));
		case 2: return (new Chord(2, ChordColor.MINOR, 0));
		case 3: return (new Chord(4, ChordColor.MINOR, 0));
		case 4: return (new Chord(5, ChordColor.MAJOR, 0));
		case 5: return (new Chord(7, ChordColor.MAJOR, 0));
		case 6: return (new Chord(9, ChordColor.MINOR, 0));
		case 7: return (new Chord(11, ChordColor.MINOR, 0));
		}
		return (new Chord(0, ChordColor.MAJOR, 0));
	}

	public static String SpeacIndexToLetter(int i) {
		switch(i) {
		case(0) : return "s";
		case(1) : return "p";
		case(2) : return "e";
		case(3) : return "a";
		case(4) : return "c";
		}
		return null;
	}
}



