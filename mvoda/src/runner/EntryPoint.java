package runner;

import java.awt.image.BufferedImage;
import java.io.IOException;

import drawing.ShowImageInFrame;

import media.MusicVideo;
import media.DrawOntoVideo;

public class EntryPoint {

	/**
	 * Main launcher for application, will do something nice
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		MusicVideo test = new MusicVideo("C:/Users/Tony/CODE/MVODAInputs/Love/RihannaYouDaOne.avi");
		System.out.println("Here is the height of your video: " + test.getHeight());
		System.out.println("Here is the duration of your video (ms): " + test.getDuration());
		//DrawOntoVideo draw = new DrawOntoVideo("C:/Users/Tony/CODE/MVODAInputs/Love/RihannaYouDaOne.avi");
	while (test.hasNextPacket()) {
		BufferedImage image = test.getVideoFrame();
		ShowImageInFrame gui = new ShowImageInFrame(image);
		gui.createAndShowGui();
		
		
	}

	}

}
