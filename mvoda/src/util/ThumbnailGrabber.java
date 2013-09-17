package util;

import java.awt.image.BufferedImage;

/**
 * Interface to decouple the thumbfinder from the xuggler implementation.
 * @author tony
 *
 */
public interface ThumbnailGrabber {

	/**
	 * Seeks to a point in the video passed in as string UNC and passes back the image at that point. Intended for the GUI to display thumbnails
	 * @param fileUNC
	 * @return the thumbnail of an image from the music video file
	 */
	BufferedImage grabThumbs(String fileUNC); //TODO: should pass number of seconds in also
	

}