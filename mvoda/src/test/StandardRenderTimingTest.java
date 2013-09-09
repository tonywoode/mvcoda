package test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import media.Encoder;
import media.MusicVideo;
import media.xuggle.DecodeAndPlayAudioAndVideo;
import media.xuggle.EncoderXuggle;
import media.xuggle.MusicVideoXuggle;
import playlist.Playlist;
import playlist.PlaylistEntry;
import runner.HandleLoggers;
import themes.Theme;
import themes.XMLReader;
import themes.XMLSerialisable;
import view.MediaOpenException;

public class StandardRenderTimingTest {

	/**
	 * Tests time taken to compose standard render
	 * @param args
	 * @throws IOException 
	 * @throws MediaOpenException 
	 */
	public static void main(String[] args) throws IOException, MediaOpenException {

		HandleLoggers.allLoggers();
		long start = System.currentTimeMillis(); //get rough start time

		/*//Full version paths
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

		//make music vid paths
		List<String> strings = asList(
		 "../../../MVODAInputs/BrunoShort.mp4",
		"../../../MVODAInputs/FlorenceShort.mp4",
		"../../../MVODAInputs/GloriaShort.mp4",
		 "../../../MVODAInputs/KateShort.mp4",
		 "../../../MVODAInputs/LeonaShort.mp4",
		 "../../../MVODAInputs/MaroonShort.mp4",
		 "../../../MVODAInputs/NeyoShort.mp4",
		"../../../MVODAInputs/NickiShort.mp4",
		"../../../MVODAInputs/PinkShort.mp4",
		"../../../MVODAInputs/RihannaShort.mp4"
		);

		//make vids out of paths
		ArrayList<MusicVideo> vids = new ArrayList<>();
		for (int i = 0; i < strings.size(); i++) {	
			MusicVideo video = new MusicVideoXuggle(strings.get(i) );
			 vids.add(video);
		}

		//make playlist entries
		ArrayList<PlaylistEntry> entries = new ArrayList<>();
		for (int j = 0; j < vids.size(); j++) {
			 entries.add( new PlaylistEntry(vids.get(j) ) );
		}

		//make a playlist
		Playlist playlist = new Playlist("Biggest Beats I've seen in a while");
		for (PlaylistEntry entry : entries) { playlist.setNextEntry(entry); }
		
				//set an output file
		String outFileUNC = "E:/Output.mp4";

		//Pop.setNum(1); //TODO: very silly AND has to be done before instantiation...
		
		String themeName = "Classic";
		Path rootDir = Paths.get("Theme");
		Path themeDir = Paths.get(rootDir.toString(),themeName);	
		XMLSerialisable themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
		Theme theme = (Theme) themeAsSerialisable;
		Path properDir = Paths.get( Theme.getRootDir().toString(), theme.getItemName() );

		//get Xuggler's video info
		System.out.println(vids.get(0).toString());
		//draw onto video
		Encoder draw = new EncoderXuggle(playlist, theme, outFileUNC);
		vids.get(0).close();

		//report time taken
		long elapsed = System.currentTimeMillis() - start;
		DateFormat df = new SimpleDateFormat("mm 'mins,' ss 'seconds', SS 'millis'");
		df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		System.out.println("TIME TAKEN: " + df.format(new Date(elapsed)));

		//play it in xugglers media player
		DecodeAndPlayAudioAndVideo player = new DecodeAndPlayAudioAndVideo(outFileUNC);

	}
}
