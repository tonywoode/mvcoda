package view;

import java.nio.file.Path;
import java.nio.file.Paths;

import media.Encoder;
import media.MusicVideo;
import media.xuggle.EncoderXuggle;
import media.xuggle.MusicVideoXuggle;
import playlist.Playlist;
import playlist.PlaylistEntry;
import themes.Theme;
import themes.XMLReader;
import themes.XMLSerialisable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ViewController {

	public Button renderButton;
	
	@FXML void render(MouseEvent e) {
		//make a couple of music vid paths
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

				//Pop.setNum(1); //TODO: very silly AND has to be done before instantiation...
				
				String themeName = "Classic";
				Path rootDir = Paths.get("Theme");
				Path themeDir = Paths.get(rootDir.toString(),themeName);
				
				XMLSerialisable themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
				Theme theme = (Theme) themeAsSerialisable;
				
				Path properDir = Paths.get( Theme.getRootDir().toString(), theme.getItemName() );
				
				/*System.out.println("This is the dir: " + theme.getThemeDir());
				System.out.println("This is the root dir: " + Theme.getRootDir());
				System.out.println("and this is the logo: " + theme.getLogo());
				System.out.println("AND THE REAL PATH IS:" + properDir);*/

				//get Xuggler's video info - idea could Junit test compare MY music vid class to THIS info?
				System.out.println(test.toString());
				//draw onto video
				Encoder draw = new EncoderXuggle(playlist, theme, outFileUNC);
				test.close();
	}
	
}