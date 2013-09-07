package test;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Just a test frame that displays an image passed to it
 * @author Tony
 *
 */
public class ShowImageInFrame extends JPanel {
   private BufferedImage image;
   
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
 * overrides size of frame with actual size of image
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
      if (image != null) {g.drawImage(image, 0, 0, null);}
   }

   /**
    * Dislpays the image
    * @throws IOException
    */
   public void createAndShowGui() throws IOException {
      JFrame frame = new JFrame("The image passed in is this");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(this);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

}