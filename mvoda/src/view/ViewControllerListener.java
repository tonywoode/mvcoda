package view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import playlist.PlaylistEntry;

public interface ViewControllerListener {
	void onNewTrackAvailable(String name);

	PlaylistEntry addPlaylistEntry(ActionEvent e, Stage stage) throws IOException;

	void deletePlaylistEntry(ActionEvent e);

	void moveUp(ActionEvent e);

	void moveDown(ActionEvent e);

	void newPlaylist(ActionEvent e);

	void loadPlaylist(ActionEvent e);

	void savePlaylist(ActionEvent e) throws PopupException;

	
	

	
}
