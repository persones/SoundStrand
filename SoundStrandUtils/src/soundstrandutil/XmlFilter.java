package soundstrandutil;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import util.Util;

public class XmlFilter extends FileFilter {

	public String getDescription() {
		// TODO Auto-generated method stub
		return "XML file";
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = Util.getExtension(f);
		if (extension != null) {
			if (extension.equals("XML") ||
					extension.equals("xml")) {
				return true;
			} 
			else {
				return false;
			}
		}

		return false;
	}
}
