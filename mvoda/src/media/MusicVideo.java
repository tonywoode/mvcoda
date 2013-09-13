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

	Container getContainer();

	StreamCoder getAudioCoder();

	StreamCoder getVideoCoder(); 

	IPixelFormat.Type getPixFormat(); //we choose to keep the type safety instead of adapt with Strings

	Rational getFramesPerSecond(); //

	double getFramesPerSecondAsDouble();
	
	ICodec.ID getVideoCodecID(); //we choose to keep the type safety instead of adapt with Strings

	String getFileUNC();
	
	String getFiletype();

	int getWidth();

	int getHeight();

	int getNumChannelsAudio();

	int getAudioStreamIndex();

	int getVideoStreamIndex();

	int getAudioStreamID();
	
	int getVideoStreamID();
	
	long getContainerDuration();
	
	long getFrameRateDivisor();

	long getVidStreamDuration();

	long getNumVidFrames();

	Decoder getDecoder();

	void close();

	String toString();

}