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

	AudioSamples getAudioSamples();

	BufferedImage getVideoFrame();

	long getVideoTimeStamp();
	
	String getFormattedVideoTimestamp();
	
	long getAudioTimeStamp();

	String getFormattedAudioTimestamp();

	void makeResampler(int outputWidth, int outputHeight);
	
	void readVideo(Packet packet) throws RuntimeException;
	
	void readAudio(Packet packet) throws RuntimeException;
	
	boolean hasNextPacket() throws RuntimeException;

}