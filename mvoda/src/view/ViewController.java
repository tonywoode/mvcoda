package view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
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
	public Button savePlaylistButton;

	@Getter @Setter ViewControllerListener viewListener;	
	@Getter @Setter Stage stage;

	public TextField trackTextField;
	public TextField artistTextField;

	
	//@Getter @Setter public Playlist playlist = new Playlist("Biggest Beats I've seen in a while"); //TODO: playlist name
	//@Getter @Setter private ObservableList<PlaylistEntry> playlistObservable = FXCollections.observableArrayList(new ArrayList<PlaylistEntry>());
	//public ArrayList<String> vidFiles = new ArrayList<>();

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

	public void sendPlaylistNodesToScreen(Playlist playlist) {
		for (PlaylistEntry playlistEntry : playlist.getPlaylistEntries())
			playlistView.getItems().add(playlistEntry);
	}

	@FXML void loadPlaylist(ActionEvent e) {
		viewListener.loadPlaylist();
		playlistView.setDisable(false); //TODO only disable these if it goes well.....
		
	}

	@FXML void savePlaylist(ActionEvent e) throws PopupException { viewListener.savePlaylist(); }
	
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
	
	public void popup(Stage stage, String text) {
		Dialog.dialogBox(stage, text);
	}


	public void setNumbersInPlaylist() {
		for (int t = 0; t < viewListener.getPlaylist().getPlaylistEntries().size(); t++) {
			viewListener.getPlaylist().getPlaylistEntries().get(t).setPositionInPlaylist(t + 1); //set the playlist positions in the playlist to something sensible
		}
	}


	@FXML void render(ActionEvent e) {
		viewListener.render();
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


