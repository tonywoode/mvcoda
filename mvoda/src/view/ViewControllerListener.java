package view;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.management.modelmbean.XMLParseException;

import playlist.Playlist;
import playlist.PlaylistEntry;

public interface ViewControllerListener {

	PlaylistEntry addPlaylistEntry() throws IOException, MediaOpenException;

	void deletePlaylistEntry();

	void moveUp();

	void moveDown();

	void newPlaylist();

	void loadPlaylist() throws FileNotFoundException, IOException, XMLParseException, MediaOpenException, InterruptedException;

	void savePlaylist() throws FileNotFoundException, IOException;

	void render() throws IOException, XMLParseException;
	
	Playlist getPlaylist();

	void reFindPlaylistEntry(int pos) throws MediaOpenException;

	
	

	
}
