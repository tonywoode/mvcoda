package themes;

import java.util.Comparator;

/**
 * Compares two files in order to order them. Used to sort the filenames from GFXElement
 *
 */
public class NumberedFileComparator implements Comparator<Object> {
//http://answers.yahoo.com/question/index?qid=20090616170823AAPtPWG
	
	public int compare(Object o1, Object o2) {
		String f1 = (String)o1;
		String f2 = (String)o2;
		int val = f1.compareTo(f2);
		if (val != 0){
			int int1 = Integer.parseInt(f1);
			int int2 = Integer.parseInt(f2);
			val = int1 - int2;
		}
		return val;
	}
}