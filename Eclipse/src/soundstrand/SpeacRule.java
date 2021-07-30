package soundstrand;

public class SpeacRule {
	public SpeacRule() {
	}
	
	public SpeacRule(String inFunction, String[] inTransitions) {
		System.arraycopy(inTransitions, 0, transitions, 0, 5);
		state = inFunction;;
	};
		
	public String[] transitions = new String[7];
	public String state;
}
