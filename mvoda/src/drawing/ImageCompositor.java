package drawing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import lombok.Setter;
import media.Decoder;
import media.MusicVideo;

/**
 * deals with overlaying graphics over Buffered Images passed to it by the media package of MVODA
 * @author twoode
 *
 */
public class ImageCompositor {

	@Setter private BufferedImage image;
	@Setter private BufferedImage overlayImage;
	private BufferedImage composite;
	//private Theme theme;
	//private String dir;

	private String filetype = "";
	private String filePrefix = "";
	private int fileIndex;
	private ArrayList<String> logoFiles;


	/**
	 * WE take the 2 image files
	 * @param image intended to be an image from a video stream
	 * @param overlayImage intended to be an overlay image, like a logo
	 * @throws IOException 
	 */
	/*public ImageCompositor(String backgroundFile, String overlayFile, String outputFile) throws IOException {

	  Image back = ImageIO.read(new File(backgroundFile));
      image = (BufferedImage) back;

      Image over = ImageIO.read(new File(overlayFile));
      overlay = (BufferedImage) over;

      this.outputFile = outputFile;
  }*/

	/**
	 *  We are going to take in 2 image filenames, load them, overlay, and save the result for now as a file
	 * @param image
	 * @param overlayImage
	 * @throws IOException
	 */
	public ImageCompositor(BufferedImage image, BufferedImage overlayImage) throws IOException {

		this.image = image;
		this.overlayImage = overlayImage;

	}

	/**
	 * Takes a theme name and arranges to overlay the sequence of images set as logo TODO: we can have a choice of logos probably now
	 * @param theme
	 * @param overlayImage
	 */
	public ImageCompositor(String dir) {
		//this.dir = dir;
		//this.image = image;
		logoFiles = getOverlayFileNames(dir); //do this now once only to get the arraylist of that directory's filenames
	}

	public String nextFileUNC(Decoder decoder, MusicVideo video) {
		String thisImageUNC = logoFiles.get(fileIndex);
		if (fileIndex < ( logoFiles.size() / 2) ) { //if we're not half way through return the next image
		//if ( decoder.getTimeStamp() <= ((video.getVidStreamDuration() / 25 * 1000) - 17000) ) {
		fileIndex++;
		return thisImageUNC;
		}
		else if ( decoder.getTimeStamp() >= ((video.getVidStreamDuration() / 25 * 1000) - 3000) ) { //that will give you 17000, my vid is 20 secs long
			if (fileIndex < logoFiles.size() -1 ) {
				System.out.println(fileIndex + "     " + logoFiles.size() + "     " );
				fileIndex++;
			}
			return thisImageUNC;
		}
		else return logoFiles.get(fileIndex); //else return the image that's half way through
		
	}

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


	public BufferedImage overlayImage() throws IOException {

		// create the new image, canvas size is the max. of both image sizes
		int w = Math.max(image.getWidth(), overlayImage.getWidth());
		int h = Math.max(image.getHeight(), overlayImage.getHeight());
		BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlayImage, 0, 0, null);

		// Save as new image
		//ImageIO.write(combined, "PNG", new File(outputFile));
		composite = combined;
		return composite;
	}




}
