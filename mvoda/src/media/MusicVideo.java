package media;

import media.types.Container;
import media.types.Rational;
import media.types.StreamCoder;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;

public interface MusicVideo {

	public abstract Container getContainer(); //TODO: nearly got this one but a static?

	public abstract StreamCoder getAudioCoder(); //didn't get too far there but not many

	public abstract StreamCoder getVideoCoder(); //do the above you got this one too

	public abstract IPixelFormat.Type getPixFormat(); //appears to be some way to just go "type" right?

	public abstract Rational getFramesPerSecond(); //JUST ONE CALL...but don't know how....

	public abstract double getFramesPerSecondAsDouble();
	
	public abstract ICodec.ID getVideoCodecID(); //just 3 references.....

	public abstract String getFileUNC();
	
	public abstract String getFiletype();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract int getNumChannelsAudio();

	public abstract int getAudioStreamIndex();

	public abstract int getVideoStreamIndex();

	public abstract int getAudioStreamID();
	
	public abstract int getVideoStreamID();
	
	public abstract long getContainerDuration();
	
	public abstract long getFrameRateDivisor();

	public abstract long getVidStreamDuration();

	public abstract long getNumVidFrames();

	public abstract Decoder getDecoder();

	public abstract void close();

	public abstract String toString();

}