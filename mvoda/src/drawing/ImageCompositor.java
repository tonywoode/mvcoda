package drawing;

import gfxelement.GFXElement;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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


	
	/**
	 * When passed the current video's timestamp, it's total duration, and the current videoframe, will return the composited image
	 * depending on what type of compositor you've created ie: what GFX Element this object is overlaying
	 * @param vidTimeStamp target video's timestamp
	 * @param vidDuration targer video's total duration
	 * @param videoFrame the image to overlay
	 * @return the composited image
	 * @throws IOException
	 */
	public BufferedImage overlayNextImage(long vidTimeStamp, long vidDuration, BufferedImage videoFrame) throws IOException {
		String overlayFile = nextFileUNC(vidTimeStamp,vidDuration);		
		BufferedImage overlay = ImageIO.read(new File(overlayFile));
		BufferedImage composite = overlayImage(videoFrame, overlay);
		return composite;
	}
	
	
	
	/**
	 * Computes what GFX to overlay over the current videoframe by passing in the current and total video timestamps
	 * @param vidTimeStamp current position in video
	 * @param vidDuration total duration of video
	 * @return the full UNC path of the next image to overlay
	 */
	public String nextFileUNC(long vidTimeStamp, long vidDuration) {
		String thisImageUNC = gfxFiles.get(fileIndex);
		//if (fileIndex < ( gfxFiles.size() / 2) ) { //if we're not half way through return the next image
			if ( vidTimeStamp <= gfxElement.getInTime()) {//((video.getVidStreamDuration() / 25 * 1000) - 17000) ) {
			fileIndex++;
			return thisImageUNC;
		}
		else if ( vidTimeStamp >= vidDuration - gfxElement.getOutTime()) {/// 25 * 1000) - 3000) ) { //that will give you 17000, my vid is 20 secs long
			if (fileIndex < gfxFiles.size() -1 ) {
				System.out.println(thisImageUNC + " At gfx file no.: " + fileIndex + "     " + "Out of Total Files: " + gfxFiles.size() );
				fileIndex++;
			}
			return thisImageUNC;
		}
		else return gfxFiles.get(fileIndex); //else return the image that's half way through

	}


	public BufferedImage overlayImage(BufferedImage image, BufferedImage overlayImage) throws IOException {

		// create the new image, canvas size is the max. of both image sizes
		int w = image.getWidth();
		int h = image.getHeight();

		BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlayImage, 0, 0, null);

		//ImageIO.write(combined, "PNG", new File(outputFile));
		
		return combined;
	}

	
	



}
