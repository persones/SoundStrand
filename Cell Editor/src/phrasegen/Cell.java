package phrasegen;

import java.util.ArrayList;

public class Cell {
	private ArrayList<Pattern> patternList = new ArrayList<Pattern>();
	private Phrase phrase = new Phrase(16);
	private String name;
	
	public Cell(String inName) {
		name = inName;
		patternList.add(new Pattern(16));
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Pattern> getPatternlist() {
		return patternList;
	}
	
	public Phrase getPhrase() {
		return phrase;
	}
	
	// TODO : melody

}
