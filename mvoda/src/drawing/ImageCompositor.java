package drawing;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * We are going to take in 2 image filenames, load them, overlay, and save the result for now as a file
 * @author twoode
 *
 */
public class ImageCompositor {
	
	private BufferedImage image;
	private BufferedImage overlay;
	private String outputFile;

	/**
	 * WE take the 2 image files
	 * @param BackgroundFile
	 * @param OverlayFile
	 * @throws IOException 
	 */
  public ImageCompositor(String backgroundFile, String overlayFile, String outputFile) throws IOException {
		   
	  Image back = ImageIO.read(new File(backgroundFile));
      image = (BufferedImage) back;
      
      Image over = ImageIO.read(new File(overlayFile));
      overlay = (BufferedImage) over;
      
      this.outputFile = outputFile;
  }
  
  public ImageCompositor(BufferedImage image, BufferedImage overlay, String outputFile) throws IOException {
  
	  this.image = image;
	  this.overlay = overlay;
	  this.outputFile = outputFile;
  }
  
  
  
  public void overlay() throws IOException {
  
  		// create the new image, canvas size is the max. of both image sizes
		int w = Math.max(image.getWidth(), overlay.getWidth());
		int h = Math.max(image.getHeight(), overlay.getHeight());
		BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlay, 0, 0, null);

		// Save as new image
		ImageIO.write(combined, "PNG", new File(outputFile));
  
  }
  
}
