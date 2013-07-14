package drawing;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

import lombok.Setter;

public class TextCompositor {

	@Setter private Font textFont;
	@Setter private String text;
	private int textXPos;
	private int textYPos;
	

	public TextCompositor(String text, int textXPos, int textYPos){
		this.text = text;
		this.textXPos = textXPos;
		this.textYPos = textYPos;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontList = ge.getAvailableFontFamilyNames();
		for (int i = 0; i < fontList.length; i++) {
		System.out.println("Available font list on this machine: At index no.: " + i + " is: " + fontList[i]);
		}
		textFont = new Font(fontList[10],1,32);
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
			if (text != null) {
			renderText(videoFrame, text, textFont, textXPos, textYPos);
			}
			
		}
		BufferedImage composite = videoFrame;
		return composite;
	}
	
	
	public void setTextPos(int x, int y) {
		textXPos = x;
		textYPos = y;
	}
	
	
}
