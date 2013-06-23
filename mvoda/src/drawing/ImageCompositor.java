package drawing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * We are going to take in 2 image filenames, load them, overlay, and save the result for now as a file
 * @author twoode
 *
 */
public class ImageCompositor {
	
	private BufferedImage image;
	private BufferedImage overlayImage;
	private BufferedImage composite;

	/**
	 * WE take the 2 image files
	 * @param BackgroundFile intended to be an image from a video stream
	 * @param OverlayFile intended to be an overlay image, like a logo
	 * @throws IOException 
	 */
  /*public ImageCompositor(String backgroundFile, String overlayFile, String outputFile) throws IOException {
		   
	  Image back = ImageIO.read(new File(backgroundFile));
      image = (BufferedImage) back;
      
      Image over = ImageIO.read(new File(overlayFile));
      overlay = (BufferedImage) over;
      
      this.outputFile = outputFile;
  }*/
  
  public ImageCompositor(BufferedImage image, BufferedImage overlayImage) throws IOException {
  
	  this.image = image;
	  this.overlayImage = overlayImage;
	  
  }
  
  public BufferedImage overlay() throws IOException {
  
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
