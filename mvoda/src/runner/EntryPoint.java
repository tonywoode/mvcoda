package runner;

import java.io.IOException;

import media.Encoder;
import media.MusicVideo;
import media.xuggle.DecodePlayVid;
import media.xuggle.EncoderXuggle;
import media.xuggle.MusicVideoXuggle;

public class EntryPoint {

	/**
	 * Main launcher for application, launches GUI
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//load a music vid
		String fileUNC = "../../../MVODAInputs/NeyoStayShort.avi";
		//set an output file
		String outFileUNC = "../../../MVODAOutputs/doesthiswork.avi";
		MusicVideo test = new MusicVideoXuggle(fileUNC);

		//get Xuggler's video info - idea could Junit test compare MY music vid class to THIS info?
		System.out.println(test.toString());
		//draw onto video
		Encoder draw = new EncoderXuggle(test, outFileUNC);
		test.close();
		
		//now let's try and grab a frame using the SIMPLE API
		//ModifyMusicVideo vid = new ModifyMusicVideo(fileUNC, outFileUNC );
		
		//play it in xugglers media player
		DecodePlayVid player = new DecodePlayVid(outFileUNC);

	}
}
