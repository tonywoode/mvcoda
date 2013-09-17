package view;

import java.io.FileNotFoundException;
import java.io.IOException;
import media.MediaOpenException;
import playlist.Playlist;
import themes.GFXElementException;

/**
 * The view controller listener functions to decouple and enable message passing between MV-CoDA's main controller and its
 * view controller. The main controller implements this interface and so calls from the view controller use this interface.
 * @author tony
 *
 */
public interface ViewControllerListener {

	/**
	 * Adds a playlist entry to the observable list in the gui
	 * @throws MediaOpenException if the video associated with the playlist entry can't be opened
	 */
	void addPlaylistEntry() throws MediaOpenException;

	/**
	 * Deletes a playlist entry from the observable list in the GUI
	 */
	void deletePlaylistEntry();

	/**
	 * Moves a playlist entry up in terms of both chart position, position in obervable list, and onscreen
	 */
	void moveUp();
	
	/**
	 * Moves a playlist entry down in terms of both chart position, position in obervable list, and onscreen
	 */
	void moveDown();

	/**
	 * Clear a playlist entry in terms of both chart position, position in obervable list, and onscreen
	 */
	void clearPlaylistEntries();

	/**
	 * Loads a new playlist from file to both observable list, and onscreen. Also validates each entry and the themes and fonts selected
	 * @throws FileNotFoundException if the XML cannot be accessed
	 * @throws IOException if there is a problem with themes
	 * @throws MediaOpenException if the media associated with a playlist entry cannot be found or read
	 */
	void loadPlaylist() throws IOException, MediaOpenException;

	/**
	 * Saves the currently displayed playlist to XML file.
	 * @throws IOException if there is a problem accessing the output files

	 */
	void savePlaylist() throws FileNotFoundException, IOException;

	/**
	 * Validates and then calls the engine to render a GFX composite output file from the selected playlist options displayed in the GUI
	 * @throws IOException a problem accessing input or output files needed for the operation
	 * @throws MediaOpenException if there are problems with any playlist entries' related music videos, or those videos do not agree on the output format
	 * @throws NullPointerException a theme has not been selected
	 * @throws GFXElementException if there is a problem with the GFX Element's files
	 */
	void render() throws IOException, MediaOpenException, NullPointerException, GFXElementException;
	
	/**
	 * returns a playlist, for use in communcation between controllers
	 * @return a playlist
	 */
	Playlist getPlaylist();

	/**
	 * Revalidates a particular Playlist Entry for it's associated media file i.e.: is it still on disk and can it still be opened by the media imp
	 * @param pos
	 * @throws MediaOpenException
	 */
	void reFindPlaylistEntry(int pos) throws MediaOpenException;
	
	/**
	 * Sets the font family name in the model
	 * @param newValue
	 */
	void setFontName(String newValue);

	/**
	 * Sets the font size to be used by the model
	 * @param intValue
	 */
	void setFontSize(int intValue);	

	
}
