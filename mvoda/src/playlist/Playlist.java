package playlist;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import themes.XMLSerialisable;

import javafx.collections.ObservableList;

import lombok.Getter;
import lombok.Setter;

/**
 * A playlist determines which tracks form a chart, which position each is in, and acoompanying text and GFX to display
 * Playlist is serialisable to and from disk
 * @author tony
 *
 */
@XStreamAlias("Playlist") public class Playlist implements XMLSerialisable {

	@Getter private static int playlistID; 
	@Getter @Setter private ArrayList<PlaylistEntry> playlistEntries;
	@Getter @Setter String playlistName;
	@Getter @Setter String themeName;

	/**
	 * A playlist must have a name //TODO: name for playlist?
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
		playlistEntries = playlistEntries; //just so we can set the breakpoint here
	}

	@Override public String getItemName() {	return (playlistName == null)? "Default Playlist Name" : playlistName; //TODO: is this really best? What to do about playlist name - this is for xml serialisable so should be the filename we set really
	}

	@Override public String toString() { 
		String entry = ""; //TODO: are you going to have a playlist name or not? if yes that's what should return from tostring....
		for (PlaylistEntry element : playlistEntries) {	entry = entry + "Playlist entry in the real playlist " + element.getFileUNC() + "\n"; }
		return entry;
	}

}
