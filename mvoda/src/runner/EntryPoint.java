package runner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thoughtworks.xstream.XStream;

import playlist.Playlist;
import playlist.PlaylistEntry;

import media.Encoder;
import media.MusicVideo;
import media.xuggle.DecodeAndPlayAudioAndVideo;
import media.xuggle.EncoderXuggle;
import media.xuggle.EncoderXuggleMultipleFixedDecoders;
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


		/*//make a coupkle of music vid paths
		String fileUNC = "../../../MVODAInputs/Love/BrunoMarsJustTheWay.avi";
		String fileUNC2 = "../../../MVODAInputs/Love/FlorenceAndTheMachineLoverToLover.avi";
		String fileUNC3 = "../../../MVODAInputs/Love/GloriaEstefanAnythingForYou.avi";
		String fileUNC4 = "../../../MVODAInputs/Love/KateBushWutheringHeights.avi";
		String fileUNC5 = "../../../MVODAInputs/Love/LeonaLewisHappy.avi";
		String fileUNC6 = "../../../MVODAInputs/Love/Maroon5SheWillBeLoved.avi";
		String fileUNC7 = "../../../MVODAInputs/Love/NeyoStay.avi";
		String fileUNC8 = "../../../MVODAInputs/Love/NickiMinajYourLove.avi";
		String fileUNC9 = "../../../MVODAInputs/Love/PinkOneLastKiss.avi";
		String fileUNC10 = "../../../MVODAInputs/Love/RihannaYouDaOne.avi";*/

		//make a couple of music vid paths
		String fileUNC = "../../../MVODAInputs/BrunoShort.mp4";
		String fileUNC2 = "../../../MVODAInputs/FlorenceShort.mp4";
		String fileUNC3 = "../../../MVODAInputs/GloriaShort.mp4";
		String fileUNC4 = "../../../MVODAInputs/KateShort.mp4";
		String fileUNC5 = "../../../MVODAInputs/LeonaShort.mp4";
		String fileUNC6 = "../../../MVODAInputs/MaroonShort.mp4";
		String fileUNC7 = "../../../MVODAInputs/NeyoShort.mp4";
		String fileUNC8 = "../../../MVODAInputs/NickiShort.mp4";
		String fileUNC9 = "../../../MVODAInputs/PinkShort.mp4";
		String fileUNC10 = "../../../MVODAInputs/RihannaShort.mp4";

		//make vids out of them
		MusicVideo test = new MusicVideoXuggle(fileUNC);
		MusicVideo test2 = new MusicVideoXuggle(fileUNC2);
		MusicVideo test3 = new MusicVideoXuggle(fileUNC3);
		MusicVideo test4 = new MusicVideoXuggle(fileUNC4);
		MusicVideo test5 = new MusicVideoXuggle(fileUNC5);
		MusicVideo test6 = new MusicVideoXuggle(fileUNC6);
		MusicVideo test7 = new MusicVideoXuggle(fileUNC7);
		MusicVideo test8 = new MusicVideoXuggle(fileUNC8);
		MusicVideo test9 = new MusicVideoXuggle(fileUNC9);
		MusicVideo test10 = new MusicVideoXuggle(fileUNC10);


		//make a couple of playlist entries
		PlaylistEntry playlistEntry = new PlaylistEntry(test);
		PlaylistEntry playlistEntry2 = new PlaylistEntry(test2);
		PlaylistEntry playlistEntry3 = new PlaylistEntry(test3);
		PlaylistEntry playlistEntry4 = new PlaylistEntry(test4);
		PlaylistEntry playlistEntry5 = new PlaylistEntry(test5);
		PlaylistEntry playlistEntry6 = new PlaylistEntry(test6);
		PlaylistEntry playlistEntry7 = new PlaylistEntry(test7);
		PlaylistEntry playlistEntry8 = new PlaylistEntry(test8);
		PlaylistEntry playlistEntry9 = new PlaylistEntry(test9);
		PlaylistEntry playlistEntry10 = new PlaylistEntry(test10);


		//make a playlist
		Playlist playlist = new Playlist("Biggest Beats I've seen in a while");
		playlist.setNextEntry(playlistEntry);
		playlist.setNextEntry(playlistEntry2);
		playlist.setNextEntry(playlistEntry3);
		playlist.setNextEntry(playlistEntry4);
		playlist.setNextEntry(playlistEntry5);
		playlist.setNextEntry(playlistEntry6);
		playlist.setNextEntry(playlistEntry7);
		playlist.setNextEntry(playlistEntry8);
		playlist.setNextEntry(playlistEntry9);
		playlist.setNextEntry(playlistEntry10);


		//set an output file
		String outFileUNC = "E:/Output.mp4";


		//Urban.setNum(1); //TODO: very silly AND has to be done before instantiation...
		Theme theme = new Classic();


		//get Xuggler's video info - idea could Junit test compare MY music vid class to THIS info?
		System.out.println(test.toString());
		//draw onto video
		//Encoder draw = new EncoderXuggle(test, test2, theme, outFileUNC);
		//Encoder draw = new EncoderXuggle(playlist, theme, outFileUNC);
		Encoder draw = new EncoderXuggleMultipleFixedDecoders(playlist, theme, outFileUNC);
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
