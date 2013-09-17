package media;

import java.awt.image.BufferedImage;

import media.types.AudioSamples;
import media.types.Packet;

/**
 * Interface for media decoder. The decoder should open a music video container to read packets and timestamps
 * The interface decouples and all types have been made generic, using the adapter pattern, for use in other frameworks
 * @author Tony
 *
 */
public interface Decoder {

	/**
	 * Get the audio samples associated with a particular audio packet
	 * @return the audio samples from the packer
	 */
	AudioSamples getAudioSamples();

	/**
	 * Gets the video frame associated with part of a series of video packets
	 * @return a buffered image of the video frame
	 */
	BufferedImage getVideoFrame();

	/**
	 * Gets the video time stamp at the current position decoded
	 * @return the video timestamp
	 */
	long getVideoTimeStamp();

	/**
	 * Gets the video time stamp at the current postition decoded, but formatted in human readable hrs, mins, secs, millis
	 * @return a string representing the current timestamp in human-readable form
	 */
	String getFormattedVideoTimestamp();

	/**
	 * Gets the audio time stamp at the current postition decoded
	 * @return the currentaudio  timestamp
	 */
	long getAudioTimeStamp();

	/**
	 * Gets the audio time stamp at the current postition decoded, but formatted in human readable hrs, mins, secs, millis
	 * @return a string representing the current audio timestamp in human-readable form
	 */
	String getFormattedAudioTimestamp();

	/**
	 * Create a new resampler to change the pixel type of an image
	 * @param outputWidth the width the new image is to be
	 * @param outputHeight the height the new image is to be
	 */
	void makeResampler(int outputWidth, int outputHeight);

	/**
	 * reads a video packet from a container
	 * @param packet the video packet
	 * @throws RuntimeException if the packet cannot be read
	 */
	void readVideo(Packet packet) throws RuntimeException;

	/**
	 * reads an audio packet from a container
	 * @param packet the audio packet
	 * @throws RuntimeException if the packet cannot be read
	 */
	void readAudio(Packet packet) throws RuntimeException;

	/**
	 * Returns true if the stream being read has another packet to be decoded
	 * @return true if there are more packets, false otherwise
	 * @throws RuntimeException if the status of the next packet cannot be ascertained
	 */
	boolean hasNextPacket() throws RuntimeException;

}