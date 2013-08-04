package playlist;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Playlist {

	@Getter private static int playlistID; 
	@Getter private ArrayList<PlaylistEntry> playlistEntries;
	@Getter @Setter String playlistName;

	public Playlist(String playlistName) {
		this.playlistName = playlistName;
		playlistID = playlistID + 1;
		playlistEntries = new ArrayList<PlaylistEntry>();
	}
	
	public int getSize() {
		return playlistEntries.size();
	}
	
	public void setNextEntry(PlaylistEntry playlistEntry) {	playlistEntries.add(playlistEntry);	}
	
	public PlaylistEntry getNextEntry(int index) { return playlistEntries.get(index); }

}
