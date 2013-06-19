package runner;

import java.awt.image.BufferedImage;
import java.io.IOException;

import drawing.ShowImageInFrame;

import media.DecodePlayVid;
import media.MusicVideo;
import media.DrawOntoVideo;
import media.VideoInfo;

public class EntryPoint {

	/**
	 * Main launcher for application, will do something nice
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//load a music vid
		String fileUNC = "../../../MVODAInputs/Love/BrunoMarsJustTheWay.avi";
		MusicVideo test = new MusicVideo(fileUNC);

		//get some properties
		System.out.println("Here is the height of your video: " + test.getHeight());
		System.out.println("Here is the duration of your video (ms): " + test.getDuration());
		//DrawOntoVideo draw = new DrawOntoVideo("C:/Users/Tony/CODE/MVODAInputs/Love/RihannaYouDaOne.avi");
		
		//get Xuggler's video info - idea could Junit test compare MY music vid class to THIS info?
		VideoInfo info = new VideoInfo(test.getFileUNC());
		
		//play it in xugglers media player
		DecodePlayVid player = new DecodePlayVid(fileUNC);

		
		//hmm that's not working well...
/*		while (test.hasNextPacket()) {
			BufferedImage image = test.getVideoFrame();
			if (image != null) {
				ShowImageInFrame gui = new ShowImageInFrame(image);
				//gui.createAndShowGui();
			}
		}	
*/	
		



	}
}
