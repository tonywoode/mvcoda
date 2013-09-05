package themes;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import playlist.Number;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;


import lombok.Getter;
import lombok.Setter;

@XStreamAlias("GFXElement")
public class GFXElement implements XMLSerialisable {

	//annotations because we'd like to omit some of these fields from the XML, they are calculated fields only for the class to use
	@XStreamOmitField private String filetype = "";
	@XStreamOmitField private String filePrefix = "";
	private ArrayList<String> fileNumbers;
	@XStreamOmitField private long duration;
	
	@Getter @Setter private String themeName;
	@Getter @Setter private String itemName;
	@Getter @Setter private String elementName;
	@Getter @Setter private String author;
	@Getter @Setter private String version;
	@Getter @Setter public CoOrd coOrd;
	
	private final static Logger LOGGER = Logger.getLogger(GFXElement.class.getName()); //setup a logger for this class

	public GFXElement(String themeName, String itemName, String elementName, String author, String version, CoOrd coOrd) {
		this.themeName = themeName;
		this.itemName = itemName;
		this.elementName = elementName;
		this.author = author;
		this.version = version;
		this.coOrd = coOrd;
	}


	public Path getDirectory() {
		if (isNumbers()) return (Paths.get( Theme.getRootDir().toString(), themeName, elementName, Number.getNumber() +itemName ) );
		return Paths.get(Theme.getRootDir().toString(), themeName, elementName, itemName );
	}
	

	//have these getters so we don't have to call clasA.classB.getxoffset()
	public int getXOffsetSD() {	return coOrd.getXOffsetSD(); }
	public int getYOffsetSD() {	return coOrd.getYOffsetSD(); }


	public long getDuration(long frameRateDivisor) { //TODO: this is just the duration of the media, that ok? what about the out and in durations?
		duration = convertFrameToTime( fileNumbers.size() );
		return duration;
	}

	//we need these so that we can call an AnimatedGFXElement as a GFXElement
	public long getInDuration() { return 0;	}
	public long getOutDuration() { return 0; }

	//an animatedGFXElement has none of these things //TODO: I wish we could get this stuff out of here then
	public boolean isReverse() { return false; }
	public boolean isLoop() { return false; }
	public boolean isNumbers() { return false; }
	public int getSpeed() { return 1; } //TODO: is 1x speed correct for a static element?
	public int getFirstHoldFrame() { return -1; }
	public int getLastHoldFrame() { return -1; }
	public int getNumberOfFrames() { return 1; }


	public static long convertFrameToTime(long frames) {
		//TODO: if we divide by zero, throw an exception
		long result = frames * 1000000; //the time basis
		result = result / 25; //the frame rate //TODO: magic numbers....
		return result;
	}


	public ArrayList<String> getOverlayFileNames(Path dir) {
		File file = new File(dir.toString());  
		File[] files = file.listFiles();
		fileNumbers = new ArrayList<>();

		for (int g = 0; g < files.length; g++) {
			LOGGER.setLevel(Level.OFF); //set to Level.ALL to log this block

			String i = files[g].toString();
			LOGGER.info(i);
			String path = i;
			String base = dir.toString();
			//first we need to get the relative path - we do this by effectively masking
			String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
			LOGGER.info("relative filename is: " + relative);
			//we can't substring backwards in java and we can only rely on getting numbers from the filetype backwards (in case filename has a number in).
			String reverse = new StringBuilder(relative).reverse().toString(); //so we reverse
			LOGGER.info("reversed relative filename is: " + reverse);
			int j = 0;
			while (!(reverse.charAt(j) == '.')) { j++; }	//then we go back through our reversed string till we find a period. That's not going to work on unix then TODO: maybe "if system=windows"
			LOGGER.info("The first digit is at position: " + j); 
			String filetypeReversed = reverse.substring(0, j + 1);
			//we digress here to save out the filetype
			LOGGER.info("Here's the reversed filetype: " + filetypeReversed);
			filetype = new StringBuilder(filetypeReversed).reverse().toString(); //we reverse the filetype
			LOGGER.info("Filetype is: " + filetype);
			//carry on with getting the number
			String filetypeRemoved = reverse.substring(j + 1);	//then we remove the characters up to the first digit
			LOGGER.info("Chars removed up to first digit is : " + filetypeRemoved);	
			int k = 0;
			while (Character.isDigit(filetypeRemoved.charAt(k))) { k++; }	//then we continue through the numbers left until we find the first character
			LOGGER.info("The digits continue up to position:" + k);
			//digress again to get the file prefix name ie: what's before the digit
			String filePrefixReverse = filetypeRemoved.substring(k);
			LOGGER.info("File Prefix in reverse is: " + filePrefixReverse);
			filePrefix = new StringBuilder(filePrefixReverse).reverse().toString();
			LOGGER.info("So here's the file prefix:" + filePrefix);
			String numberExtracted = filetypeRemoved.substring(0,k); //then we remove anything past the last number
			LOGGER.info(numberExtracted);	
			String reverseBack = new StringBuilder(numberExtracted).reverse().toString(); //then we've got the digits out. We need to reverse the number back again
			LOGGER.info(reverseBack);
			fileNumbers.add(reverseBack);				
		}
		//now let's use the comparator to sort the arrayList - we'll just write back to the same arraylist TODO: good idea?

		Collections.sort(fileNumbers, new NumberedFileComparator());
		for (int i = 0; i < fileNumbers.size(); i++) {
			LOGGER.info(fileNumbers.get(i));
		}


		//now we can reconstitute the file list IN ORDER

		for (int l = 0; l < fileNumbers.size(); l++) {
			fileNumbers.set(l, dir + "/" + filePrefix + fileNumbers.get(l) + filetype);
		}

		return fileNumbers;
	}



	//TODO: one of these I ended up not using either here or in theme
	@Override public String toString() { return itemName; }



}

