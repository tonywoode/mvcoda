package playlist;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import themes.XMLReader;
import themes.XMLSerialisable;
import themes.XMLWriter;
import view.MediaOpenException;

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

	public int getSize() { return playlistEntries.size(); }

	public void setNextEntry(PlaylistEntry playlistEntry) {	playlistEntries.add(playlistEntry);	}

	public PlaylistEntry getNextEntry(int index) { return playlistEntries.get(index); }


	public void resetArray(ObservableList<PlaylistEntry> playlistObservable) {
		ArrayList<PlaylistEntry> temp = new ArrayList<>(); //because if we try to directly do this to playlistEntries we'll get the concurrentModificationError
		temp.addAll(playlistObservable);
		playlistEntries = temp;
	}

	@Override public String getItemName() {	return (playlistName == null)? "Default Playlist Name" : playlistName;
	}


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
			try {
				entry = entry.validatePlaylistEntry(entry);
			} catch (MediaOpenException e) { //We wish to catch this here so we can carry on validating. The playlist cell will highlight any null video files for the user
				System.out.println(e.getMessage());
			}
			
			if (entry == null) {
				found = false;
			}
			else { entry.setPositionInPlaylist(i + 1); } //defensively re-set the playlist entry number while we have a loop	
		}
		return found;
	}


	@Override public String toString() { 
		String entry = ""; 
		for (PlaylistEntry element : playlistEntries) {	entry = entry + "Playlist entry "  + element.getFileUNC() + "\n"; }
		return entry;
	}

}
