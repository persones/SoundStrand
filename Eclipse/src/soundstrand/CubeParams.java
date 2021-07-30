package soundstrand;

public class CubeParams {
	public int potList[];
	public int id;
	
	public CubeParams() {
		potList = new int[3];
	}
	
	public CubeParams(int inId, int inPots[]) {
		id = inId;
		potList = new int[3];
		for (int i = 0; i < 3; i++) {
			potList[i] = inPots[i];
		}
	}
}
