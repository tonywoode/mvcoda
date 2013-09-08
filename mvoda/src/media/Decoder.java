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

	public abstract AudioSamples getAudioSamples();

	public abstract BufferedImage getVideoFrame();

	public abstract long getVideoTimeStamp();
	
	public abstract String getFormattedVideoTimestamp();
	
	public abstract long getAudioTimeStamp();

	public abstract String getFormattedAudioTimestamp();

	public abstract void makeResampler(int outputWidth, int outputHeight);
	
	public abstract void readVideo(Packet packet) throws RuntimeException;
	
	public abstract void readAudio(Packet packet) throws RuntimeException;
	
	public abstract boolean hasNextPacket() throws RuntimeException;

}