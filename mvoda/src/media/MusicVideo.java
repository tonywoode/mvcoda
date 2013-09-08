package media;

import media.types.Container;
import media.types.Rational;
import media.types.StreamCoder;

import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;

/**
 * Interface for the music video class which represent a video to MV-CoDA. A video should be able to quote a number of its properties
 * The interface decouples and types have been made generic, using the adapter pattern, for use in other frameworks
 * @author tony
 *
 */
public interface MusicVideo {

	public abstract Container getContainer();

	public abstract StreamCoder getAudioCoder();

	public abstract StreamCoder getVideoCoder(); 

	public abstract IPixelFormat.Type getPixFormat(); //we choose to keep the type safety instead of adapt with Strings

	public abstract Rational getFramesPerSecond(); //

	public abstract double getFramesPerSecondAsDouble();
	
	public abstract ICodec.ID getVideoCodecID(); //we choose to keep the type safety instead of adapt with Strings

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