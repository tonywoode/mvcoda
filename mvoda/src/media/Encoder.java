package media;

import media.types.MediaWriter;
import media.types.StreamCoder;
import playlist.Playlist;

public interface Encoder {
	
	public abstract void render(Playlist playlist);
	
	public abstract MediaWriter getWriter(String filename);
	
	public abstract void addVideoStreamTo(MediaWriter writer);

	public abstract void addAudioStreamTo(MediaWriter writer,
			StreamCoder audioCodec);

}