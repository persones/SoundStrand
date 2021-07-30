package soundstrand;

public class MusicUtil {

	/**
	 * Reference frequency for middle A.
	 */
	public static final double A_FREQ = 440;

	/**
	 * MIDI note value for middle A.
	 */
	public static final int A_MIDI = 69;

	/**
	 * Common names for diatonic notes. Indexed from "C" == 0.
	 */
	public static final String[] NOTE_NAMES = {"C ", "C#", "D ", "D#", "E ", "F ", "F#", "G ", "G#", "A ", "A#", "B "};

	private static final double LOG2 = Math.log(2);

	/**
	 * 
	 * Scale This enum presents several common scales. Each entry is an intex of
	 * diatonic intervals for each member of the scale.
	 * 
	 * @author Peter
	 * 
	 */
	public enum Scale {
		CHROMATIC(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}),
		DIATONIC_MAJOR(new int[] {0, 2, 4, 5, 7, 9, 11}),
		DIATONIC_NATURAL_MINOR(new int[] {0, 2, 3, 5, 7, 8, 10}),
		DIATONIC_HARMONIC_MINOR(new int[] {0, 2, 3, 5, 7, 8, 11}),
		DIATONIC_MELODIC_ASCENDING_MINOR(new int[] {0, 2, 3, 5, 7, 9, 11}),
		DIATONIC_MELODIC_DESCENDING_MINOR(new int[] {0, 2, 3, 5, 7, 8, 10}),
		HEXATONIC_WHOLE_TONE(new int[] {0, 2, 4, 6, 8, 10}),
		PENTATONIC_MAJOR(new int[] {0, 2, 4, 7, 9}),
		PENTATONIC_MINOR(new int[] {0, 3, 5, 7, 10}),
		OCTATONIC(new int[] {0, 2, 3, 5, 6, 8, 9, 11});

		public final int[] intervals;

		Scale(int[] intervals) {
			this.intervals = intervals;
		}
	}

	public static final int[] TENSION_SCORE = {0, 6, 5, 3, 2, 1, 4, 1, 2, 3, 5, 6};
	public static final int[] TENSION_SORTED_INTERVALS = {0, 5, 7, 4, 8, 3, 9, 6, 2, 10, 1, 11};
	// public static final int[][] TENSION_GROUPS = { {0}, {5, 7}, {4, 8}, {3, 9},
	// {6}, {2, 10}, {1, 11}};
	public static final int[][] TENSION_GROUPS = { {0}, {5}, {4}, {3}, {6}, {2}, {1}};

	private MusicUtil() {

	}

	public static String noteToName(int note) {
		return NOTE_NAMES[note % 12] + " " + (note / 12);
	}

	public static int nameToNote(String name) {
		String[] parts = name.split(" ");
		int octave = Integer.parseInt(parts[1]);
		int noteIndex = 0;
		for (; noteIndex < NOTE_NAMES.length; noteIndex++) {
			if (NOTE_NAMES[noteIndex].equals(parts[0])) {
				break;
			}
		}

		return noteIndex + 12 * octave;
	}

	public static double nameToFreq(String name) {
		return noteToFreq(nameToNote(name));
	}

	public static int freqToNote(double freq) {
		return (int)Math.round(12 * Math.log(freq / A_FREQ) / LOG2) + A_MIDI;
	}

	public static double noteToFreq(int note) {
		return A_FREQ * Math.pow(2, (note - A_MIDI) / 12);
	}

	public static double freqToRatio(double freq) {
		return freqToRatio(freq, freqToNote(freq));
	}

	public static double freqToRatio(double freq, int note) {
		return 12 * Math.log(freq / noteToFreq(note)) / LOG2;
	}

	public static int freqToCents(double freq, int note) {
		return (int)Math.round(100 * freqToRatio(freq, note));
	}

	public static int freqToCents(double freq) {
		return (int)Math.round(100 * freqToRatio(freq));
	}

	public static String freqToName(double freq) {
		// double noteFrac = 12 * Math.log(freq / A_FREQ) / Math.log(2) + A_MIDI;
		// int note = (int)Math.round(noteFrac);
		// int cents = (int)Math.round((noteFrac - note) * 100);
		int note = freqToNote(freq);
		int cents = freqToCents(freq, note);
		return noteToName(note) + ((cents >= 0f) ? " +" : " ") + cents;
	}

	public static String functionToRoman(int f) {
		switch(f) {
		case 1: return "I";
		case 2: return "II";
		case 3: return "III";
		case 4: return "IV";
		case 5: return "V";
		case 6: return "VI";
		case 7: return "VII";
		}
		return null;
	}

	public static int RomanToNumber(String s) {

		switch (s.toUpperCase()) {
		case "I" 	: return 1;
		case "II" 	: return 2;
		case "III" 	: return 3;
		case "IV" 	: return 4;
		case "V" 	: return 5;
		case "VI" 	: return 6;
		case "VII" 	: return 7;
		}
		return -1;
	}

	public static int romanToInt(String s) {
		switch (s.toUpperCase()) {
		case "I" 	: return 1;
		case "II" 	: return 2;
		case "III" 	: return 3;
		case "IV" 	: return 4;
		case "V" 	: return 5;
		case "VI" 	: return 6;
		case "VII" 	: return 7;
		}
		return -1;
	}
	public static Chord romanToChord(String s, Scale scale) { // TODO : this belongs to Utils
		//collect roman letters
		 
		String roman;
		int i = 0;
		while (( i < s.length()) && isRoman(s.charAt(i))) {
			i++;
		}
		
		roman = s.substring(0, i);
		ChordColor chordColor;
		if ((s.charAt(0) == 'i') || (s.charAt(0) == 'v')) {
			chordColor = ChordColor.MINOR;
		}
		else {
			chordColor = ChordColor.MAJOR;
		}
		int root = scale.intervals[MusicUtil.RomanToNumber(roman) - 1];
		i++;
		
		if (i < s.length()) {
			if (s.charAt(i) == '#') {
				root++;
			}
			else if (s.charAt(i) == 'b') {
				root--;
			}
		}
		return new Chord(root, chordColor, 0);
	}
	
	public static boolean isRoman(char c) {
		if ((c == 'i') || (c == 'I') || (c == 'V') || (c == 'v')) {
			return true;
		}
		return false;
	}
	
}