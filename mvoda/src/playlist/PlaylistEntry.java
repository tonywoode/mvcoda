package playlist;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import lombok.Getter;
import lombok.Setter;
import media.MusicVideo;

/**
 * A playlist entry describes a position in a music video chart for Playlist. So what video it is, and what text to display onscreen for that video
 * @author tony
 *
 */
@XStreamAlias("PlaylistEntry") public class PlaylistEntry {
	
	@Setter @Getter private int positionInPlaylist;
	@Setter @Getter private String artistName;
	@Setter @Getter private String trackName;
	@XStreamOmitField @Setter @Getter private MusicVideo video;
	@Setter @Getter String fileUNC;
	
	public PlaylistEntry(MusicVideo video) { this.video = video; fileUNC = video.getFileUNC(); }
	
	public PlaylistEntry(MusicVideo video, String trackName, String artistName) { 
		this.video = video; 
		this.trackName = trackName;
		this.artistName = artistName;
		fileUNC = video.getFileUNC();
	}
	
}
