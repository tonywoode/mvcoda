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
import javax.swing.SwingUtilities;

public class TestAlphaComposite extends JPanel {
   private static final int PREF_W = 400;
   private static final int PREF_H = PREF_W;
   BufferedImage backgroundImage;
   BufferedImage overlayImage;
   

   public TestAlphaComposite() throws IOException {
  
      
      Image back = ImageIO.read(new File("src/test/bru.png"));
      backgroundImage = (BufferedImage) back;
      
      Image over = ImageIO.read(new File("Theme/Pop/Logo/4M1BugFrames/4M68.png"));
      overlayImage = (BufferedImage) over;
      
   }


   @Override public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }

   @Override protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (backgroundImage != null) { g.drawImage(backgroundImage, 0, 0, null); }
      if (overlayImage != null) { g.drawImage(overlayImage, 0, 0, null); }
   }

   private static void createAndShowGui() throws IOException {
      JFrame frame = new JFrame("TestAlphaComposite");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(new TestAlphaComposite());
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            try { createAndShowGui(); } 
            catch (IOException e) { e.printStackTrace(); }
         }
      });
   }
}
