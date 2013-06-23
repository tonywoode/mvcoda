package media;

import java.awt.image.BufferedImage;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IPacket;

/**
 * Interface for media decoder. The decoder should open a music video container to read packets and timstamps
 * @author Tony
 *
 */
public interface Decoder {

	public static final int SIZE_AUDIO_BUFFER = 1024; //TODO: why is this hard coded? Could I just remove it?

	public abstract IAudioSamples getAudioSamples();

	public abstract BufferedImage getVideoFrame();

	public abstract long getTimeStamp();

	public abstract String getFormattedTimestamp();

	/**
	 * If the video codec of the video is not in pix format BGR24 we make it so, otherwise we aren't going to get a picture out of it
	 * TODO: investigate and really this should be capable of converting back to YUV420 surely?
	 * @param outputWidth
	 * @param outputHeight
	 */
	public abstract void makeResampler(int outputWidth, int outputHeight);

	/**
	 * When passed a video packet, will decode the packet until a single VideoPicture is decoded. 
	 * If the VideoPicture isn't in BGR24 format, we will resample it to make it so. We convert the result to a BufferedImage which is stored in the class
	 * @param packet
	 * @throws RuntimeException
	 */
	public abstract void readVideo(IPacket packet) throws RuntimeException;

	/**
	 * When passed an audio packet, will decode to audio samples and store in audioSamples field in class
	 * @param packet
	 * @throws RuntimeException
	 */
	public abstract void readAudio(IPacket packet) throws RuntimeException;

	/**
	 * Checks whether a packet passed to it is audio or video. If either is passed calls readVideo or readAudio respectively and returns true
	 * @return
	 * @throws RuntimeException
	 */
	public abstract boolean hasNextPacket() throws RuntimeException;

}