package drawing;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;

/**
 * Arranages to composite standard text fonts onto BufferedImages for MV-CoDA
 * @author tony
 *
 */
public class TextCompositor {

	@Setter private Font textFont;
	@Getter @Setter static String fontName = "Ariel Narrow";
	@Getter @Setter static int fontSize = 24;
	@Setter private String text;
	private int textXPos;
	private int textYPos;

	public final static Logger LOGGER = Logger.getLogger(TextCompositor.class.getName()); //get a logger for this class


	public TextCompositor(String text, int textXPos, int textYPos){
		this.text = text;
		this.textXPos = textXPos;
		this.textYPos = textYPos;
	}

	/**
	 * For testing purposes we can get the fonts available on the local machine and choose one to use for the render
	 */
	public void getLocalFonts() {
		//see what fonts are available on local machine, for testing
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontList = ge.getAvailableFontFamilyNames();
		for (int i = 0; i < fontList.length; i++) {
			LOGGER.info("Available font list on this machine: At index no.: " + i + " is: " + fontList[i]);
			textFont = new Font(fontList[10],1,32);
		}
	}

	/**
	 * Checks to see if an overlay is required onto an image, and arranges overlay if so
	 * @param imOut does the current iteration require an overlay
	 * @param videoFrame the image to overlay onto
	 * @return the composited image
	 * @throws IOException //TODO: exception
	 */
	public BufferedImage overlayNextFontFrame(boolean imOut, BufferedImage videoFrame)  throws IOException {
		BufferedImage composite = nextText(imOut, videoFrame );
		return composite;
	}

	/**
	 * controls the composition of the text overlay
	 * @param imOut does the current interation require an overlay
	 * @param videoFrame image to overlay onto
	 * @return the composited image
	 */
	private BufferedImage nextText(boolean imOut, BufferedImage videoFrame) {
		textFont = new Font(fontName, 1, fontSize);
		if ( imOut == false) { if (text != null) { renderText(videoFrame, text, textFont, textXPos, textYPos); } }
		BufferedImage composite = videoFrame;
		return composite;
	}
	
	/**
	 * Renders text to image
	 * @param image image to write onto
	 * @param text characters to print
	 * @param font font to print in
	 * @param x at x coord in image
	 * @param y at y coord in image
	 */
	private void renderText(BufferedImage image, String text, Font font, int x, int y){
		Graphics2D g = image.createGraphics();
		g.setFont(font);
		g.drawString(text, x, y);
	}


	//TODO: refactor with coord
	public void setTextPos(int x, int y) {
		textXPos = x;
		textYPos = y;
	}


}
