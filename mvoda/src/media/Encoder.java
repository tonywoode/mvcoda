package media;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.xuggler.IStreamCoder;

public interface Encoder {
	
	public abstract void render();
	
	public abstract IMediaWriter getWriter(String filename);
	
	public abstract void addVideoStreamTo(IMediaWriter writer);

	public abstract void addAudioStreamTo(IMediaWriter writer,
			IStreamCoder audioCodec);

}