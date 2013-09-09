package view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.management.modelmbean.XMLParseException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import playlist.Playlist;
import playlist.PlaylistEntry;
import themes.Theme;
import util.ThemeFinder;
import util.ThemeFinderImpl;
import view.buttons.Dialog;

public class ViewController implements Initializable {

	//TODO: you've given these fx:id's but why? you don't do anything with the button but onClick...., so why do they need to be here
	//public Button loadPlaylistButton;


	@Getter @Setter ViewControllerListener viewListener;	
	@Getter @Setter Stage stage;
	@FXML @Getter @Setter ComboBox<String> themeSelectBox;
	@FXML @Getter @Setter ListView<PlaylistEntry> playlistView;
	@FXML TextArea mediaInfoArea;

	public Button savePlaylistButton;
	public TextField trackTextField;
	public TextField artistTextField;

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

		initThemeSelectBox();

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
				
				mediaInfoArea.setText(ov.getValue().getVideo().toString()); //write media info to screen for this entry
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

	public void initThemeSelectBox() {
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
		System.out.println(themename);
		themeSelectBox.setItems(themename);
	}

	public void sendPlaylistNodesToScreen(Playlist playlist) {
		for (PlaylistEntry playlistEntry : playlist.getPlaylistEntries())
			playlistView.getItems().add(playlistEntry);
	}

	@FXML void loadPlaylist(ActionEvent e) {
		try { viewListener.loadPlaylist(); 
		playlistView.setDisable(false); //TODO only disable these if it goes well.....
		} 
		catch (NullPointerException e1) { popup(e1.getMessage()); }
		catch (XMLParseException e2) { popup("Error: not a valid MV-CoDA XML file"); }	
		catch (FileNotFoundException e3) { popup("Error: Could not access the XML file"); }
		catch (IOException e4) { popup("Error: Could not close the input file"); }
	}

	@FXML void savePlaylist(ActionEvent e) { 
		try { viewListener.savePlaylist(); } 
		catch (NullPointerException e1) { popup(e1.getMessage()); }
		catch (FileNotFoundException e2) { popup("Error: Could not access the ouptut file"); }
		catch (IOException e3) { popup("Error: Could not close the ouptut file"); }
	}

	@FXML void newPlaylist(ActionEvent e) { viewListener.newPlaylist(); }

	@FXML void addPlaylistEntry(ActionEvent e) throws IOException { //TOD: loading a music video exception please
		PlaylistEntry entry = viewListener.addPlaylistEntry();
		//playlistObservable.add(entry); //TODO we cannot pass the observable list outside of the view controller, so we return a playlist entry here
		//edit: and now it seems we can and it was adding twice
		playlistView.setDisable(false);
	}

	@FXML void deletePlaylistEntry(ActionEvent e) {
		viewListener.deletePlaylistEntry();
		//if (playlistObservable.isEmpty()) { playlistView.setDisable(true); }
	}

	@FXML void moveUp(ActionEvent e) {
		//MoveButtons moveUpButton = new MoveButtons(playlistView);
		//moveUpButton.moveUp(e);	
		viewListener.moveUp();
	}

	@FXML void moveDown(ActionEvent e) {
		//Dialog dialog = new Dialog();
		//Dialog.dialogBox(stage, "ehat");//, new Scene());
		try {
			viewListener.moveDown();
		} catch (IndexOutOfBoundsException error) {
			// TODO Auto-generated catch block
			//Dialog.dialogBox(stage, "No playlist loaded. Please first create or load a playlist");//, new Scene());
			error.printStackTrace();
		}
	}


	@FXML void render(ActionEvent e) {
		viewListener.render();
	}
	
	public void popup(String text) {
		Dialog.dialogBox(stage, text);
	}
	
	@FXML void reFindVideoInPlaylistView() {
		System.out.println("heelo");
	}
	
	public static FileChooser getFileChooser(String filetype) {
		final FileChooser fileChooser = new FileChooser();
		//below we receive a full filetype i.e: .mp4 and convert to the word MP4 for the filetype notification
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(filetype.substring(1).toUpperCase() + " files (*" + filetype + ")", "*" + filetype);
		fileChooser.getExtensionFilters().add(extFilter);
		return fileChooser;
	}


	/*@FXML void playlistEntryEntered(ActionEvent e) {
		String name = ""; // get from textbox
		viewListener.onNewTrackAvailable(name);
	}*/


}

/*	// adapted from: http://stackoverflow.com/questions/16880115/javafx-2-2-how-to-force-a-redraw-update-of-a-listview
	private void forceListRefreshOn(ListView lsv) {
		ObservableList items = lsv.getItems();
		lsv.setItems(lsv.getItems());
		lsv.setItems(items);
	}

 */


