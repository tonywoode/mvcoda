package drawing;

//import gfxelement.GFXElement;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import xstream.GFXElement;

import lombok.Getter;

/**
 * deals with overlaying graphics over Buffered Images passed to it by the media package of MVODA
 * @author twoode
 *
 */
public class ImageCompositor {

	private int fileIndex;
	private ArrayList<String> gfxFiles;
	private GFXElement gfxElement;

	long vidTimeStamp = 0;
	long inTime = 0;
	long desiredDuration = 0;
	long outTime = 0;

	long inTimeWithHandles = 0;
	long outTimeWithHandles = 0;

	BufferedImage videoFrame;
	BufferedImage overlay;

	int x = 0;
	int y = 0;
	double newWidth = 0;
	double newHeight = 0;

	@Getter boolean imOut = false;
	private float alpha = 0f;
	boolean fadeIt = false;


	int outOffset = 0;

	/**
	 * Takes a theme name and arranges to overlay the sequence of images set as logo
	 * @param gfxElement the element to be composited
	 */
	public ImageCompositor(GFXElement gfxElement) {
		this.gfxElement = gfxElement;
		String dir = gfxElement.getDirectory();
		gfxFiles = gfxElement.getOverlayFileNames(dir);
	}

	/**
	 * When passed the current video's timestamp, it's total duration, and the current videoframe, will return the composited image
	 * depending on what type of compositor you've created ie: what GFX Element this object is overlaying
	 * @param vidTimeStamp target video's timestamp
	 * @param inTime the GFX element's specified in time
	 * @param desiredDuration GFX element's specified duration
	 * @param videoFrame the image to overlay
	 * @return the composited image
	 * @throws IOException
	 */
	public BufferedImage overlayNextImage(long vidTimeStamp, long inTime, long desiredDuration, BufferedImage videoFrame) throws IOException {
		x = gfxElement.getXOffsetSD();
		y = gfxElement.getYOffsetSD();
		this.vidTimeStamp = vidTimeStamp;
		this.inTime = inTime;
		inTimeWithHandles = inTime - gfxElement.getInDuration();
		this.desiredDuration = desiredDuration;
		this.videoFrame = videoFrame;
		outTime = inTime + desiredDuration;		
		outTimeWithHandles = outTime + gfxElement.getOutDuration();


		/*System.out.println(gfxElement.getDirectory() + " inDuration is " + gfxElement.getInDuration());
		System.out.println("inDuration is :" + gfxElement.getInDuration());
		System.out.println("outDuration is :" + gfxElement.getOutDuration());
		System.out.println(" in time with handles is " + inTimeWithHandles);
		System.out.println(" duration is " + desiredDuration);
		System.out.println(" out time with handles is " + outTimeWithHandles);
		System.out.println("duration of element is " + gfxElement.getDuration(25));
		System.out.println("Reverse for logo is " + gfxElement.isReverse());*/


		//if its not the time for this element to come in or if its already gone out, or if the duration is not long enough even for the handle, do nothing
		if (vidTimeStamp >= inTimeWithHandles && vidTimeStamp <= outTimeWithHandles){// && desiredDuration > gfxElement.getInDuration()) {  //TODO: why did I put this here
			nextFileUNC();
			String overlayFile = gfxFiles.get(fileIndex);
			overlay = ImageIO.read(new File(overlayFile));
			if (fadeIt) {
				//BufferedImage composite = fadeImage();
				BufferedImage composite = wipeImage();
				return composite;
			}
			else {
				BufferedImage composite = overlayImage();
				return composite;
			}
		}
		else { imOut = true; return videoFrame; }

	}

	public int timeCodeToFrameIndexConverter(long timeStamp) {
		int frame = (int) (timeStamp * 25 - 1); // / 1000) - 1; //minus one because we are changing to the Array's INDEX number		
		return frame;
	}
	//TODO: WHEREVER I CHANGE A FRAME NUMBER INTO A COMMAND TO GO ONSCREEN I NEED TO SUBTRACT ONE TO GET ITS INDEX NUMBER IN THE ARRAY!!!
	//TODO: need a frame converter as well as a frame index converter - another constanty thing somewhere.....and the above should go there too

	/**
	 * Computes what GFX to overlay over the current videoframe by passing in the times and desired times. From these we alter the index of the current position
	 * in the GFX Element's sequence (a field in this class)
	 * @param vidTimeStamp current position in video
	 * @param inTime the time specified for the element to animate on
	 * @param desiredDuration the total duration the element should last for
	 */
	public void nextFileUNC() {
		imOut = true;

		if (gfxFiles.size() == 1) { fadeIt = true; return; } //if just a static image rather than a sequence, return it, don't do the below

		/*if we start with not enough handle time, find the correct animate point to come in:
		  we CHECK by seeing if the time you've said to come-in is less time than the in-handle, we ACTION
		  if the current time is ALSO less than the in-handle, we EXECUTE by setting the Array Index of the 
		  gfx element to the number of frames we need to miss*/

		//TODO - not sure about the middle check for timestamp less than induration. Induration will always be less than zero if we have this problem and fileindex = 0 should pick up beginning anyway
		if (inTime < gfxElement.getInDuration() && vidTimeStamp < gfxElement.getInDuration() && fileIndex == 0) { 
			fileIndex = timeCodeToFrameIndexConverter(gfxElement.getInDuration() - inTime); } 

		if (gfxElement.isReverse() == true) { nextFileUNCForReverseOut();} //if its a reverse out, go to that method
		if (gfxElement.isLoop() == true) { nextFileUNCForLoop();}

		else if (fileIndex < gfxFiles.size() -1 ) { //if we aren't at the last element frame
			if (fileIndex < gfxElement.getFirstHoldFrame() ) { //and if we aren't at the half-way point of the element
				fileIndex++; //animate
			} //also if we are at the end of the specified duration
			else if (vidTimeStamp >= outTime ) { //note not with handles - we want to START the out animation.....
				fileIndex = (gfxElement.getLastHoldFrame() + 1) + outOffset; //we are now in the out sequence so we jump to first out frame and then increment that
				outOffset++;
				//fileIndex++; //animate
				imOut = true;
			}

		}
		if (vidTimeStamp > inTime && vidTimeStamp < outTime  ) { imOut = false; } //note no handles so text comes in and out only while we are on hold
		//else we are before, after, or at the animation hold point, so don't animate...
	}


	public void nextFileUNCForReverseOut() {
		if (fileIndex > 0 && vidTimeStamp >= outTime) { //otherwise so long as we are above the sequence start frame, and past the out time
			if (fileIndex - gfxElement.getSpeed() >= 0) { fileIndex = fileIndex - gfxElement.getSpeed(); }//iterate backwards through the animation at the specified time factor
			//if (fileIndex > 0 && fileIndex < outSpeedUp) {fileIndex = 0; } //but we need the animation to end on blank frame zero irrespective of outSpeedUp factor
		}
		else if (fileIndex < gfxFiles.size() -1 && fileIndex < gfxElement.getFirstHoldFrame() ) { // && vidTimeStamp < outTimeWithHandles && vidTimeStamp >= inTimeWithHandles) { //if we arent at the last element frame or the outTime, but we are past the intime,
			fileIndex++;} //animate

	}

	public void nextFileUNCForLoop() {
		if (vidTimeStamp > inTimeWithHandles ) {
			if (fileIndex == gfxElement.getLastHoldFrame() ) {
				fileIndex = gfxElement.getFirstHoldFrame() -1;
			}
		}
		if (fileIndex < gfxFiles.size() -1 ) { //if we aren't at the last element frame
			fileIndex++; //animate
		 //also if we are at the end of the specified duration
		if (vidTimeStamp >= outTime ) { //note not with handles - we want to START the out animation.....
			fileIndex = (gfxElement.getLastHoldFrame() + 1) + outOffset; //we are now in the out sequence so we jump to first out frame and then increment that
			outOffset++;
			//fileIndex++; //animate
			imOut = true;
		}
		}
	}
	
	
	public void resetFileUNC() {
		fileIndex = 0;
		outOffset = 0;
	}


	public BufferedImage overlayImage() throws IOException {
		//see history for some ways to make a new bufferedimage of correct type for xuggle and to output to file instead of passing back
		Graphics2D g = videoFrame.createGraphics();
		g.drawImage(overlay, x, y, null);
		return videoFrame;
	}


	public BufferedImage fadeImage() throws IOException {//http://www.java2s.com/Code/Java/2D-Graphics-GUI/Fadeoutanimageimagegraduallygetmoretransparentuntilitiscompletelyinvisible.htm

		float alphaFactor = 0.06f;
		Graphics2D g2d = videoFrame.createGraphics();

		if ( vidTimeStamp >= inTimeWithHandles && vidTimeStamp <= outTimeWithHandles) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2d.drawImage(overlay, x, y, null);
			if  (alpha < 1.00f - alphaFactor ) {	alpha += alphaFactor; }
		}
		else if ( vidTimeStamp >= outTimeWithHandles ) { //TODO: doesn't fade out completely does it, knew it wouldn't - and these algorithms are a bit silly....
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2d.drawImage(overlay, x, y, null);
			if ( alpha - alphaFactor >= 0.00f ) { alpha -= alphaFactor; }
			if ( alpha - alphaFactor < 0.0f ) { alpha = 0.0f; } //we really want it to fade off at the end
		}
		return videoFrame;
	}


	public BufferedImage wipeImage() throws IOException {

		if (vidTimeStamp > inTimeWithHandles && vidTimeStamp <= outTimeWithHandles ) {
			double fadeTime = 50;
			double width = overlay.getWidth();
			double height = overlay.getHeight();

			double widthInc = width / fadeTime;
			double heightInc = height / fadeTime;

			if (vidTimeStamp >= outTimeWithHandles - (fadeTime * 40000) && newWidth - widthInc >=0 && newHeight - heightInc >=0) { 
				//TODO: at 25fps to get one ms its 1000/25 = 40, so 25*40 gives us 1 second, need that framerate constant again
				//uh oh so in microseconds that's 1000000/25 = 
				newWidth = newWidth - widthInc;
				newHeight = newHeight - heightInc;//TODO: now be careful here this only works because we first hit the below increment code so w+h will be at max
			}

			else {
				if (newWidth + widthInc <= width) { newWidth = newWidth + widthInc; }
				if (newHeight + heightInc <= height) { newHeight = newHeight + heightInc; }
			}
			/*System.out.println("Width is: " + overlayImage.getWidth()  );System.out.println("Height is: " + overlayImage.getHeight() );System.out.println("WidthFactor is: "+ widthInc);
		System.out.println("HeightFactor is: "+ heightInc);System.out.println("New Width is currently: " + newWidth  );System.out.println("New Height is currently: " + newHeight  );*/
			overlay = overlay.getSubimage(0, 0, (int) Math.ceil(newWidth), (int) Math.ceil(newHeight) );//TODO: read again then delete: http://stackoverflow.com/questions/2386064/how-do-i-crop-an-image-in-java
			Graphics2D g = videoFrame.createGraphics();
			g.drawImage(overlay, x, y, null);
		}
		return videoFrame;
	}



}



