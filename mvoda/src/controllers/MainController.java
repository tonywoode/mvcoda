package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.management.modelmbean.XMLParseException;

import lombok.Getter;
import lombok.Setter;
import media.Encoder;
import media.MusicVideo;
import media.xuggle.DecodeAndPlayAudioAndVideo;
import media.xuggle.EncoderXuggle;
import media.xuggle.MusicVideoXuggle;
import playlist.Playlist;
import playlist.PlaylistEntry;
import themes.Theme;
import themes.XMLReader;
import themes.XMLSerialisable;
import themes.XMLWriter;
import view.MediaOpenException;
import view.ViewController;
import view.ViewControllerListener;

public class MainController implements ViewControllerListener {

	//private Playlist playlist;
	@Getter @Setter public Playlist playlist = new Playlist("Biggest Beats I've seen in a while"); //TODO: playlist name
	@Getter @Setter private ObservableList<PlaylistEntry> observedEntries = FXCollections.observableArrayList(playlist.getPlaylistEntries());

	@Getter @Setter ViewController view;
	@Getter @Setter static Stage stage; //has to be static as instantiated in static JavaFX launch method in ImageCompositorTester

	public final static Logger LOGGER = Logger.getLogger(MainController.class.getName()); //get a logger for this class

	public void onNewTrackAvailable(String name) {
		/*PlaylistEntry pe = new PlaylistEntry(null);
		pe.setTrackName(name);
		playlist.setNextEntry(pe);*/
	}


	@Override public void newPlaylist() { observedEntries.clear(); }


	@Override public PlaylistEntry addPlaylistEntry() throws IOException, MediaOpenException { //TOD: loading a music video exception please //note we pass a stage so we can popup in the cirrect place
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		//if (file != null) {
		String fileUNC = file.getAbsolutePath();
		MusicVideo vid = new MusicVideoXuggle(fileUNC);
		PlaylistEntry entry = new PlaylistEntry( vid, "Track" + (observedEntries.size() + 1), "Artist" + (observedEntries.size() + 1) );
		entry.setPositionInPlaylist(observedEntries.size() + 1);//no point in doing this really
		setObservedEntries(view.getPlaylistView().getItems() ); //we must update the array passed in to get the view to refresh, cleaner to do it here than back in viewcontroller
		observedEntries.add(entry);
		//}
		return entry;
	}


	@Override public void deletePlaylistEntry() {
		int indexOfItemToDelete = view.getPlaylistView().getSelectionModel().getSelectedIndex();
		int indexSize = view.getPlaylistView().getItems().size();
		//TODO still doesn't solve the issue where you lose focus and stop being able to delete after 2 items - yes 2 ITEMS!
		if (indexOfItemToDelete >= 0 && indexOfItemToDelete < indexSize ) { view.getPlaylistView().getItems().remove(indexOfItemToDelete); }
	}


	@Override public void moveUp() {
		int indexOfItemToMove = view.getPlaylistView().getSelectionModel().getSelectedIndex();
		if (indexOfItemToMove <= 0) { return; } //don't attempt to move the top item, or anything in an empty list

		PlaylistEntry temp = view.getPlaylistView().getSelectionModel().getSelectedItem(); //the entry we want to move
		view.getPlaylistView().getItems().set(indexOfItemToMove, view.getPlaylistView().getItems().get(indexOfItemToMove - 1)); //set replaces the item: so move item below to selected index
		view.getPlaylistView().getItems().set(indexOfItemToMove - 1, temp); //now move original

		PlaylistEntry moveDown = view.getPlaylistView().getSelectionModel().getSelectedItem();
		PlaylistEntry moveUp = view.getPlaylistView().getItems().get(indexOfItemToMove - 1);

		moveDown.setPositionInPlaylist(indexOfItemToMove);
		moveUp.setPositionInPlaylist(indexOfItemToMove + 1);

		LOGGER.info("Moving Up: " + moveUp.getPositionInPlaylist() + "; " + moveUp.getFileUNC());
		LOGGER.info("Moving Down: " + moveDown.getPositionInPlaylist() + "; " + moveDown.getFileUNC());
	}


	@Override public void moveDown() {
		int indexOfItemToMove = view.getPlaylistView().getSelectionModel().getSelectedIndex();
		int lastIndex = view.getPlaylistView().getItems().size() -1;
		if (indexOfItemToMove == lastIndex) { return; } //JavaFX's view array returns -1 for empty list, hence we catch both moving off end of list AND empty list

		PlaylistEntry temp = view.getPlaylistView().getSelectionModel().getSelectedItem();
		view.getPlaylistView().getItems().set(indexOfItemToMove, view.getPlaylistView().getItems().get(indexOfItemToMove + 1));
		view.getPlaylistView().getItems().set(indexOfItemToMove + 1, temp);

		PlaylistEntry moveUp = view.getPlaylistView().getSelectionModel().getSelectedItem();
		PlaylistEntry moveDown = view.getPlaylistView().getItems().get(indexOfItemToMove + 1);

		moveUp.setPositionInPlaylist(indexOfItemToMove);
		moveDown.setPositionInPlaylist(indexOfItemToMove);


		LOGGER.info("Moving Down: " + moveDown.getPositionInPlaylist() + "; " + moveDown.getFileUNC());
		LOGGER.info("Moving Up: " + moveUp.getPositionInPlaylist() + "; " + moveUp.getFileUNC());
	}


	@Override public void loadPlaylist() throws FileNotFoundException, IOException, XMLParseException, MediaOpenException {

		final FileChooser fileChooser = ViewController.getFileChooser(".xml");

		//read XML (exceptions thrown to view)
		File file;
		XMLSerialisable playlistAsSerialisable;
		file = fileChooser.showOpenDialog(stage);
		playlistAsSerialisable = XMLReader.readPlaylistXML(file.toPath());


		//set XML contents as playlist to work on
		playlist = (Playlist) playlistAsSerialisable;
		boolean found = playlist.validatePlaylist(playlist);
		observedEntries.clear(); //clear the gui list

		setObservedEntries(view.getPlaylistView().getItems() );//we must update the array passed in to get the view to refresh, cleaner to do it here than back in viewcontroller
		view.sendPlaylistNodesToScreen(playlist);
		if (!found ) { 	view.popup("Problem with opening highlighted files, double click them to refind files");
		}

		//select the XML's theme as the theme in theme select box
		String themename = playlist.getThemeName();
		if	( view.getThemeSelectBox().getItems().contains(playlist.getThemeName() ) ) { //if the theme name is actually one of our themes	
			ObservableList<String> themeBoxItems = view.getThemeSelectBox().getItems(); //turn the theme box's list into an iterable list
			for ( String itemName : themeBoxItems ) { //iterate through it //until we match the text strings
				if ( themename.equals(itemName) ) {view.getThemeSelectBox().setValue(themename); }//and set that theme name as the active one in both the box and the list the box is generated from
			}			
		}
		else { //if a theme name match isn't found, we must clear any previous theme selection, and throw an exception
			view.getThemeSelectBox().getSelectionModel().clearSelection();
			throw new NullPointerException("The Theme in the XML cannot be found in your themes folder");
		} 
	}	


	


	@Override public void savePlaylist() throws FileNotFoundException, IOException {

		playlist.resetArray( observedEntries );

		try { playlist.setThemeName( view.getThemeSelectBox().getSelectionModel().getSelectedItem().toString()); } 
		catch (NullPointerException e) { throw new NullPointerException("Please select a theme before saving"); } 


		setNumbersInPlaylist();
		XMLSerialisable xmlSerialisable = playlist;

		final FileChooser fileChooser = ViewController.getFileChooser(".xml");

		File file;
		String fileAsString = "";


		try { 
			file = fileChooser.showSaveDialog(stage);	 	
			if(!file.getName().contains(".xml")) { 	fileAsString = file.toString() + ".xml"; } //this check helps if the file is already existing as .xml
			else { fileAsString = file.toString(); } //else we will get "x.xml.xml"
		}
		catch (NullPointerException e) { throw new NullPointerException("Please select a file to save to");	}
		Path path = Paths.get(fileAsString);

		XMLWriter.writePlaylistXML(true, path, xmlSerialisable);	

	}


	@Override public void render() {

		//playlist gets whatever is in the gui windows for its entries array
		playlist.resetArray( observedEntries );
		setNumbersInPlaylist();


		if (playlist.getPlaylistEntries().size() <= 0 ) { return; } //do nothing if theres no playlist
		for ( int i=0;i < playlist.getPlaylistEntries().size(); i++ ) {
			System.out.println("At postion: " + (i + 1) + " We have " + playlist.getPlaylistEntries().get(i).getFileUNC() );
		}

		String themeName = view.getThemeSelectBox().getSelectionModel().getSelectedItem().toString();
		Path rootDir = Paths.get("Theme");
		Path themeDir = Paths.get(rootDir.toString(),themeName);
		Theme theme = new Theme("Not set"); //todo: tidy this up
		XMLSerialisable themeAsSerialisable;
		try {
			themeAsSerialisable = XMLReader.readXML(themeDir, themeName);
			theme = (Theme) themeAsSerialisable;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		theme.setIndex( view.getThemeSelectBox().getSelectionModel().getSelectedIndex() ); //TODO; the lines above is effectively a new so any index setting before this has no effect
		Path properDir = Paths.get( Theme.getRootDir().toString(), theme.getItemName() );

		//TODO: problem is we need to get the FIRST files' filetype....is there a better way of encapsulating this its not obvious it go through about 3 classes...
		String filetype = playlist.getNextEntry(0).getVideo().getFiletype();

		//first we must ask where you want to save with a dialog
		final FileChooser fileChooser = ViewController.getFileChooser(filetype);
		File file = fileChooser.showSaveDialog(stage);
		String outFileUNC = "";
		if(!file.getName().endsWith( filetype ) ) { 	outFileUNC = file.toString() + filetype; } //this check helps if the file is already existing
		else { outFileUNC = file.toString(); } //else we will get "x.filetype.filetype //TODO: same code as in save playlist button
		if( file != null ) { Encoder draw = new EncoderXuggle(playlist, theme, outFileUNC); }

		DecodeAndPlayAudioAndVideo player = new DecodeAndPlayAudioAndVideo(outFileUNC);
	}


	public void setNumbersInPlaylist() {
		for (int t = 0; t < playlist.getPlaylistEntries().size(); t++) {
			playlist.getPlaylistEntries().get(t).setPositionInPlaylist(t + 1); //set the playlist positions in the playlist to something sensible
		}
	}



}
