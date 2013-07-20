package runner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import playlist.Playlist;
import playlist.PlaylistEntry;

import media.Encoder;
import media.MusicVideo;
import media.xuggle.DecodeAndPlayAudioAndVideo;
import media.xuggle.EncoderXuggle;
import media.xuggle.EncoderXuggleTWODECODERS;
import media.xuggle.MusicVideoXuggle;
import theme.Classic;
import theme.Pop;
import theme.Theme;
import theme.Urban;

public class EntryPoint {

	/**
	 * Main launcher for application, launches GUI
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		
		long start = System.currentTimeMillis(); //get rough start time
		Logger.getGlobal().setLevel(Level.OFF);//(Level.INFO); //using logger in some of the trickier sections
		
		
		//make a coupkle of music vid paths
		String fileUNC = "../../../MVODAInputs/NeyoStayShort.avi";
		String file2UNC = "../../../MVODAInputs/NickiMShort.avi";
		
		//make vids out of them
		MusicVideo test = new MusicVideoXuggle(fileUNC);
		MusicVideo test2 = new MusicVideoXuggle(file2UNC);
		
		//make a couple of playlist entries
		PlaylistEntry playlistEntry = new PlaylistEntry(test);
		PlaylistEntry playlistEntry2 = new PlaylistEntry(test2);
		
		//make a playlist
		Playlist playlist = new Playlist("Biggest Beats I've seen in a while");
		playlist.setNextEntry(playlistEntry);
		playlist.setNextEntry(playlistEntry2);
		
		//set an output file
		String outFileUNC = "../../../MVODAOutputs/doesthiswork.avi";
		
		
		
		
		//Urban.setNum(1); //TODO: very silly AND has to be done before instantiation...
		Theme theme = new Classic();
		

		//get Xuggler's video info - idea could Junit test compare MY music vid class to THIS info?
		System.out.println(test.toString());
		//draw onto video
		//Encoder draw = new EncoderXuggle(test, test2, theme, outFileUNC);
		//Encoder draw = new EncoderXuggle(playlist, theme, outFileUNC);
		Encoder draw = new EncoderXuggleTWODECODERS(playlist, theme, outFileUNC);
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
