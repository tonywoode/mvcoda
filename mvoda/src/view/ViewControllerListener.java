package view;

import java.io.FileNotFoundException;
import java.io.IOException;
import media.MediaOpenException;
import playlist.Playlist;
import themes.GFXElementException;

public interface ViewControllerListener {

	void addPlaylistEntry() throws IOException, MediaOpenException;

	void deletePlaylistEntry();

	void moveUp();

	void moveDown();

	void clearPlaylistEntries();

	void loadPlaylist() throws FileNotFoundException, IOException, MediaOpenException;

	void savePlaylist() throws FileNotFoundException, IOException;

	void render() throws IOException, MediaOpenException, NullPointerException, GFXElementException;
	
	Playlist getPlaylist();

	void reFindPlaylistEntry(int pos) throws MediaOpenException;
	
	void setFontName(String newValue);

	void setFontSize(int intValue);	

	
}
