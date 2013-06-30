package drawing;

import gfxelement.GFXElement;
import gfxelement.logo.FourMusic1;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

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

	private int fileIndex;
	private ArrayList<String> logoFiles;



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

		GFXElement fourMusic1 = new FourMusic1(); //TODO: dependency
		logoFiles = fourMusic1.getOverlayFileNames(dir);
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
