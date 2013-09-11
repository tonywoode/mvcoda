package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.management.modelmbean.XMLParseException;

import lombok.Getter;
import lombok.Setter;
import media.Encoder;
import media.MusicVideo;
import media.xuggle.EncoderXuggle;
import media.xuggle.MusicVideoXuggle;
import moduleExamples.javafx2.BackgroundProcesses;
import playlist.Playlist;
import playlist.PlaylistEntry;
import test.DecodeAndPlayAudioAndVideo;
import themes.Theme;
import themes.XMLReader;
import themes.XMLSerialisable;
import themes.XMLWriter;
import util.FileUtil;
import util.ThemeFinder;
import util.ThemeFinderImpl;
import view.MediaOpenException;
import view.ViewController;
import view.ViewControllerListener;

public class MainController implements ViewControllerListener {

	@Getter @Setter public Playlist playlist = new Playlist("Biggest Beats I've seen in a while"); //TODO: playlist name
	@Getter @Setter private ObservableList<PlaylistEntry> observedEntries = FXCollections.observableArrayList(playlist.getPlaylistEntries());

	@Getter @Setter ViewController view;
	@Getter @Setter static Stage stage; //has to be static as instantiated in static JavaFX launch method in ImageCompositorTester
	
	static Task renderWorker;

	public final static Logger LOGGER = Logger.getLogger(MainController.class.getName()); //get a logger for this class

	@Override public void clearPlaylist() { observedEntries.clear(); }

	@Override public void addPlaylistEntry() throws IOException, MediaOpenException { //TOD: loading a music video exception please //note we pass a stage so we can popup in the cirrect place
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		String fileUNC;
		try {
			fileUNC = file.getAbsolutePath();
		} catch (NullPointerException e) {
			throw new NullPointerException("Please choose a valid file");	
		}
		MusicVideo vid = new MusicVideoXuggle(fileUNC);
		PlaylistEntry entry = new PlaylistEntry( vid, "Track" + (observedEntries.size() + 1), "Artist" + (observedEntries.size() + 1) );
		entry.setPositionInPlaylist(observedEntries.size() + 1);//no point in doing this really
		setObservedEntries(view.getPlaylistView().getItems() ); //we must update the array passed in to get the view to refresh, cleaner to do it here than back in viewcontroller
		observedEntries.add(entry);
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


	@Override public void loadPlaylist() throws FileNotFoundException, IOException, XMLParseException, MediaOpenException, InterruptedException {

		final FileChooser fileChooser = ViewController.getFileChooser(".xml");

		//read XML (exceptions thrown to view)
		File file;
		XMLSerialisable playlistAsSerialisable;
		try { file = fileChooser.showOpenDialog(stage);
		playlistAsSerialisable = XMLReader.readPlaylistXML(file.toPath());	
		} 
		catch (NullPointerException e) { throw new NullPointerException("Please select a file to load from to");	}

		//set XML contents as playlist to work on
		playlist = (Playlist) playlistAsSerialisable;
		boolean found = playlist.validatePlaylist(playlist);
		observedEntries.clear(); //clear the gui list

		setObservedEntries(view.getPlaylistView().getItems() );//we must update the array passed in to get the view to refresh, cleaner to do it here than back in viewcontroller
		view.sendPlaylistNodesToScreen(playlist);
		if (!found ) { 	view.popup("Problem with opening highlighted files, double click them to refind files");
		}

		//select the XML's theme as the theme in theme select box, or clear it if not found
		String themename = playlist.getThemeName();
		ObservableList<Theme> themeBoxItems = view.getThemeSelectBox().getItems(); //turn the theme box's list into an iterable list
		boolean themeFound = false;
		for ( Theme theme : themeBoxItems ) {
			if ( themename.equals(theme.toString()) ) {view.getThemeSelectBox().setValue(theme); themeFound=true; }//and set that theme name as the active one in both the box and the list the box is generated from
		}			
		if (!themeFound) { //if a theme name match isn't found, we must clear any previous theme selection, and throw an exception
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


	@Override public void render() throws IOException, MediaOpenException, NullPointerException {

		//playlist array gets whatever is in the gui at this moment for its entries array
		playlist.resetArray( observedEntries );
		setNumbersInPlaylist();

		if (playlist.getPlaylistEntries().size() <= 0 ) { return; } //do nothing if theres no playlist

		Theme theme;
		try {
			theme = view.getThemeSelectBox().getSelectionModel().getSelectedItem();
			theme.setIndex( view.getThemeSelectBox().getSelectionModel().getSelectedIndex() ); //TODO; the lines above is effectively a new so any index setting before this has no effect
		} 
		catch (NullPointerException e) { throw new NullPointerException("Please select a Theme"); }

		//LOG the entries
		for ( int i=0;i < playlist.getPlaylistEntries().size(); i++ ) {
			LOGGER.info("Rendering begun - At index postion: " + i + " The UNC path is " + playlist.getPlaylistEntries().get(i).getFileUNC() );
		}

		boolean ok = playlist.validatePlaylist(playlist);
		if (!ok) { throw new MediaOpenException("There are still problems with the playlist. Cannot render"); }
		//ok that will tell us if the files validate, but it won't tell us if all the files are the same. We do that next

		//TODO: problem is we need to get the FIRST files' filetype....is there a better way of encapsulating this its not obvious it go through about 3 classes...
		String filetype = playlist.getNextEntry(0).getVideo().getFiletype();

		for (PlaylistEntry entry : playlist.getPlaylistEntries()) {
			if ( !FileUtil.getFiletype(entry.getVideo().getFiletype() ).equalsIgnoreCase(filetype) ) {
				throw new MediaOpenException("All the files need to be the same type or I cannot render them");
			}
		}


		//first we must ask where you want to save with a dialog
		File file;
		String outFileUNC;
		try {
			final FileChooser fileChooser = ViewController.getFileChooser(filetype);
			file = fileChooser.showSaveDialog(stage);
			outFileUNC = "";
			if(!file.getName().endsWith( filetype ) ) { 	outFileUNC = file.toString() + filetype; } //this check helps if the file is already existing
			else { outFileUNC = file.toString(); } //else we will get "x.filetype.filetype //TODO: same code as in save playlist button
		}
		catch (NullPointerException e) { throw new NullPointerException("Please select a file to save to");	}
		
		
		renderWorker = createWorker(theme, file, outFileUNC);
		new Thread(renderWorker).start();
	}
	
	
	public Task createWorker(final Theme theme, final File file, final String outFileUNC) {
		return new Task() {
			
			@Override
			protected Object call() throws Exception {
				//then finally render it
				if( file != null ) { Encoder draw = new EncoderXuggle(playlist, theme, outFileUNC); }

				
				//lastly display it in the swing window				
				DecodeAndPlayAudioAndVideo player = new DecodeAndPlayAudioAndVideo(outFileUNC);
				return true;
			}
			};
		
	}


	public void setNumbersInPlaylist() {
		for (int t = 0; t < playlist.getPlaylistEntries().size(); t++) {
			playlist.getPlaylistEntries().get(t).setPositionInPlaylist(t + 1); //set the playlist positions in the playlist to something sensible
		}
	}

	@Override
	public void reFindPlaylistEntry(int pos) throws MediaOpenException {
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		String fileUNC = file.getAbsolutePath();
		PlaylistEntry entry =  view.getPlaylistView().getItems().get(pos);
		entry.setFileUNC(fileUNC);
		entry = entry.validatePlaylistEntry(entry);
		if (entry != null) {			
			view.getPlaylistView().getItems().remove(pos);
			view.getPlaylistView().getItems().add(pos, entry);
		}
		else { throw new MediaOpenException("The file is not valid, try again"); }



	}



}
