package view;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import media.MediaOpenException;
import playlist.Playlist;
import playlist.PlaylistEntry;
import themes.GFXElementException;
import themes.Theme;
import util.ThemeFinder;
import util.ThemeFinderImpl;
import util.ThumbnailGrabber;
import util.ThumbnailGrabberXuggle;
import view.buttons.Dialog;

/**
 * Handles communication between the JavaFX FXML file and MV-CoDA's main controller, and reports information and errors to the user
 * The ViewController talks to the main controller via a listener interface to decouple the two.
 * This class contains a number of JAVAFX bindings and listeners to enable communication between diferent parts of the GUI, and the model
 * via the main controller
 * @author tony
 *
 */
public class ViewController implements Initializable {

	public final static Logger LOGGER = Logger.getLogger(ViewController.class.getName()); //get a logger for this class
	@FXML private static ImageView imageThumb;
	@Setter	private static ViewControllerListener viewListener;	
	@Setter	private static Stage stage;
	private static Image fxImage;
	private static BufferedImage thisThumb;
	private static Task<?> thumbnailWorker;

	@FXML private Button clearPlaylistButton;
	@FXML private Button savePlaylistButton;
	@FXML private Button deletePlaylistEntryButton;
	@FXML private Button moveUpButton;
	@FXML private Button moveDownButton;
	@FXML private Button renderButton;

	@FXML @Getter @Setter private ListView<PlaylistEntry> playlistView;
	@FXML @Getter private ComboBox<Theme> themeSelectBox;
	@FXML @Getter private ComboBox<String> fontSelectBox;
	@FXML @Getter private ComboBox<Number> fontSizeBox;
	@FXML @Getter private TextField chartTextField;

	@FXML private TextArea mediaInfoArea;

	public TextField trackTextField;
	public TextField artistTextField;
	public TextArea trackInfoTextField;

	private ThumbnailGrabber grabber = new ThumbnailGrabberXuggle();



	/**
	 * Main constructor for the GUI - binds buttons and views to the model with bindings, bidirectional bindings and listeners
	 */
	@Override public void initialize(URL arg0, ResourceBundle arg1) {		

		playlistView.setDisable(true); //we will enable the playlistview when it populates with items
		savePlaylistButton.disableProperty().bind(playlistView.disabledProperty()); //the save playlist button will follow the playlistview buttons enable status
		deletePlaylistEntryButton.disableProperty().bind(playlistView.disabledProperty());
		clearPlaylistButton.disableProperty().bind(playlistView.disabledProperty());
		renderButton.disableProperty().bind(playlistView.disabledProperty());
		moveUpButton.setDisable(true); //whereas these two buttons must be disabled if one or less entries are in the list
		moveDownButton.setDisable(true); //so we handle these in checkMoveButtons() (you cannot both bind and set in javaFX)

		initThemeSelectBox(); //reads themes 
		initFontSelectBox(); //font list to GUI
		initFontSizeBox(); //font list to GUI


		//custom cell factory for the playlist entries
		playlistView.setCellFactory(new Callback<ListView<PlaylistEntry>, ListCell<PlaylistEntry>>() {
			@Override public ListCell<PlaylistEntry> call(ListView<PlaylistEntry> list) {
				return new PlaylistEntryListCell();
			}
		});

		//tie the track and artist and media select boxes to the currently selected playlist entry
		playlistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PlaylistEntry>() {
			public void changed( final ObservableValue<? extends PlaylistEntry> ov, PlaylistEntry old_val, PlaylistEntry new_val ) {
				if (ov == null || ov.getValue() == null) { return; }
				SimpleStringProperty sspTrack = new SimpleStringProperty( ov.getValue().getTrackName() );
				SimpleStringProperty sspArtist = new SimpleStringProperty(ov.getValue().getArtistName() );
				SimpleStringProperty sspInfoText = new SimpleStringProperty(ov.getValue().getTrackInfo() );

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

				sspInfoText.addListener(new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> ovTrackInfo, String old_val, String new_val) { 
						ov.getValue().setTrackInfo(ovTrackInfo.getValue().toString());
					}
				});	

				trackTextField.textProperty().bindBidirectional(sspTrack);
				artistTextField.textProperty().bindBidirectional(sspArtist); 
				trackInfoTextField.textProperty().bindBidirectional(sspInfoText);

				//now deal with the media Info area and the thumbnail display
				if (ov == null || ov.getValue().getVideo() == null) { mediaInfoArea.setText("File Not Found"); imageThumb.setImage( null );	}
				else {
					mediaInfoArea.setText(ov.getValue().getVideo().toString());//write media info to screen for this entry
					loadThumb(ov.getValue());
				}	
			}

		});
	}


	/**
	 * Loads the thumbnail display in a worker thread, and delays main thread so listeners can get through
	 * @param entry playlist entry we want to find a thumbnail for
	 */
	private void loadThumb(PlaylistEntry entry) {
		thumbnailWorker = createWorker( entry );
		//we must introduce some delay into the master thread or our listeners stop being received
		try { new Thread(thumbnailWorker).start(); Thread.sleep(500);  }
		catch (InterruptedException e) { e.printStackTrace(); }

	}
	/**
	 * Worker thread that gets thumbnails for the GUI, for now by using Xuggler's Media Tools API in Thumbnail Grabber imp in the util package
	 * @param entry
	 * @return
	 */
	public Task<?> createWorker(final PlaylistEntry entry) {
		return new Task<Object>() {

			@Override protected Object call() throws Exception {

				thisThumb =  grabber.grabThumbs(entry.getFileUNC());
				fxImage = SwingFXUtils.toFXImage(thisThumb, null);
				imageThumb.setImage( fxImage );
				return true;
			}
		};

	}

	/**
	 * Initialises the combobox that holds themes - these are held directly as Themes in the box and their toString() methods provide the text in the box
	 */
	public void initThemeSelectBox() {
		ThemeFinder themeFinder = new ThemeFinderImpl(); //we must instantiate the themeFinder because it implements an interface - could use a factory instead
		ArrayList<Theme> themeTemp = new ArrayList<>(); //we don't want the themeFinder to be tied to JavaFX's observable list imp
		try { themeTemp = themeFinder.returnThemes(); } 
		catch (IOException e) { popup( e.getMessage() ); } 	
		themeSelectBox.setItems(FXCollections.observableList(themeTemp) ); //NOW we make an observable list from our array list when we set it as the box's list
	}


	/**
	 * By obtaining the fonts on the local machine, we populate the font select box with their string representations, and because we are using strings
	 * We are able to set a font in the model. A listener ensures that changes in the value are provided to the main controller
	 */
	public void initFontSelectBox() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontListArray = ge.getAvailableFontFamilyNames();
		ArrayList<String> fontList = new ArrayList<String>( Arrays.asList(fontListArray) );
		LOGGER.info("FontSelectBox sees Available font list on this machine: " + fontList);
		fontSelectBox.setItems(FXCollections.observableList(fontList));
		fontSelectBox.getSelectionModel().select(10); //displays but doesn't select - seems to be another javafx bug, we will set the same in text compositor
		fontSelectBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				viewListener.setFontName(newValue);
			}	
		});		
	}

	/**
	 * We set a specific number of fontsizes which can be used (these must be sensible to fit in straps, chart, number boxes in a television resolution)
	 * and allow the user to set these. A listener ensures that changes in the value are provided to the main controller
	 */
	public void initFontSizeBox() {
		ArrayList<Number> fontSizeList = new ArrayList<Number>();
		fontSizeList.add(16); fontSizeList.add(20); fontSizeList.add(24); fontSizeList.add(28); fontSizeList.add(32); fontSizeList.add(36); fontSizeList.add(40);
		LOGGER.info("FontSizeBox has these choices: " + fontSizeList);
		fontSizeBox.setItems(FXCollections.observableList(fontSizeList));
		fontSizeBox.getSelectionModel().select(2); //int method for index - also set as default in text compositor
		fontSizeBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Number>() {
			@Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				viewListener.setFontSize(newValue.intValue());
			}	
		});

	}

	/**
	 * Takes a playlist and sends the entries to the GUI's list view
	 * @param playlist a playlist
	 */
	public void sendPlaylistNodesToScreen(Playlist playlist) {
		for (PlaylistEntry playlistEntry : playlist.getPlaylistEntries())
			playlistView.getItems().add(playlistEntry);
	}

	/**
	 * Allows the user to request that a playlist file is deserialised and returned to the GUI. Will clear any existing entries if the user doesn't cancel
	 * Will report errors with that process directly to the user
	 * @param e when button accessed by user
	 */
	@FXML void loadPlaylist(ActionEvent e) {
		if ( !playlistView.getItems().isEmpty() ) { popup("This will clear any existing information"); }
		try { viewListener.loadPlaylist(); } 
		catch (NullPointerException e1) { popup(e1.getMessage()); }
		catch (FileNotFoundException e3) { popup(e3.getMessage() ); }
		catch (IOException e4) { popup("Error: Could not close the input file"); }
		catch (MediaOpenException e5) { popup(e5.getMessage()); }
		checkMoveButtons();
		if (!playlistView.getItems().isEmpty()) { 
			trackTextField.clear();
			artistTextField.clear();
			trackInfoTextField.clear();
			playlistView.setDisable(false); } //only enable if something loaded
	}

	/**
	 * Allows the user to request that a playlist file is serialised containing data currently in the GUI.
	 * Will report errors with that process directly to the user
	 * @param e when button is accessed by user
	 */
	@FXML void savePlaylist(ActionEvent e) { 
		try { viewListener.savePlaylist(); } 
		catch (NullPointerException e1) { popup(e1.getMessage()); }
		catch (FileNotFoundException e2) { popup("Error: Could not access the ouptut file"); }
		catch (IOException e3) { popup("Error: Could not close the ouptut file"); }
		catch (IllegalArgumentException e4) { popup(e4.getMessage()); }
	}

	/**
	 * Allows the user to clear a playlist and start afresh. Will check the values of buttons and return fonts to default sizes
	 * @param e when button is accessed by user
	 */
	@FXML void clearPlaylist(ActionEvent e) { //TODO: "are you sure" popup
		viewListener.clearPlaylistEntries(); 
		checkMoveButtons(); 
		themeSelectBox.getSelectionModel().clearSelection();
		fontSelectBox.getSelectionModel().select(10);
		fontSizeBox.getSelectionModel().select(2);
		chartTextField.clear();
		trackTextField.clear();
		artistTextField.clear();
		trackInfoTextField.clear();
		playlistView.setDisable(true);
	}

	/**
	 * Allows the user to request that an individual music video from secondary storage is added to their current playlist. Will be added at the bottom
	 * of the playlist.
	 * Will report errors with that process directly to the user
	 * @param e when button is accessed by user
	 */
	@FXML void addPlaylistEntry(ActionEvent e) throws IOException {
		try { 
			viewListener.addPlaylistEntry(); 
			if (playlistView.isDisable()) {	playlistView.setDisable(false); }
		}
		catch (MediaOpenException e1) { popup(e1.getMessage()); }
		catch (NullPointerException e2) { popup(e2.getMessage()); }
		checkMoveButtons();
	}

	/**
	 * We must only enable these buttons if there is more than ONE playlist entry
	 */
	private void checkMoveButtons() {
		if (playlistView.getItems().size() <= 1 ) { moveUpButton.setDisable(true); moveDownButton.setDisable(true);  }
		else { moveUpButton.setDisable(false); moveDownButton.setDisable(false); }
	}

	/**
	 * Allows the user to request that the highlighted playlist entry is deleted from the playlist
	 * @param e when button is accessed by user
	 */
	@FXML void deletePlaylistEntry(ActionEvent e) {
		viewListener.deletePlaylistEntry();
		checkMoveButtons();	
		if (playlistView.getItems().isEmpty()) { playlistView.setDisable(true); }
	}

	/**
	 * Moves a playlist up one in the list
	 * @param e when button is accessed by user
	 */
	@FXML void moveUp(ActionEvent e) { viewListener.moveUp(); }

	/**
	 * Moves a playlist entry down one in the list
	 * @param e when button is accessed by user
	 */
	@FXML void moveDown(ActionEvent e) { viewListener.moveDown(); }

	/**
	 * Allows the user to request the current GUI's playlist to render to a video file. Reports errors in that proces back to the user
	 * @param e when button is accessed by user
	 */
	@FXML void render(ActionEvent e) {
		try { viewListener.render();} 
		catch (NullPointerException e1) { popup(e1.getMessage() ); }	
		catch (MediaOpenException e2) { popup(e2.getMessage() ); }
		catch (GFXElementException e3) { popup(e3.getMessage() ); }
		catch (IOException e4) { popup("Error: Could not access the file to write to");}
		catch (IllegalArgumentException e5) { popup(e5.getMessage() ); }	

	}

	/**
	 * JAVAFX has no default dialog boxes. We call a simple implementation of a dialog box when the user needs to be informed from the model
	 * @param text the text to display
	 */
	public static void popup(String text) {
		Dialog.dialogBox(stage, text);
	}

	/**
	 * Generic method for the load/save actions the user can request. JAVAFX brings up a standard windows file dialog, which handles a lot of problems for us.
	 * Ensures that the file extension the user requests is provided to the dialog
	 * @param filetype
	 * @return
	 */
	public static FileChooser getFileChooser(String filetype) {
		final FileChooser fileChooser = new FileChooser();
		//below we receive a full filetype i.e: .mp4 and convert to the word MP4 for the filetype notification
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filetype.substring(1).toUpperCase() + " files (*" + filetype + ")", "*" + filetype);
		fileChooser.getExtensionFilters().add(extFilter);
		return fileChooser;
	}

	/**
	 * When the user double clicks on a problem entry in the playlist, we allow them to select a file from secondary storage
	 * @param pos the position in the playlist array the entry concerned is located
	 */
	public static void reFindPlaylistEntry(int pos) {
		try { viewListener.reFindPlaylistEntry(pos); } 
		catch (MediaOpenException e) { popup(e.getMessage() ); }
		catch (NullPointerException e1) { popup("Please select a file to load from");	}
	}

	
}




