package playlist;

import lombok.Getter;
import lombok.Setter;
import media.MusicVideo;

public class PlaylistEntry {
	
	@Setter @Getter private int positionInPlaylist;
	@Setter @Getter private String artistName;
	@Setter @Getter private String trackName;
	@Setter @Getter private MusicVideo video;
	
	public PlaylistEntry(MusicVideo video) { this.video = video; }
	
	public PlaylistEntry(MusicVideo video, String trackName, String artistName) { 
		this.video = video; 
		this.trackName = trackName;
		this.artistName = artistName;
	}
}
