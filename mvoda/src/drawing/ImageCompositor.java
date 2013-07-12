package drawing;

import gfxelement.GFXElement;
import gfxelement.numbers.Numbers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import lombok.Getter;

/**
 * deals with overlaying graphics over Buffered Images passed to it by the media package of MVODA
 * @author twoode
 *
 */
public class ImageCompositor {

	//@Setter private BufferedImage image; //TODO: should these really be setters and not passed parameters?
	//@Setter private BufferedImage overlayImage;
	//private BufferedImage composite;
	private int fileIndex;
	private ArrayList<String> gfxFiles;
	private GFXElement gfxElement;
	@Getter boolean imOut = false;


	/**
	 * Takes a theme name and arranges to overlay the sequence of images set as logo TODO: we can have a choice of logos probably now
	 * @param theme
	 * @param overlayImage
	 */
	public ImageCompositor(GFXElement gfxElement) {
		this.gfxElement = gfxElement;
		String dir = gfxElement.getDirectory();
		gfxFiles = gfxElement.getOverlayFileNames(dir);
	}

	/*public ImageCompositor(GFXElement gfxElement, int num) {
		this.gfxElement = gfxElement;
		Numbers number = (Numbers) gfxElement;
		number.setNum(num);
		number.setDirectory(num)
		String dir = number.getDirectory();
		gfxFiles = number.getOverlayFileNames(dir);
	}*/

	public void resetFileUNC() {
		fileIndex = 0;
	}

	/**
	 * When passed the current video's timestamp, it's total duration, and the current videoframe, will return the composited image
	 * depending on what type of compositor you've created ie: what GFX Element this object is overlaying
	 * @param vidTimeStamp target video's timestamp
	 * @param vidDuration targer video's total duration
	 * @param videoFrame the image to overlay
	 * @return the composited image
	 * @throws IOException
	 */
	public BufferedImage overlayNextImage(long vidTimeStamp, long inTime, long desiredDuration, BufferedImage videoFrame) throws IOException {
		BufferedImage composite = overlayNextImageAtCoord(vidTimeStamp, inTime, desiredDuration, videoFrame, 0, 0);
		return composite;
	}




	public BufferedImage overlayNextImageAtCoord(long vidTimeStamp, long inTime, long desiredDuration, BufferedImage videoFrame, int x, int y) throws IOException {
		//String overlayFile = nextFileUNC(vidTimeStamp,vidDuration);
		//int index = nextFileUNC(vidTimeStamp,inTime,desiredDuration);
		nextFileUNC(vidTimeStamp, inTime, desiredDuration);
		String overlayFile = gfxFiles.get(fileIndex);

		//String overlayFile = nextFileUNC(vidTimeStamp,inTime,desiredDuration);


		BufferedImage overlay = ImageIO.read(new File(overlayFile));
		BufferedImage composite = overlayImage(videoFrame, overlay, x, y);
		return composite;
	}



	/**
	 * Computes what GFX to overlay over the current videoframe by passing in the current and total video timestamps
	 * @param vidTimeStamp current position in video
	 * @param vidDuration total duration of video
	 * @return the full UNC path of the next image to overlay
	 */
	/*	public String OLDnextFileUNC(long vidTimeStamp, long vidDuration) {
		String thisImageUNC = gfxFiles.get(fileIndex);

		long inTime = vidTimeStamp + 4000;
		long outTime = vidDuration - 6000;
		long holdTime = 12000;


		if ( inTime <= gfxElement.getInDuration()) {
			fileIndex++;
			return thisImageUNC;
		}
		else if ( outTime >= vidTimeStamp - gfxElement.getOutDuration()) {
			if (fileIndex < gfxFiles.size() -1 ) {
				System.out.println(thisImageUNC + " At gfx file no.: " + fileIndex + "     " + "Out of Total Files: " + gfxFiles.size() );
				fileIndex++;
			}
			return thisImageUNC;
		}
		else return gfxFiles.get(fileIndex); //else return the image that's half way through
	}

	 */


	public void nextFileUNC(long vidTimeStamp, long inTime, long desiredDuration){ //just set duration to zero to play for natural length
		imOut = true;
		long outTime = inTime + desiredDuration;
		if (gfxFiles.size() == 1) { return; } //if theres just a static image rather than a sequence, return it, don't do the below
		else if (gfxElement.getOutDuration() <= 0) { nextFileUNCForReverseOut(vidTimeStamp, inTime, outTime);} //if its a reverse out, go to that method
		else if (fileIndex < gfxFiles.size() -1 ) { //if we aren't at the last element frame
			if (vidTimeStamp >= inTime) { //and if we are at the specified in time
				if (fileIndex < gfxElement.getLastInFrame() ) { //and if we aren't at the half-way point of the element
					fileIndex++; //animate
					// imOut = false; //TODO: Why DON'T I need this here?!?!
				} //also if we are at the end of the specified duration
				else if (vidTimeStamp >= outTime) {
					/*TODO: to animate the logo out we'd need this: if (vidTimeStamp >= inTime + desiredDuration - gfxElement.getOutDuration() ) {
					 * but I can't put that in because then there'd be no possibility of ever holding the logo through videos...
					 * there needs to be some check if an element NEEDS to fade out before a video ends that it can
					 * and then we can use that method to ACTUALLY fade the logo out at the end video as well as ticking off the user				
					 */
					fileIndex++; //animate
					imOut = true;
				}

			}

		}//TODO: here's the imout fire - can you make it look nicer
		if (vidTimeStamp > inTime + gfxElement.getInDuration() + 500
				&& vidTimeStamp < outTime + gfxElement.getOutDuration() + 1000 ) { 
			imOut = false; }
		//return; 				//else we are before, after, or at the animation hold point, so don't animate...
	}



	public void nextFileUNCForReverseOut(long vidTimeStamp, long inTime, long outTime) {
		int outSpeedUp = 2; //factor by which we speed up the out. This is a common trick for reverse-out animations
		if (fileIndex < gfxFiles.size() -1 && vidTimeStamp < outTime && vidTimeStamp >= inTime) { //if we arent at the last element frame or the outTime, but we are past the intime,
			fileIndex++;} //animate
		else if (fileIndex > 0 + outSpeedUp && vidTimeStamp >= outTime) { //otherwise so long as we are above the sequence start frame, and past the out time
			fileIndex = fileIndex - outSpeedUp; //iterate backwards through the animation at the specified time factor
			if (fileIndex > 0 && fileIndex < outSpeedUp) {fileIndex = 0; } //but we need the animation to end on blank frame zero irrespective of outSpeedUp factor
		}
		if (vidTimeStamp > inTime + gfxElement.getInDuration() + 500//TODO: repeating code!
				&& vidTimeStamp < outTime + (gfxElement.getInDuration() / 2 ) + 1000) { //nb: can't use out duration, use in divided by speedup factor
			imOut = false; }
		//return;
	}



	public BufferedImage overlayImage(BufferedImage image, BufferedImage overlayImage, int x, int y) throws IOException {

		// create the new image, canvas size is the max. of both image sizes
		int w = image.getWidth();
		int h = image.getHeight();

		BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlayImage, x, y, null);

		//ImageIO.write(combined, "PNG", new File(outputFile));

		return combined;
	}

}
