package playlist;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;

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
	
	public void setNextEntry(PlaylistEntry playlistEntry) {	
		playlistEntries.add(playlistEntry);	
		//playlistEntry.setPositionInPlaylist(playlistEntries.indexOf(playlistEntry) + 1); //TODO: that only gets you first occurance
	}
	
	public PlaylistEntry getNextEntry(int index) { return playlistEntries.get(index); }
	
	/*public void clearArray() {
		playlistEntries.clear();
	}*/

	public void resetArray(ObservableList<PlaylistEntry> videosObservable) {
		//playlistEntries.clear();
		//playlistEntries.addAll(videosObservable);
		ArrayList<PlaylistEntry> temp = new ArrayList<>(); //because if we try to directly do this to playlistEntries we'll get the concurrentModificationError
		temp.addAll(videosObservable);
		playlistEntries = temp;
		playlistEntries = playlistEntries; //just so we can set the breakpoint here
	}

}
