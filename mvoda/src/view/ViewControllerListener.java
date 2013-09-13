package view;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.management.modelmbean.XMLParseException;

import playlist.Playlist;

public interface ViewControllerListener {

	void addPlaylistEntry() throws IOException, MediaOpenException;

	void deletePlaylistEntry();

	void moveUp();

	void moveDown();

	void clearPlaylistEntries();

	void loadPlaylist() throws FileNotFoundException, IOException, XMLParseException, MediaOpenException, InterruptedException;

	void savePlaylist() throws FileNotFoundException, IOException;

	void render() throws IOException, MediaOpenException, NullPointerException, GFXElementException;
	
	Playlist getPlaylist();

	void reFindPlaylistEntry(int pos) throws MediaOpenException;



	

	
	

	
}
