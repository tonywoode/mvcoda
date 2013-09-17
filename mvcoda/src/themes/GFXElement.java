package themes;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import playlist.Number;
import util.FrameRate;
import util.XMLReader;
import util.XMLSerialisable;
import util.XMLWriter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * GFXElement is the base class for all GFXElements in MV-Coda. It describes basic properties that both static (i.e.: just a single image) and animated elements share
 * This is mostly text data, and co-ordinates onscreen. The text data also includes "element name" which describes the element TYPE, which can be:
 * 1) a logo
 * 2) a strap for holding text
 * 3) a chart name
 * 4) numbers that show chart position
 * 5) a transition
 * 6) any other type for special use
 * the class is serialisable and so the details end up in the XML that is included with theme elements - see {@link XMLReader} {@link XMLWriter}
 * @author tony
 *
 */
@XStreamAlias("GFXElement") public class GFXElement implements XMLSerialisable {

	/**
	 * A logger for this class
	 */
	public final static Logger LOGGER = Logger.getLogger(GFXElement.class.getName());
	
	//annotations because we'd like to omit some of these fields from the XML, they are calculated fields only for the class to use
	@XStreamOmitField private String filetype = "";
	@XStreamOmitField private String filePrefix = "";
	@XStreamOmitField private long duration;

	/**
	 * The name of the theme this element belongs to
	 */
	@Getter @Setter private String themeName;
	
	/**
	 * The elements type ie: Strap, Logo
	 */
	@Getter @Setter private String itemName;
	
	/**
	 * The individual name given to the element
	 */
	@Getter @Setter private String elementName;
	
	/**
	 * The author of the element
	 */
	@Getter @Setter private String author;
	
	/**
	 * The version number of the element
	 */
	@Getter @Setter private String version;
	
	/**
	 * The coordinates the element needs to be offset by to appear where it is desired onscreen
	 */
	@Getter @Setter private CoOrd coOrd;

	/**
	 * The array of sequence numbers that represent the file order to lookup for this element's sequence on disk
	 */
	private ArrayList<String> fileNumbers;



	/**
	 * The basic constituents that describe a GFX element are
	 * @param themeName which Theme it belongs to
	 * @param itemName what name we wish to give this element
	 * @param elementName the type of element we have
	 * @param author
	 * @param version
	 * @param coOrd the onscreen adjustment that needs making for the element to fit a video picture
	 */
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

	//this is just the duration of the whole pice of media
	public long getDuration() { duration = FrameRate.convertFrameToTime( fileNumbers.size() );	return duration; }

	//we need these so that we can call an AnimatedGFXElement as a GFXElement
	public long getInDuration() { return 0;	}
	public long getOutDuration() { return 0; }

	//passer methods - an animatedGFXElement has none of these things
	public boolean isReverse() { return false; }
	public boolean isLoop() { return false; }
	public boolean isNumbers() { return false; }
	public int getSpeed() { return 1; } //1 is the default for a static GFX Element
	public int getFirstHoldFrame() { return -1; }
	public int getLastHoldFrame() { return -1; }
	public int getNumberOfFrames() { return 1; }


	/**
	 * When passed a path containing a GFXElement, we will make an ordered array of the file sequence of the element
	 * The process of so-doing also gives us the filetype.
	 * We allow for elements to have zero padding //TODO
	 * @param dir path to the file sequence
	 * @return ordered array of the file sequence according to the numbering system they were saved using ie: the number postfix on the filename
	 */
	public ArrayList<String> getOverlayFileNames(Path dir) {
		File file = new File(dir.toString());  
		File[] files = file.listFiles();
		fileNumbers = new ArrayList<>();

		for (int g = 0; g < files.length; g++) {
			String i = files[g].toString();
			LOGGER.info("Inspecting file" + i);
			String path = i;
			String base = dir.toString();
			//first we need to get the relative path - we do this by effectively masking
			String relative = new File(base).toURI().relativize(new File(path).toURI()).getPath();
			LOGGER.info("relative filename is: " + relative);
			//we can't substring backwards in java and we can only rely on getting numbers from the filetype backwards (in case filename has a number in).
			String reverse = new StringBuilder(relative).reverse().toString(); //so we reverse
			LOGGER.info("reversed relative filename is: " + reverse);
			int j = 0;
			while (!(reverse.charAt(j) == '.')) { j++; }	//then we go back through our reversed string till we find a period
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
		//now let's use the comparator to sort the arrayList
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

	/**
	 * Returns merely the itemName of the element ie: what type it is
	 */
	@Override public String toString() { return itemName; }



}

