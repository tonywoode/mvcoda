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
	@Setter @Getter private String trackInfo;
	@Setter @Getter private String fileUNC;
	
	@XStreamOmitField @Setter @Getter private MusicVideo video;
	
	
	/**
	 * We allow a contructor with just a Music Video, from which we determine the filepath
	 * @param video the music video
	 */
	public PlaylistEntry(MusicVideo video) { this.video = video; fileUNC = video.getFileUNC(); }
	
	/**
	 * We allow a constructor with full details for setting a full entry
	 * @param video the music video
	 * @param trackName the text track name given to the item
	 * @param artistName the text artist name given to the item
	 * @param trackInfo the text track information given to the item
	 */
	public PlaylistEntry(MusicVideo video, String trackName, String artistName, String trackInfo) { 
		this.video = video; 
		this.trackName = trackName;
		this.artistName = artistName;
		this.trackInfo = trackInfo;
		fileUNC = video.getFileUNC();
	}
	
	
	/**
	 * If we cannot source the video associated with this entry, we will call on the entry to validate itself
	 * @param entry
	 * @return null if the file pointed to is not valid, else returns the new playlist entry
	 * @throws MediaOpenException 
	 */
	public PlaylistEntry validatePlaylistEntry(PlaylistEntry entry) throws MediaOpenException {
		File videoFile = new File(entry.getFileUNC());
		if (videoFile.exists() ) { 
			MusicVideo video = null;
			video = new MusicVideoXuggle(entry.getFileUNC() );	
			entry.setVideo(video);
			entry.setFileUNC(video.getFileUNC());
			return entry;
		}
		return null;
	}
	
}
