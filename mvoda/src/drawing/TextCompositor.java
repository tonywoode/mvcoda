package drawing;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TextCompositor {

	private Font trackArtistFont;
	private Font numberFont;

	public TextCompositor(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontList = ge.getAvailableFontFamilyNames();
		for (int i = 0; i < fontList.length; i++) {
		System.out.println("Available font list on this machine: At index no.: " + i + " is: " + fontList[i]);
		}
		trackArtistFont = new Font(fontList[11],1,32);
		numberFont = new Font(fontList[11],1,55);
	}
	
	
	
	public BufferedImage overlayNextFontFrame(long vidTimeStamp, long vidDuration, BufferedImage videoFrame) throws IOException {
		nextText(videoFrame, vidTimeStamp, vidDuration);
		BufferedImage composite = nextText(videoFrame, vidTimeStamp, vidDuration );
		return composite;
	}


	protected void renderText(BufferedImage image, String text, Font font, int x, int y){
		Graphics2D g = image.createGraphics();
		g.setFont(font);
		g.drawString(text, x, y);
		
		//g.dispose(); //TODO: investigate
	}


	public BufferedImage nextText(BufferedImage videoFrame, long vidTimeStamp, long vidDuration) {
		if ( vidTimeStamp >= 2000 && vidTimeStamp <= (vidDuration) - 2300) {//((video.getVidStreamDuration() / 25 * 1000) - 17000) ) {
			renderText(videoFrame, "5", numberFont, 285,490);
			renderText(videoFrame, "This is the track", trackArtistFont, 390,460);
			renderText(videoFrame, "This is the artist", trackArtistFont, 380,500);
		}
		BufferedImage composite = videoFrame;
		return composite;
	}
}
