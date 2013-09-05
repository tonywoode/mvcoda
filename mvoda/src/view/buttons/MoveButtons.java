package view.buttons;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import playlist.PlaylistEntry;
import themes.GFXElement;



public class MoveButtons {
	
	ListView<PlaylistEntry> playlistView;
	
	//private final static Logger LOGGER = Logger.getLogger(MoveButtons.class.getName());
	private final static Logger LOGGER = Logger.getLogger(MoveButtons.class.getName()); //setup a logger for this class
	 
	 

	
	public MoveButtons(ListView<PlaylistEntry> playlistView) {
		this.playlistView = playlistView;
		LOGGER.setLevel(Level.ALL); //set the level of the logger
	}
	
	public void moveUp(ActionEvent e) {
		
		
		int indexOfItemToMove = playlistView.getSelectionModel().getSelectedIndex();

		//if (indexOfItemToMove < 0) return; //don't attempt to move the top item

		PlaylistEntry temp = playlistView.getSelectionModel().getSelectedItem(); //temp entry holds the entry we want to move
		playlistView.getItems().set(indexOfItemToMove, playlistView.getItems().get(indexOfItemToMove - 1)); //set replaces the item: so move item below to selected index
		playlistView.getItems().set(indexOfItemToMove - 1, temp); //now move 

		PlaylistEntry moveDown = playlistView.getSelectionModel().getSelectedItem();
		PlaylistEntry moveUp = playlistView.getItems().get(indexOfItemToMove - 1);

		moveDown.setPositionInPlaylist(indexOfItemToMove);
		moveUp.setPositionInPlaylist(indexOfItemToMove + 1);

		LOGGER.info("Moving Up: " + moveUp.getPositionInPlaylist() + "; " + moveUp.getFileUNC());
		LOGGER.info("Moving Down: " + moveDown.getPositionInPlaylist() + "; " + moveDown.getFileUNC());
		

		/*	forceListRefreshOn(playlistView);

	playlistView.getSelectionModel().clearAndSelect(indexOfItemToMove - 1);	
	playlistView.getFocusModel().focus(indexOfItemToMove - 1);	
	playlistView.requestLayout();
		 */	

	}
	
	
	public void moveDown(ActionEvent e) {
		int indexOfItemToMove = playlistView.getSelectionModel().getSelectedIndex();
		int lastIndex = playlistView.getItems().size() -1;

		if (indexOfItemToMove > lastIndex) return; //TODO: still causing exception is it because i'm only catching the viewbox's error condition not the lists?

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
