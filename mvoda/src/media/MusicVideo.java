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

	/**
	 * get a media container that will hold packets within it of different codecs - herein usually just audio and video codecs.
	 * @return a media container which can hold data about audio, video, and other media streams
	 */
	Container getContainer();

	/**
	 * Get an audio codec, usually the one for particular stream that's been identified
	 * @return a codec typed audio codec
	 */
	StreamCoder getAudioCoder();

	/**
	 * Get a video codec, usually the one for particular stream that's been identified
	 * @return a codec typed video codec
	 */
	StreamCoder getVideoCoder(); 
	
	/**
	 * Get the pixel format of a video picture
	 * @return a particular type of pixel format from the IPixelFormat.Type ENUM
	 */
	IPixelFormat.Type getPixFormat(); //we choose to keep the type safety instead of adapt with Strings

	/**
	 * Get the fps reported for a video stream by a video codec
	 * @return a rational number representing the frames per seconds
	 */
	Rational getFramesPerSecond(); //

	/**
	 * Get the frames per second reported for a video stream by a video codec, but as a double
	 * @return a double representing fps
	 */
	double getFramesPerSecondAsDouble();
	
	/**
	 * Get the the codec type for a stream
	 * @return the codec type as an ICodec.ID ENUM
	 */
	ICodec.ID getVideoCodecID(); //we choose to keep the type safety instead of adapt with Strings

	/**
	 * Get the filepath of the file that contains the media
	 * @return String representation of a filepath as reported to the class (so could be absolute or relative)
	 */
	String getFileUNC();
	
	/**
	 * Return the filetype of the file that contains the media ie: the last three characters	
	 * @return a String representation of the last three characters of the filename
	 */
	String getFiletype();

	/**
	 * Gets the width of a video stream as reported by the codec
	 * @return the width of the stream
	 */
	int getWidth();

	/**
	 * Get the height of a video stream as reported by the codec
	 * @return the height of the the stream
	 */
	int getHeight();

	/**
	 * Gets the number of audio channels reported by the codec
	 * @return the number of channels
	 */
	int getNumChannelsAudio();

	/**
	 * Gets the index number of the audio stream in an opened container
	 * @return the index of the audio stream
	 */
	int getAudioStreamIndex();

	/**
	 * Gets the index number of the video stream in an opened container
	 * @return the index of the video stream
	 */
	int getVideoStreamIndex();

	/**
	 * Get the ID of the audio stream in an opened container
	 * @return the ID of the audio stream
	 */
	int getAudioStreamID();
	
	/**
	 * Gets the ID the video stream in an opened container
	 * @return the ID of the video stream
	 */
	int getVideoStreamID();
	
	/**
	 * Gets the video duration of a container, as reported by the container rather than the video codec
	 * @return the duration of the container
	 */
	long getContainerDuration();
	
	/**
	 * Get the divisor which will form the frame rate at the time basis
	 * @return the frame rate divisor as a long
	 */
	long getFrameRateDivisor();

	/**
	 * Get the duration of the video stream as reported by the video codec
	 * @return the video stream duration, as a long, as reported by the video codec
	 */
	long getVidStreamDuration();

	/**
	 * get the total number of video frames in the video stream as reported by the video codec
	 * @return the total number of video frames
	 */
	long getNumVidFrames();

	/**
	 * Gets the decoder associated with this music video
	 * @return the associated decoder object
	 */
	Decoder getDecoder();

	/**
	 * Close the video's underlying container
	 */
	void close();

	/**
	 * Prints out a variety of information (based on te other methods in this interface) in a formatted string
	 * @return media information
	 */
	String toString();

}