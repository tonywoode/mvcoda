package view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import playlist.Playlist;
import playlist.PlaylistEntry;

public interface ViewControllerListener {
	void onNewTrackAvailable(String name);

	PlaylistEntry addPlaylistEntry() throws IOException;

	void deletePlaylistEntry();

	void moveUp();

	void moveDown();

	void newPlaylist();

	void loadPlaylist();

	void savePlaylist() throws PopupException;

	void render();
	
	Playlist getPlaylist();

	
	

	
}
