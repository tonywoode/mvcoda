package runner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import media.Encoder;
import media.MusicVideo;
import media.xuggle.DecodeAndPlayAudioAndVideo;
import media.xuggle.DecodePlayVid;
import media.xuggle.EncoderXuggle;
import media.xuggle.MusicVideoXuggle;
import theme.Pop;
import theme.Theme;

public class EntryPoint {

	/**
	 * Main launcher for application, launches GUI
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis(); //get rough start time
		Logger.getGlobal().setLevel(Level.OFF);//(Level.INFO); //using logger in some of the trickier sections
		//load a music vid
		String fileUNC = "../../../MVODAInputs/NeyoStayShort.avi";
		//set an output file
		String outFileUNC = "../../../MVODAOutputs/doesthiswork.avi";
		MusicVideo test = new MusicVideoXuggle(fileUNC);
		System.out.println("Container duration is " + test.getContainerDuration());
		System.out.println("Vid stream duration is " + (test.getVidStreamDuration() / 25) * 1000);
		System.out.println("Current timestamp is " + test.getDecoder().getTimeStamp());
		
		Theme pop = new Pop();

		//get Xuggler's video info - idea could Junit test compare MY music vid class to THIS info?
		System.out.println(test.toString());
		//draw onto video
		//Encoder draw = new EncoderXuggle(test, pop, outFileUNC);
		test.close();
		
		//report time taken
		long elapsed = System.currentTimeMillis() - start;
		DateFormat df = new SimpleDateFormat("mm 'mins,' ss 'seconds', SS 'millis'");
		df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		System.out.println("TIME TAKEN: " + df.format(new Date(elapsed)));
		
		//play it in xugglers media player
		DecodeAndPlayAudioAndVideo player = new DecodeAndPlayAudioAndVideo(outFileUNC);
		
		
		

	}
}
