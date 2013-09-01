package util;

import java.io.File;
import java.util.logging.Logger;

public class FileUtil {


	public static String getFiletype(String fileUNC) {//TODO: will error if theres no parent directory ie: if we send a relative link to the proect src folder

		String i = fileUNC;
		String path = i;
		String base = fileUNC.toString();
		//first we need to get the relative path - we do this by effectively masking
		//String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
		//Logger.getGlobal().info("relative filename is: " + relative);
		//we can't substring backwards in java and we can only rely on getting numbers from the filetype backwards (in case filename has a number in).
		String reverse = new StringBuilder(fileUNC).reverse().toString(); //so we reverse
		Logger.getGlobal().info("reversed relative filename is: " + reverse);
		int j = 0;
		while (!(reverse.charAt(j) == '.')) { j++; }	//then we go back through our reversed string till we find a period. That's not going to work on unix then TODO: maybe "if system=windows"
		Logger.getGlobal().info("The first digit is at position: " + j); 
		String filetypeReversed = reverse.substring(0, j + 1);
		//we digress here to save out the filetype
		Logger.getGlobal().info("Here's the reversed filetype: " + filetypeReversed);
		String filetype = new StringBuilder(filetypeReversed).reverse().toString(); //we reverse the filetype
		return filetype;
	}
}
