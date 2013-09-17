package media.types;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.xuggle.xuggler.ICodec.ID;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class MediaWriter {

	/**
	 * Closes the mediawriter
	 */
	public abstract void close();

	/**
	 * Encode audio samples to a container
	 * @param audioStreamIndex the stream index of the audio stream
	 * @param audioSamples the audio samples to encode
	 */
	public abstract void encodeAudio(int audioStreamIndex, AudioSamples audioSamples);

	/**
	 * Encode video samples to a container
	 * @param i
	 * @param videoFrame the video frame to encode
	 * @param newVideoTimecode the timecode to encode
	 * @param microseconds the time basis
	 */
	public abstract void encodeVideo(int i, BufferedImage videoFrame, long newVideoTimecode, TimeUnit microseconds);

	/**
	 * Add a video stream to a container
	 * @param videoStreamIndex the index the stream will have in the container
	 * @param videoStreamID the ID the stream will have in the container
	 * @param videoCodecID the codec's ID
	 * @param frameRateAsDouble the frame rate as a double
	 * @param outputWidth the width the video will have
	 * @param outputHeight the height the video will have
	 */
	public abstract void addVideoStream(int videoStreamIndex, int videoStreamID,
			ID videoCodecID, double frameRateAsDouble, int outputWidth, //note here IRational has become double - caller may need to convert
			int outputHeight);


	public abstract void addAudioStream(int audioStreamIndex, int audioStreamID,
			ID codecId, int numAudioChannels, int audioSampleRate);
}
