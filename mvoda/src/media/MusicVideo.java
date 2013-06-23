package media;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStreamCoder;

public interface MusicVideo {

	public abstract IContainer getContainer();

	public abstract IStreamCoder getAudioCoder();

	public abstract IStreamCoder getVideoCoder();

	public abstract IPixelFormat.Type getPixFormat();

	public abstract IRational getFramesPerSecond();

	public abstract double getFramesPerSecondAsDouble();

	public abstract String getFileUNC();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract int getNumChannelsAudio();

	public abstract int getAudioStreamId();

	public abstract int getVideoStreamId();

	public abstract long getContainerDuration();

	public abstract long getVidStreamDuration();

	public abstract long getNumVidFrames();

	public abstract Decoder getDecoder();

	/**
	 * Closes the container that represents the music video
	 */
	public abstract void close();

	public abstract String toString();

}