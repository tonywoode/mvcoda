package media;

import media.types.MediaWriter;
import media.types.StreamCoder;
import playlist.Playlist;

/**
 * Interface for the media emcoder. The encoder should be able to creat a writing container and add audio and video streams to render a playlist
 * The interface decouples and all types have been made generic, using the adapter pattern, for use in other frameworks
 * @author tony
 *
 */
public interface Encoder {
	
	public abstract void render(Playlist playlist);
	
	public abstract MediaWriter getWriter(String filename);
	
	public abstract void addVideoStreamTo(MediaWriter writer);

	public abstract void addAudioStreamTo(MediaWriter writer, StreamCoder audioCodec);

}