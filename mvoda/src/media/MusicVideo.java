package media;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.xuggle.xuggler.ICodec;
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
	
	public abstract ICodec.ID getVideoCodecID();

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