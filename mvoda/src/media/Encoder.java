package media;

import media.types.MediaWriter;
import playlist.Playlist;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.xuggler.IStreamCoder;

public interface Encoder {
	
	public abstract void render(Playlist playlist);
	
	public abstract MediaWriter getWriter(String filename);
	
	public abstract void addVideoStreamTo(IMediaWriter writer);

	public abstract void addAudioStreamTo(IMediaWriter writer,
			IStreamCoder audioCodec);

}