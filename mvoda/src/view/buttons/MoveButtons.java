package view.buttons;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import media.MusicVideo;
import media.xuggle.MusicVideoXuggle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import playlist.PlaylistEntry;



public class MoveButtons {
	
	ListView<PlaylistEntry> playlistView;
	private ObservableList<PlaylistEntry> playlistObservable;
	//private Stage stage;
	private final static Logger LOGGER = Logger.getLogger(MoveButtons.class.getName()); //setup a logger for this class
	 
	
	public MoveButtons(ListView<PlaylistEntry> playlistView, ObservableList<PlaylistEntry> playlistObservable) {
		this.playlistView = playlistView;
		this.playlistObservable = playlistObservable;
		//this.stage = stage;
								LOGGER.setLevel(Level.ALL); //set the level of the logger
	}
	
	public PlaylistEntry addPlaylistEntry(ActionEvent e, Stage stage) throws IOException { //TOD: loading a music video exception please //note we pass a stage so we can popup in the cirrect place
		final FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(stage);
		//if (file != null) {
			String fileUNC = file.getAbsolutePath();
			MusicVideo vid = new MusicVideoXuggle(fileUNC);
			PlaylistEntry entry = new PlaylistEntry( vid, "Track" + (playlistObservable.size() + 1), "Artist" + (playlistObservable.size() + 1) );
			entry.setPositionInPlaylist(playlistObservable.size() + 1);//no point in doing this really
			playlistObservable = playlistView.getItems(); //we must update the array passed in to get the view to refresh, cleaner to do it here than back in viewcontroller
			playlistObservable.add(entry);
			
		//}
		return entry;
	}
	
	
	public void deletePlaylistEntry(ActionEvent e) {
		int indexOfItemToDelete = playlistView.getSelectionModel().getSelectedIndex();
		int indexSize = playlistView.getItems().size();
		//TODO still doesn't solve the issue where you lose focus and stop being able to delete after 2 items - yes 2 ITEMS!
		if (indexOfItemToDelete >= 0 && indexOfItemToDelete < indexSize ) { playlistView.getItems().remove(indexOfItemToDelete); }
	}
		
	
	public void moveUp(ActionEvent e) {
		int indexOfItemToMove = playlistView.getSelectionModel().getSelectedIndex();
		if (indexOfItemToMove <= 0) { return; } //don't attempt to move the top item, or anything in an empty list

		PlaylistEntry temp = playlistView.getSelectionModel().getSelectedItem(); //the entry we want to move
		playlistView.getItems().set(indexOfItemToMove, playlistView.getItems().get(indexOfItemToMove - 1)); //set replaces the item: so move item below to selected index
		playlistView.getItems().set(indexOfItemToMove - 1, temp); //now move original

		PlaylistEntry moveDown = playlistView.getSelectionModel().getSelectedItem();
		PlaylistEntry moveUp = playlistView.getItems().get(indexOfItemToMove - 1);

		moveDown.setPositionInPlaylist(indexOfItemToMove);
		moveUp.setPositionInPlaylist(indexOfItemToMove + 1);

								LOGGER.info("Moving Up: " + moveUp.getPositionInPlaylist() + "; " + moveUp.getFileUNC());
								LOGGER.info("Moving Down: " + moveDown.getPositionInPlaylist() + "; " + moveDown.getFileUNC());
	}
	
	
	public void moveDown(ActionEvent e) {
		int indexOfItemToMove = playlistView.getSelectionModel().getSelectedIndex();
		int lastIndex = playlistView.getItems().size() -1;
		if (indexOfItemToMove == lastIndex) { return; } //JavaFX's view array returns -1 for empty list, hence we catch both moving off end of list AND empty list

		PlaylistEntry temp = playlistView.getSelectionModel().getSelectedItem();
		playlistView.getItems().set(indexOfItemToMove, playlistView.getItems().get(indexOfItemToMove + 1));
		playlistView.getItems().set(indexOfItemToMove + 1, temp);

		PlaylistEntry moveUp = playlistView.getSelectionModel().getSelectedItem();
		PlaylistEntry moveDown = playlistView.getItems().get(indexOfItemToMove + 1);

		moveUp.setPositionInPlaylist(indexOfItemToMove);
		moveDown.setPositionInPlaylist(indexOfItemToMove);

		
								LOGGER.info("Moving Down: " + moveDown.getPositionInPlaylist() + "; " + moveDown.getFileUNC());
								LOGGER.info("Moving Up: " + moveUp.getPositionInPlaylist() + "; " + moveUp.getFileUNC());
	}
	
	

}
