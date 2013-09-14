package playlist;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import media.MediaOpenException;
import util.XMLReader;
import util.XMLSerialisable;
import util.XMLWriter;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A playlist determines which tracks form a chart, which position each is in, and accompanying text and GFX to display
 * Playlist is serialisable to and from disk - see {@link XMLReader} {@link XMLWriter}
 * @author tony
 *
 */
@XStreamAlias("Playlist") public class Playlist implements XMLSerialisable {

	@Getter private static int playlistID; 
	@Getter @Setter private ArrayList<PlaylistEntry> playlistEntries;
	@Getter @Setter String playlistName;
	@Getter @Setter String themeName;
	@Getter @Setter String fontName;
	@Getter @Setter int fontSize;
	@Getter @Setter String chartName;

	/**
	 * A playlist may have a name, for future to add to GUI
	 * @param playlistName
	 */
	public Playlist(String playlistName) {
		this.playlistName = playlistName;
		playlistID = playlistID + 1;
		playlistEntries = new ArrayList<PlaylistEntry>();
	}


	/**
	 * Returns a default name for the playlist item if one has not been set
	 */
	@Override public String getItemName() {	return (playlistName == null)? "Default MV-CoDA playlist" : playlistName; }


	/**
	 * Will validate the items in a playlist for if file exists or if they will load. For use by other classes
	 * @param playlist a playlist
	 * @return boolean indicating that at least one file was not found
	 * 
	 */
	public boolean validatePlaylist(Playlist playlist) {

		boolean found = true;
		for (int i = 0; i < playlist.getPlaylistEntries().size(); i++) {
			PlaylistEntry entry = playlist.getPlaylistEntries().get( i );
			try { entry = entry.validatePlaylistEntry(entry); } 
			//We wish to catch this here so we can carry on validating. The playlist cell will highlight any null video files for the user
			catch (MediaOpenException e) { System.out.println(e.getMessage()); }
			if (entry == null) { found = false; }
			else { entry.setPositionInPlaylist(i + 1); } //defensively re-set the playlist entry number while we have a loop	
		}
		return found;
	}

	/**
	 * Build up a string of playlist entry fileUNC's. this isn't used in the GUI so serves more as a development aid
	 */
	@Override public String toString() { 
		String entry = ""; 
		for (PlaylistEntry element : playlistEntries) {	entry = entry + "Playlist entry "  + element.getFileUNC() + "\n"; }
		return entry;
	}

}
