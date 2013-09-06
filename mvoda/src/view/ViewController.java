package view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.hamcrest.core.IsNull;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import media.Encoder;
import media.xuggle.DecodeAndPlayAudioAndVideo;
import media.xuggle.EncoderXuggle;
import media.xuggle.MusicVideoXuggle;
import playlist.Playlist;
import playlist.PlaylistEntry;
import themes.Theme;
import themes.ThemeFinder;
import themes.ThemeFinderImpl;
import themes.XMLReader;
import themes.XMLSerialisable;
import themes.XMLWriter;
import view.buttons.Dialog;
import view.buttons.MoveButtons;

public class ViewController implements Initializable {

	//TODO: you've given these fx:id's but why? you don't do anything with the button but onClick...., so why do they need to be here
	//public Button loadPlaylistButton;
	public Button savePlaylistButton;

	@Getter @Setter ViewControllerListener viewListener;	
	@Getter @Setter Stage stage;

	public TextField trackTextField;
	public TextField artistTextField;

	
	@Getter @Setter public Playlist playlist = new Playlist("Biggest Beats I've seen in a while"); //TODO: playlist name
	@Getter @Setter private ObservableList<PlaylistEntry> playlistObservable = FXCollections.observableArrayList(playlist.getPlaylistEntries());
	public ArrayList<String> vidFiles = new ArrayList<>();

	@FXML @Getter @Setter ComboBox<String> themeSelectBox;
	@FXML @Getter @Setter ListView<PlaylistEntry> playlistView;

	//private MoveButtons moveButtons;

	//private ObservableBooleanValue emptyList = new SimpleBooleanProperty(playlistObservable.isEmpty());
	//private Desktop desktop = Desktop.getDesktop();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		/*playlistObservable.addListener(new InvalidationListener() {	 
	 		@Override public void invalidated(Observable o) {	System.out.println("The binding is now invalid."); 
	 		if (!playlistObservable.isEmpty()) { playlistView.setDisable(true); }
	 		}
	    });*/
		//Boolean isEmpty = playlistObservable.isEmpty();
		playlistView.setDisable(true);
		savePlaylistButton.disableProperty().bind(playlistView.disabledProperty());
		//savePlaylistButton.disableProperty().bind(emptyList);
		//playlistObservable.addListener());
		//emptyList.addListener()
		/*SimpleBooleanProperty tell = playlistView.getSelectionModel().isEmpty();		
		savePlaylistButton.disabledProperty().bind(
				playlistObservable.addListener(*/


		ThemeFinder themeFinder = new ThemeFinderImpl(); //we must instantiate the themeFinder because it implements an interface
		ArrayList<Theme> themes = new ArrayList<>();
		try { themes = themeFinder.returnThemes(); } 
		catch (IOException e) {e.printStackTrace();}  // TODO exception handling 	
		catch (InterruptedException e) { e.printStackTrace(); } // TODO exception handling
		ObservableList<String> themename = FXCollections.observableArrayList(new ArrayList<String>());
		themename.clear();
		for (Theme element : themes) {
			String name = element.getItemName();
			themename.add(name);
		}
		//themeSelectBox.setAll(themename);
		System.out.println(themename);
		themeSelectBox.setItems(themename);
		//moveButtons = new MoveButtons(playlistView, playlistObservable); //instantiate the move buttons (we need to pass them a playlist)
		playlistView.setCellFactory(new Callback<ListView<PlaylistEntry>, ListCell<PlaylistEntry>>() {
			@Override public ListCell<PlaylistEntry> call(ListView<PlaylistEntry> list) {
				return new PlaylistEntryListCell();
			}
		});

		playlistView.getSelectionModel().selectedItemProperty().isNull().addListener(new ChangeListener<Boolean>() {
			@Override public void changed( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
				System.out.println("it's null has changed");
				playlistView.setDisable(true);	
			}
		});

		playlistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PlaylistEntry>() {
			public void changed( final ObservableValue<? extends PlaylistEntry> ov, PlaylistEntry old_val, PlaylistEntry new_val ) {
				if (ov == null || ov.getValue() == null) { return; } // TODO: remove this
				SimpleStringProperty sspTrack = new SimpleStringProperty( ov.getValue().getTrackName() );
				SimpleStringProperty sspArtist = new SimpleStringProperty(ov.getValue().getArtistName() );

				sspTrack.addListener(new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> ovTrack, String old_val, String new_val) { 
						ov.getValue().setTrackName(ovTrack.getValue().toString());
					}
				});

				sspArtist.addListener(new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> ovArtist, String old_val, String new_val) { 
						ov.getValue().setArtistName(ovArtist.getValue().toString());
					}
				});		

				trackTextField.textProperty().bindBidirectional(sspTrack);
				artistTextField.textProperty().bindBidirectional(sspArtist); 
			}
		});
	}
	/*  playlistObservable.addListener(new ListChangeListener<PlaylistEntry>() {
            		@Override public void onChanged( javafx.collections.ListChangeListener.Change<? extends PlaylistEntry> c ) {
            			//videos.resetArray(playlistObservable);
            			//for (int j = 0; j < playlistObservable.size(); j++) {
            			//	playlistObservable.get(j).setPositionInPlaylist(j + 1);
            			//	videos.getPlaylistEntries().get(j).setPositionInPlaylist(j + 1);	
            			}
            			//sendPlaylistNodesToScreen(videos);
            			//forceListRefreshOn(playlistView);
            			//playlistView.getSelectionModel().clearAndSelect(indexOfItemToMove - 1);	
            			//playlistView.getFocusModel().focus(indexOfItemToMove - 1);	
            		}
            	});*/

	public void sendPlaylistNodesToScreen(Playlist videos) {
		for (PlaylistEntry playlistEntry : videos.getPlaylistEntries())
			playlistView.getItems().add(playlistEntry);
	}

	@FXML void loadPlaylist(ActionEvent e) {

		viewListener.loadPlaylist(e);
		playlistView.setDisable(false); //TODO only disable these if it goes well.....
		
		/*final FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(stage);
		Playlist playlistTemp = new Playlist("hi");

		playlistObservable.clear(); //TDO: now we need to make sure we clear all 3 of the playlists! 
		playlist.resetArray(playlistObservable);

		//if (file != null) {
		//try { 
		XMLSerialisable playlistAsSerialisable = XMLReader.readPlyalistXML(file.toPath());	
		playlistTemp = (Playlist) playlistAsSerialisable;
		//}

		//TODO why do I need to instantiate the videos or else javafx will crash out the JVM, when appending to a pre-existing playlist will not make that happen
		for (int i = 0; i < playlistTemp.getPlaylistEntries().size(); i++) {
			PlaylistEntry entry = new PlaylistEntry( new MusicVideoXuggle( 
					playlistTemp.getPlaylistEntries().get( i ).getFileUNC() ),
					playlistTemp.getPlaylistEntries().get( i ).getTrackName(), 
					playlistTemp.getPlaylistEntries().get( i ).getArtistName()
					);
			entry.setPositionInPlaylist(i + 1); //set the playlist entry number while we have a loop! may be a problem later.....
			playlist.setNextEntry(entry);
		}

		playlist.setThemeName(playlistTemp.getThemeName()); //we need to set the actual playlist to the temp playlists theme name. Outside of loop as not entry specific
		String themename = playlist.getThemeName();
		playlistObservable = playlistView.getItems();
		sendPlaylistNodesToScreen(playlist);
		if	( themeSelectBox.getItems().contains(playlist.getThemeName() ) ) { //if the theme name is actually one of our themes	
			ObservableList<String> themeBoxItems = themeSelectBox.getItems(); //turn the theme box's list into an iterable list
			for ( String itemName : themeBoxItems ) { //iterate through it //until we match the text strings
				if ( themename.equals(itemName) ) {themeSelectBox.setValue(themename); }//and set that theme name as the active one in both the box and the list the box is generated from
			}
			playlistView.setDisable(false);
		}*/
	}

	@FXML void savePlaylist(ActionEvent e) throws PopupException {

		viewListener.savePlaylist(e);
		/*playlist.resetArray(playlistObservable);
		playlist.setThemeName(themeSelectBox.getSelectionModel().getSelectedItem().toString()); //Set theme name in the playlist xml
		setNumbersInPlaylist();
		XMLSerialisable xmlSerialisable = playlist;
		final FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(stage);
		String fileAsString = "";
		if(!file.getName().contains(".xml")) { 	fileAsString = file.toString() + ".xml"; } //this check helps if the file is already existing as .xml
		else { fileAsString = file.toString(); } //else we will get "x.xml.xml"
		Path path = Paths.get(fileAsString);
		XMLWriter.writePlaylistXML(true, path, xmlSerialisable);*/
	}
	

	@FXML void newPlaylist(ActionEvent e) { viewListener.newPlaylist(e); }

	@FXML void addPlaylistEntry(ActionEvent e) throws IOException { //TOD: loading a music video exception please
		PlaylistEntry entry = viewListener.addPlaylistEntry(e, stage);
		//playlistObservable.add(entry); //TODO we cannot pass the observable list outside of the view controller, so we return a playlist entry here
		//edit: and now it seems we can and it was adding twice
		playlistView.setDisable(false);
	}

	@FXML void deletePlaylistEntry(ActionEvent e) {
		viewListener.deletePlaylistEntry(e);
		//if (playlistObservable.isEmpty()) { playlistView.setDisable(true); }
	}

	@FXML void moveUp(ActionEvent e) {
		//MoveButtons moveUpButton = new MoveButtons(playlistView);
		//moveUpButton.moveUp(e);	
		viewListener.moveUp(e);
	}

	@FXML void moveDown(ActionEvent e) {
		//Dialog dialog = new Dialog();
		//Dialog.dialogBox(stage, "ehat");//, new Scene());
		try {
			viewListener.moveDown(e);
		} catch (IndexOutOfBoundsException error) {
			// TODO Auto-generated catch block
			//Dialog.dialogBox(stage, "No playlist loaded. Please first create or load a playlist");//, new Scene());
			error.printStackTrace();
		}
	}
	
	public void popup(Stage stage, String text) {
		Dialog.dialogBox(stage, text);
	}


	public void setNumbersInPlaylist() {
		for (int t = 0; t < playlist.getPlaylistEntries().size(); t++) {
			playlist.getPlaylistEntries().get(t).setPositionInPlaylist(t + 1); //set the playlist positions in the playlist to something sensible
		}
	}



	@FXML void render(ActionEvent e) {

		playlist.resetArray(playlistObservable);
		setNumbersInPlaylist();

		if (playlist.getPlaylistEntries().size() <= 0 ) { return; } //do nothing if theres no playlist
		for ( int i=0;i < playlist.getPlaylistEntries().size(); i++ ) {
			System.out.println("At postion: " + (i + 1) + " We have " + playlist.getPlaylistEntries().get(i).getFileUNC() );
		}

		String themeName = themeSelectBox.getSelectionModel().getSelectedItem().toString();
		Path rootDir = Paths.get("Theme");
		Path themeDir = Paths.get(rootDir.toString(),themeName);
		XMLSerialisable themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
		Theme theme = (Theme) themeAsSerialisable;
		theme.setIndex(themeSelectBox.getSelectionModel().getSelectedIndex()); //TODO; the lines above is effectively a new so any index setting before this has no effect
		Path properDir = Paths.get( Theme.getRootDir().toString(), theme.getItemName() );

		//first we must ask where you want to save with a dialog
		final FileChooser fileChooser = new FileChooser();
		String filetype = playlist.getNextEntry(0).getVideo().getFiletype();
		//TODO: problem is we need to get the FIRST files' filetype....is there a better way of encapsulating this its not obvious it go through about 3 classes...
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filetype + " files (*" + filetype + ")", "*" + filetype);
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(stage);
		String outFileUNC = "";
		if(!file.getName().endsWith( filetype ) ) { 	outFileUNC = file.toString() + filetype; } //this check helps if the file is already existing
		else { outFileUNC = file.toString(); } //else we will get "x.filetype.filetype //TODO: same code as in save playlist button
		if( file != null ) { Encoder draw = new EncoderXuggle(playlist, theme, outFileUNC); }

		DecodeAndPlayAudioAndVideo player = new DecodeAndPlayAudioAndVideo(outFileUNC);
	}

	/*@FXML void playlistEntryEntered(ActionEvent e) {
		String name = ""; // get from textbox
		viewListener.onNewTrackAvailable(name);
	}*/

	/**
	 * Makes an array of file unc paths to ten videos, then makes a new playlist, turns the UNC's into playlist entry music vids and adds them to the playlist,
	 */
	public void makeAPlaylist() {
playlistObservable.clear();
		/*vidFiles.add("../../../MVODAInputs/BrunoShort.mp4");
		vidFiles.add("../../../MVODAInputs/FlorenceShort.mp4");
		vidFiles.add("../../../MVODAInputs/GloriaShort.mp4");
		vidFiles.add("../../../MVODAInputs/KateShort.mp4");
		vidFiles.add("../../../MVODAInputs/LeonaShort.mp4");
		vidFiles.add("../../../MVODAInputs/MaroonShort.mp4");
		vidFiles.add("../../../MVODAInputs/NeyoShort.mp4");
		vidFiles.add("../../../MVODAInputs/NickiShort.mp4");
		vidFiles.add("../../../MVODAInputs/PinkShort.mp4");
		vidFiles.add("../../../MVODAInputs/RihannaShort.mp4");


		for (int i = 0; i < vidFiles.size(); i++) {
			PlaylistEntry entry = new PlaylistEntry( new MusicVideoXuggle( vidFiles.get( i ) ),"Track" + (i + 1) , "Artist" + (i + 1 ) );
			entry.setPositionInPlaylist(i + 1); //set the playlist entry number while we have a loop! may be a problem later.....
			playlist.setNextEntry(entry);
		}

		sendPlaylistNodesToScreen(playlist);
		playlistObservable = playlistView.getItems();*/
	}
}

/*	// adapted from: http://stackoverflow.com/questions/16880115/javafx-2-2-how-to-force-a-redraw-update-of-a-listview
	private void forceListRefreshOn(ListView lsv) {
		ObservableList items = lsv.getItems();
		lsv.setItems(lsv.getItems());
		lsv.setItems(items);
	}

*/


