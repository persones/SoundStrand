package phrasegen;

import java.io.File;
import java.util.ArrayList;

public class CellManager {
	
	private ArrayList<Cell> cellList;
	
	public CellManager(ArrayList<Cell> inCellList) {
		cellList = inCellList;
	}
	
	public CellManager() {
		cellList = new ArrayList<Cell>();
	}

	public void addCell(String s) {
		Cell c = new Cell(s);
		cellList.add(c);
	}
	
	public String[] getCellNames() {
		
		String[] s = new String[cellList.size()];
		for (int i = 0; i < cellList.size(); i++) {
			s[i] = cellList.get(i).getName();
		}
		return s;
	}
	
	public Cell getCell(String cellName) {
		for (int i = 0; i < cellList.size(); i++) {
			if (cellName.contentEquals(cellList.get(i).getName())) {
				return cellList.get(i);
			}
		}
		return null;
	}

	public void saveCells(File file) {
		@SuppressWarnings("unused")
		XmlBuilder xmlBuilder = new XmlBuilder(file, cellList); // TODO unused object
		
	}

	public Cell getCell(int i) {
		return cellList.get(i);
	}
}
