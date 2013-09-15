package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import media.MediaOpenException;
import media.MusicVideo;
import media.xuggle.PlayVideoFile;
import media.xuggle.EncoderXuggle;
import media.xuggle.MusicVideoXuggle;
import playlist.Playlist;
import playlist.PlaylistEntry;
import themes.Theme;
import util.FileUtil;
import util.XMLReader;
import util.XMLSerialisable;
import util.XMLWriter;
import view.ViewController;
import view.ViewControllerListener;
import drawing.TextCompositor;

/**
 * The main controller acts as presenter passing model data to GUI, passing GUI events to model, and actioning events up to the final save or render
 * @author Tony
 *
 */
public class MainController implements ViewControllerListener {

	public final static Logger LOGGER = Logger.getLogger(MainController.class.getName()); //get a logger for this class

	@Getter @Setter private Playlist playlist = new Playlist("Standard MV-CoDA v1 Render");//Playlist name is for future use
	@Getter @Setter private ObservableList<PlaylistEntry> observedEntries = FXCollections.observableArrayList(playlist.getPlaylistEntries());
	@Getter @Setter private ViewController view;
	@Getter @Setter static Stage stage; //has to be static as instantiated in static JavaFX launch method in ImageCompositorTester
	private static Task<?> renderWorker;

	/**
	 * Clears the GUI's playlist view
	 */
	@Override public void clearPlaylistEntries() { observedEntries.clear(); }

	/**
	 * Actions caused by the add entry button in GUI
	 */
	@Override public void addPlaylistEntry() throws IOException, MediaOpenException {
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		String fileUNC;
		try { fileUNC = file.getAbsolutePath(); }
		catch (NullPointerException e) { throw new NullPointerException("Please choose a valid file"); }
		MusicVideo vid = new MusicVideoXuggle(fileUNC);
		PlaylistEntry entry = new PlaylistEntry( vid, "Enter Track", "Enter Artist", "Enter Track Info" );
		entry.setPositionInPlaylist(observedEntries.size() + 1);//defensively set position in case something happens later
		setObservedEntries(view.getPlaylistView().getItems() ); //we must update the array passed in to get the view to refresh, cleaner to do it here than back in viewcontroller
		observedEntries.add(entry);
	}

	/**
	 * Actions caused by the delete entry button in GUI
	 */
	@Override public void deletePlaylistEntry() {
		int indexOfItemToDelete = view.getPlaylistView().getSelectionModel().getSelectedIndex();
		int indexSize = view.getPlaylistView().getItems().size();
		//TODO JAVAFX incorrectly loses focus of entries in the Text List and has no setFocus method
		if (indexOfItemToDelete >= 0 && indexOfItemToDelete < indexSize ) { view.getPlaylistView().getItems().remove(indexOfItemToDelete); }
	}

	/**
	 * The move up button in the GUI must switch the position in the playlist of the selected item, with the item above it in the list.
	 * It must also update the chart numbers that display onscreen
	 */
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

	/**
	 * The move down button in the GUI must switch the postion in the playlist of the selected item, with the item below it in the list.
	 * It must also update the chart numbers that display onscreen
	 */
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

	/**
	 * Load playlist must be above to clear the current GUI's list and populate it from the XML file that is selected by the user, and populate all the additional
	 * GUI fields
	 */
	@Override public void loadPlaylist() throws FileNotFoundException, IOException, MediaOpenException {

		final FileChooser fileChooser = ViewController.getFileChooser(".xml");

		//read XML (exceptions thrown to view)
		File file;
		XMLSerialisable playlistAsSerialisable;
		try { 
			file = fileChooser.showOpenDialog(stage);
			playlistAsSerialisable = XMLReader.readPlaylistXML(file.toPath());	
		} 
		catch (NullPointerException e) { throw new NullPointerException("Please select a file to load from");	}

		//set XML contents as playlist to work on
		playlist = (Playlist) playlistAsSerialisable;
		boolean found = playlist.validatePlaylist(playlist);
		observedEntries.clear(); //clear the gui list

		//we must update the array passed in to get the view to refresh, cleaner to do it here than back in view controller
		view.sendPlaylistNodesToScreen(playlist);
		setObservedEntries(view.getPlaylistView().getItems() );

		//if one or more items are not found, we inform the GUI with a message, but carry on....
		if (!found ) { 	ViewController.popup("Problem with opening highlighted files, double click them to refind files"); }

		//select the XML's theme as the theme in theme select box, or clear it if not found
		String themeName = playlist.getThemeName();
		ObservableList<Theme> themeBoxItems = view.getThemeSelectBox().getItems(); //turn the theme box's list into an iterable list
		boolean themeFound = false;
		for ( Theme theme : themeBoxItems ) {
			if ( themeName.equals(theme.toString()) ) {
				view.getThemeSelectBox().setValue(theme); 
				view.getThemeSelectBox().getSelectionModel().select(theme);
				themeFound=true; }//and set that theme name as the active one in both the box and the list the box is generated from
		}			
		if (!themeFound) { //if a theme name match isn't found, we must clear any previous theme selection, and throw an exception
			view.getThemeSelectBox().getSelectionModel().clearSelection();
			ViewController.popup("The Theme in the XML cannot be found in your themes folder");
		} 
		LOGGER.info("themeName from playlist on load is : " + themeName);
		LOGGER.info("Font select box has this selected on load playlist " + view.getFontSelectBox().getSelectionModel().getSelectedItem());

		//now similar (but with subtle differences) with the font TODO: generic method possible?
		String fontName = playlist.getFontName();

		ObservableList<String> fontBoxItems = view.getFontSelectBox().getItems();
		boolean fontFound = false;
		for ( String string : fontBoxItems ) {
			if ( string.equals(fontName) ) {
				view.getFontSelectBox().getSelectionModel().select(fontName); 
				view.getFontSelectBox().getSelectionModel().select(string);
				fontFound=true; }
		}			
		if (!fontFound) {
			view.getFontSelectBox().getSelectionModel().clearSelection();
			ViewController.popup("The Font in the XML cannot be found on your computer");
		} 
		LOGGER.info("fontName from playlist on load is : " + fontName);
		LOGGER.info("Font select box has this selected on load playlist " + view.getFontSelectBox().getSelectionModel().getSelectedItem());

		//and again with the FontSize box
		int fontSize = playlist.getFontSize();
		ObservableList<Number> fontBoxSizes = view.getFontSizeBox().getItems();
		boolean fontSizeFound = false;
		for ( Number num : fontBoxSizes ) {
			if ( num.intValue() == fontSize ) {
				view.getFontSizeBox().getSelectionModel().select(fontSize);  //known JavaFX bug http://stackoverflow.com/questions/12142518/combobox-clearing-value-issue
				view.getFontSizeBox().getSelectionModel().select(num);
				fontSizeFound=true; }
		}			
		if (!fontSizeFound) {
			view.getFontSizeBox().getSelectionModel().clearSelection();
			ViewController.popup("The FontSize in the XML cannot be set on your computer");
		} 
		LOGGER.info("fontSize from playlist on load is : " + fontSize);
		LOGGER.info("Font size box has this selected on load playlist " + view.getFontSizeBox().getSelectionModel().getSelectedItem());


		String chartName = playlist.getChartName();
		if (chartName == null || chartName.equals("") ) { view.getChartTextField().clear(); }
		view.getChartTextField().setText(chartName);


	}	

	/**
	 * To save a playlist we must take changes from the GUI and ensure they are in the playlist object, we check the chart positions defensively,
	 * and we ensure User has at least set a theme (faults default to values so are of secondary concern).
	 * We then serialise the result to a file the user chooses
	 */
	@Override public void savePlaylist() throws FileNotFoundException, IOException {

		resetArray( observedEntries );
		playlist.setFontName(TextCompositor.getFontName()); //set the fontname in the playlist before saving, GUI default writes here so no other checks apply
		playlist.setFontSize(TextCompositor.getFontSize());
		playlist.setChartName(view.getChartTextField().getText());
		if ( playlist.getChartName() == null || playlist.getChartName().equals("") ) { throw new IllegalArgumentException("Please give the Chart you're saving a name"); }

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
		//finally write the playlist
		XMLWriter.writePlaylistXML(true, path, xmlSerialisable);	

	}

	/**
	 * Rendering a playlist means to composite all the choices the user has made as a media file on disk. So Render will take all the choices, check what it can
	 * and then call the media implementation to render if all is ok
	 */
	@Override public void render() throws IOException, MediaOpenException, NullPointerException {

		//playlist array gets whatever is in the gui at this moment for its entries array
		resetArray( observedEntries );
		setNumbersInPlaylist(); //defensively

		if (playlist.getPlaylistEntries().size() <= 0 ) { return; } //do nothing if there's no playlist
		//now check the theme is ok to render
		Theme theme;
		try {
			theme = view.getThemeSelectBox().getSelectionModel().getSelectedItem();
			theme.setIndex( view.getThemeSelectBox().getSelectionModel().getSelectedIndex() );
		} 
		catch (NullPointerException e) { throw new NullPointerException("Please select a Theme"); }

		//now set and check the chart name
		playlist.setChartName(view.getChartTextField().getText());
		if ( playlist.getChartName() == null || playlist.getChartName().equals("") ) { throw new IllegalArgumentException("You must give the Chart you're rendering a name"); }

		//LOG the entries
		for ( int i=0;i < playlist.getPlaylistEntries().size(); i++ ) {
			LOGGER.info("Rendering begun - At index postion: " + i + " The UNC path is " + playlist.getPlaylistEntries().get(i).getFileUNC() );
		}

		//now validate the playlist entries
		boolean ok = playlist.validatePlaylist(playlist);
		if (!ok) { throw new MediaOpenException("There are still problems with the playlist. Cannot render"); }


		//In this version of MV-CoDA, we need to get chart number 1's filetype and refuse to render if all the files aren't that type
		String filetype = playlist.getPlaylistEntries().get(0).getVideo().getFiletype();

		for (PlaylistEntry entry : playlist.getPlaylistEntries()) {
			if ( !FileUtil.getFiletype(entry.getVideo().getFiletype() ).equalsIgnoreCase(filetype) ) {
				throw new MediaOpenException("All the files need to be the same type or I cannot render them");
			}
		}

		//we must ask where the user wants to save with a dialog
		File file;
		String outFileUNC;
		try {
			final FileChooser fileChooser = ViewController.getFileChooser(filetype);
			file = fileChooser.showSaveDialog(stage);
			outFileUNC = "";
			if(!file.getName().endsWith( filetype ) ) { 	outFileUNC = file.toString() + filetype; } //this check helps if the file is already existing
			else { outFileUNC = file.toString(); } //else we will get "x.filetype.filetype
		}
		catch (NullPointerException e) { throw new NullPointerException("Please select a file to save to");	}

		//then thread the actual render and display work to createWorker()
		renderWorker = createWorker(theme, file, outFileUNC);
		new Thread(renderWorker).start();
	}

	/**
	 * Actually renders the final result and displays the finshed produce in a window
	 * @param theme the selected Theme
	 * @param file the file reference the user has chosen
	 * @param outFileUNC the path as string
	 * @return true if successful, will also play result in a window
	 */
	public Task<?> createWorker(final Theme theme, final File file, final String outFileUNC) {
		return new Task<Object>() {

			@Override
			protected Object call() throws Exception {
				//then finally render it
				if( file != null ) { new EncoderXuggle(playlist, theme, outFileUNC); }		
				//lastly display it in the swing window				
				new PlayVideoFile(outFileUNC);
				return true;
			}
		};

	}

	public void resetArray(ObservableList<PlaylistEntry> playlistObservable) {
		ArrayList<PlaylistEntry> temp = new ArrayList<>(); //because if we try to directly do this to playlistEntries we'll get the concurrentModificationError
		temp.addAll(playlistObservable);
		playlist.getPlaylistEntries().clear(); //clear playlist outside of view
		playlist.getPlaylistEntries().addAll( temp ); //replace its contents with the view
	}

	/**
	 * Sets the chart numbers for a given playlist's entries array
	 */
	public void setNumbersInPlaylist() {
		for (int t = 0; t < playlist.getPlaylistEntries().size(); t++) {
			playlist.getPlaylistEntries().get(t).setPositionInPlaylist(t + 1); //set the playlist positions in the playlist to something sensible
		}
	}

	/**
	 * If a playlist entry has to be refound by the user because it has a problem, we will re-validate that entry,
	 * and if it is ok, we will replace the problem entry
	 */
	@Override public void reFindPlaylistEntry(int pos) throws MediaOpenException {
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

	/**
	 * Takes fontNames from the view and passes them to the model
	 * @param fontName string representation of a font on secondary storage
	 */
	public void setFontName(String fontName) {
		TextCompositor.setFontName(fontName);
	}	
	
	/**
	 * Takes fontsizes from the view and passes them to the model
	 * @param fontSize
	 */
	public void setFontSize(int fontSize) {
		TextCompositor.setFontSize(fontSize);
	}
	
	
	


}
