package playlist;

import java.util.ArrayList;
import java.util.List;

import themes.XMLSerialisable;

import javafx.collections.ObservableList;

import lombok.Getter;
import lombok.Setter;

public class Playlist implements XMLSerialisable {

	@Getter private static int playlistID; 
	@Getter @Setter private ArrayList<PlaylistEntry> playlistEntries;
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

	public void resetArray(ObservableList<PlaylistEntry> playlistObservable) {
		//playlistEntries.clear();
		//playlistEntries.addAll(videosObservable);
		ArrayList<PlaylistEntry> temp = new ArrayList<>(); //because if we try to directly do this to playlistEntries we'll get the concurrentModificationError
		temp.addAll(playlistObservable);
		playlistEntries = temp;
		playlistEntries = playlistEntries; //just so we can set the breakpoint here
		//System.out.println(playlistEntries.get(4).getVideo().getFileUNC()); //yup it look like we really are deleting
	}

	@Override
	public String getItemName() {
		return (playlistName == null)? "Default Playlist Name" : playlistName; //TODO: is this really best? What to do about playlist name - this is for xml serialisable so should be the filename we set really
	}

}
