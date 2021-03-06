package drawing;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import lombok.Getter;
import themes.CoOrd;
import themes.GFXElement;
import themes.GFXElementException;
import util.FrameRate;

/**
 * deals with overlaying graphics over Buffered Images passed to it by the media package of MVODA
 * @author twoode
 *
 */
public class ImageCompositor {
	
	/**
	 * A logger for this class
	 */
	public final static Logger LOGGER = Logger.getLogger(ImageCompositor.class.getName());

	/**
	 * A boolean which will signify to text compositors that the GFX element being composited is currently at its hold position or not
	 */
	@Getter private boolean imOut = false;
	
	/**
	 * The position in the GFX element we are compositing that we are currently at
	 */
	private int fileIndex;
	
	/**
	 * The timestamp of the video codec
	 */
	private long vidTimeStamp = 0;
	
	/**
	 * the in time set for the GFX element we are compositing
	 */
	private long inTime = 0;
	
	/**
	 * The out time set for the GFX element we are compositing
	 */
	private long outTime = 0;
	
	/**
	 * The in time for the GFX element we are compositing if its in-animation is considered
	 */
	private long inTimeWithHandles = 0;
	
	/**
	 * The in time for the GFX element we are compositing if its in-animation is considered
	 */
	private long outTimeWithHandles = 0;
	
	/**
	 * The width after a wipe operation of the overlaid image
	 */
	private double newWidth = 0;
	
	/**
	 * The height after a wipe operation of the overlaid image
	 */
	private double newHeight = 0;
	
	/**
	 * The alpha bitmask set at a particular moment for a static image which is fading in
	 */
	private float alpha = 0f;
	
	/**
	 * An offset by which we count whether to increment an image sequence each time round a loop
	 */
	private int outOffset = 0;
	
	/**
	 * Indicator of whether an image should be faded at a timestamp
	 */
	private boolean fadeIt = false;
	
	/**
	 * The file sequence numbers of a GFX element so we can lookup each in turn from the hard drive
	 */
	private ArrayList<String> gfxFiles;
	
	/**
	 * simple value object for co-ordinate
	 */
	private CoOrd coOrd = new CoOrd(0,0);
	
	/**
	 * The GFX element that is to be composited
	 */
	private GFXElement gfxElement;
	
	/**
	 * The video frame that is to be composited over
	 */
	private BufferedImage videoFrame;
	
	/**
	 * The frame of a GFX element that is to be overlaid on a videoframe
	 */
	private BufferedImage overlay;


	/**
	 * Takes a theme name and arranges to overlay the sequence of images set as logo
	 * @param gfxElement the element to be composited
	 */
	public ImageCompositor(GFXElement gfxElement) {
		this.gfxElement = gfxElement;
		Path dir = Paths.get(gfxElement.getDirectory().toString());
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
	 * @throws GFXElementException a problem with accessing the GFX Element files
	 */
	public BufferedImage overlayNextImage(long vidTimeStamp, long inTime, long desiredDuration, BufferedImage videoFrame) throws GFXElementException {
		this.videoFrame = videoFrame;
		this.vidTimeStamp = vidTimeStamp;
		this.inTime = inTime;
		inTimeWithHandles = inTime - gfxElement.getInDuration();
		outTime = inTime + desiredDuration;		
		outTimeWithHandles = outTime + gfxElement.getOutDuration();
		coOrd.setXOffsetSD(gfxElement.getXOffsetSD() );
		coOrd.setYOffsetSD(gfxElement.getYOffsetSD() );

		LOGGER.info(gfxElement.getDirectory() + " inDuration is " + gfxElement.getInDuration());
		LOGGER.info("inDuration is :" + gfxElement.getInDuration());
		LOGGER.info("outDuration is :" + gfxElement.getOutDuration());
		LOGGER.info(" in time with handles is " + inTimeWithHandles);
		LOGGER.info(" duration is " + desiredDuration);
		LOGGER.info(" out time with handles is " + outTimeWithHandles);
		LOGGER.info("duration of element is " + gfxElement.getDuration() );
		LOGGER.info("Reverse for logo is " + gfxElement.isReverse());


		//if its not the time for this element to come in or if its already gone out, or if the duration is not long enough even for the handle, do nothing
		if (vidTimeStamp >= inTimeWithHandles && vidTimeStamp <= outTimeWithHandles) {
			nextFileUNC();
			String overlayFile = gfxFiles.get(fileIndex);
			try {
				overlay = ImageIO.read(new File(overlayFile));
				if (fadeIt) {
					BufferedImage composite = wipeImage();
					return composite;
				}
				else {
					BufferedImage composite = overlayImage();
					return composite;
				}
			} catch (IOException e) { throw new GFXElementException("Problem with opening the Theme Element files"); }
		}
		else { imOut = true; return videoFrame; }
	}


	/**
	 * Computes what GFX to overlay over the current videoframe by passing in the times and desired times. From these we alter the index of the current position
	 * in the GFX Element's sequence (a field in this class)
	 */
	public void nextFileUNC() {
		imOut = true;
		if (gfxFiles.size() == 1) { fadeIt = true; return; } //if just a static image rather than a sequence, return it, don't do the below

		/*if we start with not enough handle time, find the correct animate point to come in:
		  we CHECK by seeing if the time you've said to come-in is less time than the in-handle, we ACTION
		  if the current time is ALSO less than the in-handle, we EXECUTE by setting the Array Index of the 
		  gfx element to the number of frames we need to miss*/
		if (vidTimeStamp > inTime && vidTimeStamp < outTime  ) { imOut = false; } //note no handles so text comes in and out only while we are on hold
		if (inTime < gfxElement.getInDuration() && vidTimeStamp < gfxElement.getInDuration() && fileIndex == 0) { 
			fileIndex = FrameRate.timeCodeToFrameIndexConverter(gfxElement.getInDuration() - inTime); } 

		if (gfxElement.isReverse() == true) { nextFileUNCForReverseOut(); return; } //if its a reverse out, go to that method
		if (gfxElement.isLoop() == true) { nextFileUNCForLoop(); }

		else if (fileIndex < gfxFiles.size() -1 ) { //if we aren't at the last element frame
			if (fileIndex < gfxElement.getFirstHoldFrame() ) { //and if we aren't at the half-way point of the element
				fileIndex++; //animate
			} //also if we are at the end of the specified duration
			else if (vidTimeStamp >= outTime ) { //note not with handles - we want to START the out animation.....
				fileIndex = (gfxElement.getLastHoldFrame() + 1) + outOffset; //we are now in the out sequence so we jump to first out frame and then increment that
				outOffset++;
				imOut = true;
			}
		}
		
		//else we are before, after, or at the animation hold point, so don't animate...
	}


	/**
	 * Iterator for GFXElements that have no out-animation. We reverse out. This gets called by nextFileUNC
	 */
	private void nextFileUNCForReverseOut() {
		if (fileIndex > 0 && vidTimeStamp >= outTime) { //otherwise so long as we are above the sequence start frame, and past the out time
			if (fileIndex - gfxElement.getSpeed() >= 0) { fileIndex = fileIndex - gfxElement.getSpeed(); }//iterate backwards through the animation at the specified time factor
		}
		else if (fileIndex < gfxFiles.size() -1 && fileIndex < gfxElement.getFirstHoldFrame() ) { fileIndex++; imOut = true; } //animate
	}

	/**
	 * Iterator for GFXElements that have a looping hold section. This gets called by nextFileUNC
	 */
	private void nextFileUNCForLoop() {
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
				imOut = true;
			}
		}
	}

	/**
	 * resets any counters used by this class when finished with a whole music video
	 */
	public void resetFileUNC() {
		fileIndex = 0;
		outOffset = 0;
	}

	/**
	 * Overlays an image on the overlay this class holds currently. Intended for alpha-channel images
	 * @return the composited image
	 */
	private BufferedImage overlayImage() {
		Graphics2D g = videoFrame.createGraphics();
		g.drawImage(overlay, coOrd.getXOffsetSD(), coOrd.getYOffsetSD(), null);
		//TODO - need a map of rendering hints: g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		return videoFrame;
	}


	/**
	 * Fades an image onto the overlay this class holds currently. Intended for static images eg: logos
	 * @return the composited image
	 * @throws GFXElementException 
	 */
	public BufferedImage fadeImage() throws GFXElementException {//http://www.java2s.com/Code/Java/2D-Graphics-GUI/Fadeoutanimageimagegraduallygetmoretransparentuntilitiscompletelyinvisible.htm

		float alphaFactor = 0.06f;
		Graphics2D g2d;
		if (videoFrame != null) { g2d = videoFrame.createGraphics(); }
		else { throw new GFXElementException("Couldn't access the overlay image while doing a fade"); }

		if ( vidTimeStamp >= inTimeWithHandles && vidTimeStamp <= outTimeWithHandles) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2d.drawImage(overlay, coOrd.getXOffsetSD(), coOrd.getYOffsetSD(), null);
			if  (alpha < 1.00f - alphaFactor ) {	alpha += alphaFactor; }
		}
		else if ( vidTimeStamp >= outTimeWithHandles ) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2d.drawImage(overlay, coOrd.getXOffsetSD(), coOrd.getYOffsetSD(), null);
			if ( alpha - alphaFactor >= 0.00f ) { alpha -= alphaFactor; }
			if ( alpha - alphaFactor < 0.0f ) { alpha = 0.0f; } //we really want it to fade off at the end
		}
		return videoFrame;
	}


	/**
	 * Wipes an image onto the overlay this class holds currently. Intended for static images eg: logos
	 * @return the composited image
	 * @throws GFXElementException if we can't acces the overlay
	 */
	public BufferedImage wipeImage() throws GFXElementException {

		if (vidTimeStamp > inTimeWithHandles && vidTimeStamp <= outTimeWithHandles ) {
			double fadeTime = 50; //TODO: hard coded
			double width = overlay.getWidth();
			double height = overlay.getHeight();

			double widthInc = width / fadeTime;
			double heightInc = height / fadeTime;
			//TODO: hard coded time - needs to be relative to framerate
			if ( vidTimeStamp >= outTimeWithHandles - (fadeTime * 40000) && newWidth - widthInc >=0 && newHeight - heightInc >= 0 ) { 
				newWidth = newWidth - widthInc;
				newHeight = newHeight - heightInc;//TODO: this only works because we first hit the below increment code so w+h will be at max
			}
			else {
				if (newWidth + widthInc <= width) { newWidth = newWidth + widthInc; }
				if (newHeight + heightInc <= height) { newHeight = newHeight + heightInc; }
			}
			LOGGER.info("Width is: " + width);
			LOGGER.info("Height is: " + height);
			LOGGER.info("WidthFactor is: "+ widthInc);
			LOGGER.info("HeightFactor is: "+ heightInc);
			LOGGER.info("New Width is currently: " + newWidth);
			LOGGER.info("New Height is currently: " + newHeight  );
			//we must celing the number as late as possible
			overlay = overlay.getSubimage(0, 0, (int) Math.ceil(newWidth), (int) Math.ceil(newHeight) );
			Graphics2D g2d;
			if (videoFrame != null) { g2d = videoFrame.createGraphics(); }
			else { throw new GFXElementException("Couldn't access the overlay image while doing a fade"); }
			g2d.drawImage(overlay, coOrd.getXOffsetSD(), coOrd.getYOffsetSD(), null);
		}
		return videoFrame;
	}

}



