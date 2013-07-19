package playlist;

import lombok.Getter;
import lombok.Setter;
import media.MusicVideo;

public class PlaylistEntry {
	
	@Setter @Getter private int positionInPlaylist;
	@Setter @Getter private String artistName;
	@Setter @Getter private String trackName;
	@Setter @Getter private MusicVideo video;

}
