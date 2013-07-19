package playlist;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Playlist {

	@Getter private static int playlistID; 
	@Getter private ArrayList<PlaylistEntry> playlistEntries;
	@Getter @Setter String playlistName;

	public Playlist() {
		playlistID = playlistID + 1;
	}

}
