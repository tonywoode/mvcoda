package gfxelement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import drawing.NumberedFileComparator;

public abstract class GFXElement {
	
	private String filetype = "";
	private String filePrefix = "";
	
	
	public ArrayList<String> getOverlayFileNames(String dir) {
		File file = new File(dir);  
		File[] files = file.listFiles();
		ArrayList<String> fileNumbers = new ArrayList<>();

		for (int g = 0; g < files.length; g++) {

			String i = files[g].toString();
			Logger.getGlobal().info(i);
			String path = i;
			String base = dir;
			//first we need to get the relative path - we do this by effectively masking
			String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
			Logger.getGlobal().info("relative filename is: " + relative);
			//we can't substring backwards in java and we can only rely on getting numbers from the filetype backwards (in case filename has a number in).
			String reverse = new StringBuilder(relative).reverse().toString(); //so we reverse
			Logger.getGlobal().info("reversed relative filename is: " + reverse);
			int j = 0;
			while (!Character.isDigit(reverse.charAt(j))) { j++; }	//then we go back through our reversed string till we find a digit
			Logger.getGlobal().info("The first digit is at position: " + j); //TODO: hope you never get a filetype with a digit in - need to look for a period really
			String filetypeReversed = reverse.substring(0, j);
			//we digress here to save out the filetype
			Logger.getGlobal().info("Here's the reversed filetype: " + filetypeReversed);
			filetype = new StringBuilder(filetypeReversed).reverse().toString(); //we reverse the filetype
			Logger.getGlobal().info("Filetype is: " + filetype);
			//carry on with getting the number
			String filetypeRemoved = reverse.substring(j);	//then we remove the characters up to the first digit
			Logger.getGlobal().info("Chars removed up to first digit is : " + filetypeRemoved);	
			int k = 0;
			while (Character.isDigit(filetypeRemoved.charAt(k))) { k++; }	//then we continue through the numbers left until we find the first character
			Logger.getGlobal().info("The digits continue up to position:" + k);
			//digress again to get the file prefix name ie: what's before the digit
			String filePrefixReverse = filetypeRemoved.substring(k);
			Logger.getGlobal().info("File Prefix in reverse is: " + filePrefixReverse);
			filePrefix = new StringBuilder(filePrefixReverse).reverse().toString();
			Logger.getGlobal().info("So here's the file prefix:" + filePrefix);
			String numberExtracted = filetypeRemoved.substring(0,k); //then we remove anything past the last number
			Logger.getGlobal().info(numberExtracted);	
			String reverseBack = new StringBuilder(numberExtracted).reverse().toString(); //then we've got the digits out. We need to reverse the number back again
			Logger.getGlobal().info(reverseBack);
			fileNumbers.add(reverseBack);				
		}
		//now let's use the comparator to sort the arrayList - we'll just write back to the same arraylist TODO: good idea?

		Collections.sort(fileNumbers, new NumberedFileComparator());
		for (int i = 0; i < fileNumbers.size(); i++) {
			Logger.getGlobal().info(fileNumbers.get(i));
		}


		//now we can reconstitute the file list IN ORDER

		for (int l = 0; l < fileNumbers.size(); l++) {
			fileNumbers.set(l, dir + "/" + filePrefix + fileNumbers.get(l) + filetype);
		}

		return fileNumbers;
	}
	
	public abstract String getDirectory();

}