package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.Getter;
import media.MusicVideo;
import media.xuggle.MusicVideoXuggle;
import playlist.Playlist;
import playlist.PlaylistEntry;
import themes.XMLReader;
import themes.XMLSerialisable;
import themes.XMLWriter;
import view.PopupException;
import view.ViewControllerListener;
import view.ViewController;
import view.buttons.Dialog;
import view.buttons.MoveButtons;

public class MainScreenController implements ViewControllerListener {

	//private Playlist playlist;

	@Getter @Setter ViewController view;
	@Getter @Setter static Stage stage; //has to be static as instantiated in static JavaFX launch method in runner

	private final static Logger LOGGER = Logger.getLogger(MainScreenController.class.getName()); //get a logger for this class

	public void onNewTrackAvailable(String name) {
		/*PlaylistEntry pe = new PlaylistEntry(null);
		pe.setTrackName(name);
		playlist.setNextEntry(pe);*/
	}


	@Override public void newPlaylist(ActionEvent e) { view.getPlaylistObservable().clear(); }


	@Override public PlaylistEntry addPlaylistEntry(ActionEvent e, Stage stage) throws IOException { //TOD: loading a music video exception please //note we pass a stage so we can popup in the cirrect place
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		//if (file != null) {
		String fileUNC = file.getAbsolutePath();
		MusicVideo vid = new MusicVideoXuggle(fileUNC);
		PlaylistEntry entry = new PlaylistEntry( vid, "Track" + (view.getPlaylistObservable().size() + 1), "Artist" + (view.getPlaylistObservable().size() + 1) );
		entry.setPositionInPlaylist(view.getPlaylistObservable().size() + 1);//no point in doing this really
		view.setPlaylistObservable(view.getPlaylistView().getItems() ); //we must update the array passed in to get the view to refresh, cleaner to do it here than back in viewcontroller
		view.getPlaylistObservable().add(entry);

		//}
		return entry;
	}


	@Override public void deletePlaylistEntry(ActionEvent e) {
		int indexOfItemToDelete = view.getPlaylistView().getSelectionModel().getSelectedIndex();
		int indexSize = view.getPlaylistView().getItems().size();
		//TODO still doesn't solve the issue where you lose focus and stop being able to delete after 2 items - yes 2 ITEMS!
		if (indexOfItemToDelete >= 0 && indexOfItemToDelete < indexSize ) { view.getPlaylistView().getItems().remove(indexOfItemToDelete); }
	}


	@Override public void moveUp(ActionEvent e) {
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


	@Override public void moveDown(ActionEvent e) {
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


	@Override public void loadPlaylist(ActionEvent e) {

		final FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(stage);
		Playlist playlistTemp = new Playlist("hi");

		view.getPlaylistObservable().clear(); //TDO: now we need to make sure we clear all 3 of the playlists! 
		view.getPlaylist().resetArray( view.getPlaylistObservable() );

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
			view.setPlaylistObservable(view.getPlaylistView().getItems() ); //we must update the array passed in to get the view to refresh, cleaner to do it here than back in viewcontroller
			view.getPlaylistObservable().add(entry);
		}
		view.getPlaylist().setThemeName(playlistTemp.getThemeName()); //we need to set the actual playlist to the temp playlists theme name. Outside of loop as not entry specific
		String themename = view.getPlaylist().getThemeName();
		view.setPlaylistObservable(view.getPlaylistView().getItems() );
		view.sendPlaylistNodesToScreen(view.getPlaylist());
		if	( view.getThemeSelectBox().getItems().contains(view.getPlaylist().getThemeName() ) ) { //if the theme name is actually one of our themes	
			ObservableList<String> themeBoxItems = view.getThemeSelectBox().getItems(); //turn the theme box's list into an iterable list
			for ( String itemName : themeBoxItems ) { //iterate through it //until we match the text strings
				if ( themename.equals(itemName) ) {view.getThemeSelectBox().setValue(themename); }//and set that theme name as the active one in both the box and the list the box is generated from
			}			
		}
	}	


	@Override public void savePlaylist(ActionEvent e) throws PopupException {

		view.getPlaylist().resetArray( view.getPlaylistObservable() );
		
		try {
			view.getPlaylist().setThemeName( view.getThemeSelectBox().getSelectionModel().getSelectedItem().toString());
		} catch (NullPointerException e1) {
			throw new PopupException("Please Select A Theme Before Saving");
		} //Set theme name in the playlist xml
		
		view.setNumbersInPlaylist();
		XMLSerialisable xmlSerialisable = view.getPlaylist();
		final FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(stage);
		String fileAsString = "";
		if(!file.getName().contains(".xml")) { 	fileAsString = file.toString() + ".xml"; } //this check helps if the file is already existing as .xml
		else { fileAsString = file.toString(); } //else we will get "x.xml.xml"
		Path path = Paths.get(fileAsString);
		XMLWriter.writePlaylistXML(true, path, xmlSerialisable);
	}


public static void popup(String text) {
	Dialog.dialogBox(stage, text);
}


}
