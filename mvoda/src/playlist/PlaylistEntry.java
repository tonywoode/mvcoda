package playlist;

import java.io.File;

import view.MediaOpenException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import lombok.Getter;
import lombok.Setter;
import media.MusicVideo;
import media.xuggle.MusicVideoXuggle;

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
	
	
	/**
	 * If we cannot source the video associated with this entry, we will call on the entry to validate itself
	 * @param entry
	 * @return null if the file pointed to is not valid, else returns the new playlist entry
	 */
	public PlaylistEntry validatePlaylistEntry(PlaylistEntry entry) {
		File videoFile = new File(entry.getFileUNC());
		if (videoFile.exists() ) { 
			MusicVideo video = null;
			try {
				video = new MusicVideoXuggle(entry.getFileUNC() );
			} catch (MediaOpenException e) { //We wish to catch this here so we can carry on validating. The playlist cell will highlight any null video files for the user
				System.out.println(e.getMessage());
			}
			entry.setVideo(video);
			entry.setFileUNC(video.getFileUNC());
			return entry;
		}
		return null;
	}
	
}
