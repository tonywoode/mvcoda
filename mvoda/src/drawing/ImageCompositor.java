package drawing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import theme.Theme;

/**
 * deals with overlaying graphics over Buffered Images passed to it by the media package of MVODA
 * @author twoode
 *
 */
public class ImageCompositor {
	
	private BufferedImage image;
	private BufferedImage overlayImage;
	private BufferedImage composite;
	private Theme theme;

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
  public ImageCompositor(Theme theme, BufferedImage overlayImage) {
	  this.theme = theme;
	  this.overlayImage = overlayImage;
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
