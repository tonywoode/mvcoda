package playlist;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import lombok.Getter;
import lombok.Setter;
import media.MusicVideo;

@XStreamAlias("PlaylistEntry")
public class PlaylistEntry {
	
	@Setter @Getter private int positionInPlaylist;
	@Setter @Getter private String artistName;
	@Setter @Getter private String trackName;
	@Setter @Getter private MusicVideo video;
	@Setter @Getter String fileUNC;
	
	public PlaylistEntry(MusicVideo video) { this.video = video; }
	
	public PlaylistEntry(MusicVideo video, String trackName, String artistName) { 
		this.video = video; 
		this.trackName = trackName;
		this.artistName = artistName;
		fileUNC = video.getFileUNC();
	}
	
	
}
