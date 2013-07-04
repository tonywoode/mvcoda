package drawing;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TextCompositor {

	private Font font;

	public TextCompositor(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontList = ge.getAvailableFontFamilyNames();
		//System.out.println(fontList[0]);
		font = new Font(fontList[0],1,25);
	}
	
	
	
	public BufferedImage overlayNextFontFrame(long vidTimeStamp, long vidDuration, BufferedImage videoFrame) throws IOException {
		nextText(videoFrame, vidTimeStamp, vidDuration);
		BufferedImage composite = nextText(videoFrame, vidTimeStamp, vidDuration );
		return composite;
	}


	private void renderText(BufferedImage image, String text, Font font, int x, int y){
		Graphics2D g = image.createGraphics();
		g.setFont(font);
		g.drawString(text, x, y);
		
		//g.dispose(); //TODO: investigate
	}


	public BufferedImage nextText(BufferedImage videoFrame, long vidTimeStamp, long vidDuration) {
		if ( vidTimeStamp >= 2000 && vidTimeStamp <= (vidDuration / 25 * 1000) - 3000) {//((video.getVidStreamDuration() / 25 * 1000) - 17000) ) {
			renderText(videoFrame, "Testing first write to an image", font, 450,450);
		}
		BufferedImage composite = videoFrame;
		return composite;
	}
}
