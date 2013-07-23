package gfxelement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import drawing.NumberedFileComparator;

public abstract class GFXElement {
	
	private String filetype = "";
	private String filePrefix = "";
	private ArrayList<String> fileNumbers;
	private long duration;
	private long inDuration;
	private long outDuration;
	
	public ArrayList<String> getOverlayFileNames(String dir) {
		File file = new File(dir);  
		File[] files = file.listFiles();
		fileNumbers = new ArrayList<>();

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
			while (!(reverse.charAt(j) == '.')) { j++; }	//then we go back through our reversed string till we find a period. That's not going to work on unix then TODO: maybe "if system=windows"
			Logger.getGlobal().info("The first digit is at position: " + j); 
			String filetypeReversed = reverse.substring(0, j + 1);
			//we digress here to save out the filetype
			Logger.getGlobal().info("Here's the reversed filetype: " + filetypeReversed);
			filetype = new StringBuilder(filetypeReversed).reverse().toString(); //we reverse the filetype
			Logger.getGlobal().info("Filetype is: " + filetype);
			//carry on with getting the number
			String filetypeRemoved = reverse.substring(j + 1);	//then we remove the characters up to the first digit
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
	

	public long getInDuration() {
		inDuration = (getFirstHoldFrame() - 1) * 1000000 / 25;
		return inDuration;
	}
	
	public long getOutDuration() {
		//if we have a reverse element, we need to use the inverse of the usual manner of getting duration AND know what speed we want the animate out to be
		if (isReverse()) { outDuration = ((getNumberOfFrames() - (getNumberOfFrames() - getLastHoldFrame() + 1)) * 1000000 /25) / getSpeed(); }
		else { outDuration = (getNumberOfFrames() - getLastHoldFrame() + 1) * 1000000 /25; }//TODO: make sure framerate is never going to be zero
		
		return outDuration;
	}


	
	public abstract int getFirstHoldFrame();
	public abstract int getLastHoldFrame();
	public abstract int getNumberOfFrames();
	
	
	public long getDuration(long frameRateDivisor) { //TODO: this is just the duration of the media, that ok? what about the out and in durations?
		duration = fileNumbers.size() * 1000000 / 25;
		return duration;
		}
	
	public abstract boolean isReverse();
	public abstract boolean isLoop();
	public abstract int getSpeed();
	

	public abstract int getXOffsetSD();

	public abstract int getYOffsetSD();

}
