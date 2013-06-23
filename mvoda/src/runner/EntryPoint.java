package runner;

import java.io.IOException;

import media.Encoder;
import media.EncoderXuggle;
import media.MusicVideo;
import media.VideoInfo;

public class EntryPoint {

	/**
	 * Main launcher for application, will do something nice
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//load a music vid
		String fileUNC = "../../../MVODAInputs/NeyoStayShort.avi";
		//set an output file
		String outFileUNC = "../../../MVODAOutputs/doesthiswork.avi";
		MusicVideo test = new MusicVideo(fileUNC);

		//get Xuggler's video info - idea could Junit test compare MY music vid class to THIS info?
		VideoInfo info = new VideoInfo(test.getFileUNC());
		
		//draw onto video
		Encoder draw = new EncoderXuggle(fileUNC, outFileUNC);
		
		//now let's try and grab a frame using the SIMPLE API
		//ModifyMusicVideo vid = new ModifyMusicVideo(fileUNC, outFileUNC );
		
		//play it in xugglers media player
		//DecodePlayVid player = new DecodePlayVid(fileUNC);

	}
}
