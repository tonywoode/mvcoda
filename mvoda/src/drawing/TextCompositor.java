package drawing;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

import lombok.Setter;

public class TextCompositor {

	@Setter private Font trackArtistFont;
	private Font numberFont;
	@Setter private String text1;
	@Setter private String text2;
	private int text1XPos;
	private int text1YPos;
	private int text2XPos;
	private int text2YPos;
	@Setter private String number;

	public TextCompositor(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontList = ge.getAvailableFontFamilyNames();
		for (int i = 0; i < fontList.length; i++) {
		System.out.println("Available font list on this machine: At index no.: " + i + " is: " + fontList[i]);
		}
		trackArtistFont = new Font(fontList[10],1,32);
		numberFont = new Font(fontList[10],1,55);
	}
	
	
	
	public BufferedImage overlayNextFontFrame(boolean imOut, BufferedImage videoFrame)  throws IOException {
		nextText(imOut, videoFrame);
		BufferedImage composite = nextText(imOut, videoFrame );
		return composite;
	}


	protected void renderText(BufferedImage image, String text, Font font, int x, int y){
		Graphics2D g = image.createGraphics();
		g.setFont(font);
		g.drawString(text, x, y);
		
		//g.dispose(); //TODO: investigate
	}


	public BufferedImage nextText(boolean imOut, BufferedImage videoFrame) {
		if ( imOut == false) {
			if (number != null) {
			renderText(videoFrame, number, numberFont, 285,490);
			}
			if (text1 != null) {
			renderText(videoFrame, text1, trackArtistFont, text1XPos, text1YPos);
			}
			if (text2 != null) {
			renderText(videoFrame, text2, trackArtistFont, text2XPos, text2YPos);
			}
		}
		BufferedImage composite = videoFrame;
		return composite;
	}
	
	
	public void setText1Pos(int x, int y) {
		text1XPos = x;
		text1YPos = y;
	}
	
	public void setText2Pos(int x, int y) {
		text2XPos = x;
		text2YPos = y;
	}
	
	
}
