package drawing;

import gfxelement.GFXElement;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
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
	private float alpha = 0f;
	boolean fadeIt = false;
	double newWidth = 0;
	double newHeight = 0;


	/**
	 * Takes a theme name and arranges to overlay the sequence of images set as logo TODO: we can have a choice of logos probably now
	 * @param gfxElement the element to be composited
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
		BufferedImage composite = overlayNextImage(vidTimeStamp, inTime, desiredDuration, videoFrame, gfxElement.getXOffsetSD(), gfxElement.getYOffsetSD());
		return composite;
	}

	//TODO: you just made the splitting of these methods defunct as everything you CAN overlay DEFINTELY has x and y offsets in the class.....


	public BufferedImage overlayNextImage(long vidTimeStamp, long inTime, long desiredDuration, BufferedImage videoFrame, int x, int y) throws IOException {

		nextFileUNC(vidTimeStamp, inTime, desiredDuration);
		String overlayFile = gfxFiles.get(fileIndex);
		BufferedImage overlay = ImageIO.read(new File(overlayFile));
		if (fadeIt) {
			//BufferedImage composite = fadeImage(vidTimeStamp, inTime, desiredDuration, videoFrame, overlay, x, y);
			BufferedImage composite = wipeImage(vidTimeStamp, inTime, desiredDuration, videoFrame, overlay, x, y);
			return composite;
		}
		else {
			BufferedImage composite = overlayImage(videoFrame, overlay, x, y);
			return composite;
		}
	}



	/**
	 * Computes what GFX to overlay over the current videoframe by passing in the times and desired times. From these we alter the index of the current position
	 * in the GFX Element's sequence (a field in this class)
	 * @param vidTimeStamp current position in video
	 * @param inTime the time specified for the element to animate on
	 * @param desiredDuration the total duration the element should last for
	 */
	public void nextFileUNC(long vidTimeStamp, long inTime, long desiredDuration){ //just set duration to zero to play for natural length
		imOut = true;
		long outTime = inTime + desiredDuration;
		if (gfxFiles.size() == 1) { fadeIt = true; return; } //if theres just a static image rather than a sequence, return it, don't do the below
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
		if (vidTimeStamp > inTime + gfxElement.getInDuration()
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
		//int w = image.getWidth();
		//int h = image.getHeight();
		//BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

		// paint both images, preserving the alpha channels
		Graphics2D g = image.createGraphics();
		//g.drawImage(image, 0, 0, null);
		//if (fadeIt) { g.drawImage(fadeImage(image,overlayImage,x,y), 0,0, null); }
		//overlayImage = fadeImage(overlayImage,x ,y );
		g.drawImage(overlayImage, x, y, null);
		//ImageIO.write(combined, "PNG", new File(outputFile));
		return image;
	}



	public BufferedImage fadeImage(long vidTimeStamp, long inTime, long desiredDuration, BufferedImage image, BufferedImage overlayImage, int x, int y) throws IOException {
		//http://www.java2s.com/Code/Java/2D-Graphics-GUI/Fadeoutanimageimagegraduallygetmoretransparentuntilitiscompletelyinvisible.htm
		float alphaFactor = 0.06f;
		Graphics2D g2d = image.createGraphics();
		long outTime = inTime + desiredDuration;
		//alphaFactor = desiredDuration /   YES THIS IS THE WAY TO DO IT - ITS ZERO TO ONE: WORK OUT HOW LONG IT TAKES FOR EACH PERCENTAGE

		if ( vidTimeStamp >= inTime && vidTimeStamp <= outTime) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2d.drawImage(overlayImage, x, y, null);
			if  (alpha < 1.00f - alphaFactor ) {	alpha += alphaFactor; }
		}
		else if ( vidTimeStamp >= outTime ) { //TODO: doesn't fade out completely does it, knew it wouldn't - and these algorithms are a bit silly....
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2d.drawImage(overlayImage, x, y, null);
			if ( alpha - alphaFactor >= 0.00f ) { alpha -= alphaFactor; }
			if ( alpha - alphaFactor < 0.0f ) { alpha = 0.0f; } //we really want it to fade off at the end
		}
			return image;
		}
	
	
	public BufferedImage wipeImage(long vidTimeStamp, long inTime, long desiredDuration,BufferedImage image, BufferedImage overlayImage, int x, int y) throws IOException {
		
		long outTime = inTime + desiredDuration;
		if (vidTimeStamp > inTime && vidTimeStamp <= outTime ) {
		double fadeTime = 50;
		double width = overlayImage.getWidth();
		double height = overlayImage.getHeight();
			
		double widthInc = width / fadeTime;
		double heightInc = height / fadeTime;
		
		if (vidTimeStamp >= outTime - (fadeTime * 40) && newWidth - widthInc >=0 && newHeight - heightInc >=0) { 
		//TODO: at 25fps to get one ms its 1000/25 = 40, so 25*40 gives us 1 second, need that framerate constant again
		newWidth = newWidth - widthInc;
		newHeight = newHeight - heightInc;//TODO: now be careful here this only works because we first hit the below increment code so w+h will be at max
		}
			
		else {
		if (newWidth + widthInc <= width) { newWidth = newWidth + widthInc; }
		if (newHeight + heightInc <= height) { newHeight = newHeight + heightInc; }
		}
		
		System.out.println("Width is: " + overlayImage.getWidth()  );
		System.out.println("Height is: " + overlayImage.getHeight() );
		System.out.println("WidthFactor is: "+ widthInc);
		System.out.println("HeightFactor is: "+ heightInc);
		System.out.println("New Width is currently: " + newWidth  );
		System.out.println("New Height is currently: " + newHeight  );
		
		
		overlayImage = overlayImage.getSubimage(0, 0, (int) Math.ceil(newWidth), (int) Math.ceil(newHeight) );//TODO: read again then delete: http://stackoverflow.com/questions/2386064/how-do-i-crop-an-image-in-java
		Graphics2D g = image.createGraphics();
		
		
		g.drawImage(overlayImage, x, y, null);
		}
		return image;
	}
	


	}



