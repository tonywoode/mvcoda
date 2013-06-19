package drawing;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Just a test gui that displays an image passed to it
 * @author Tony
 *
 */
public class ShowImageInFrame extends JPanel {
   private static final int PREF_W = 400;
   private static final int PREF_H = PREF_W;
   private static final Stroke BASIC_STROKE = new BasicStroke(6f);
   BufferedImage image;
   
/**
 * Either we allow a BufferedImage to be displayed
 * @param image
 * @throws IOException
 */
   public ShowImageInFrame(BufferedImage image) throws IOException {
      
    this.image = image;
    createAndShowGui();
      
   }
   
   /**
    * Alternatively we allow a file path to be displayed
    * @param fileUNC
    * @throws IOException
    */
   public ShowImageInFrame(String fileUNC) throws IOException {
	   Image back = ImageIO.read(new File(fileUNC));
	      image = (BufferedImage) back;
	     createAndShowGui();
   }

/**
 * overrides size of frame with actul size of image
 */
   @Override
   public Dimension getPreferredSize() {
      return new Dimension(image.getWidth(), image.getHeight());
   }

   /**
    * overrides paintComponent to paint our image
    */
   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (image != null) {
         g.drawImage(image, 0, 0, null);
      }
   }

   /**
    * Dislpays the image
    * @throws IOException
    */
   public void createAndShowGui() throws IOException {
      JFrame frame = new JFrame("Your Image, Sire...");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(this);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

}
