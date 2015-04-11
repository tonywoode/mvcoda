package media.types;

import com.xuggle.xuggler.ICodec.ID;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class StreamCoder {


	public abstract int decodeAudio(AudioSamples audioSamples, Packet packet, int offset);

	public abstract int decodeVideo(VideoPicture picture, Packet packet, int offset);

	/**
	 * Get the stream codec as an object of the type specified by the media implementation
	 * @return an object containing the codec of the type specified by the media implementation
	 */
	public abstract Object getInternalCoder();

	/**
	 * gets the number of channels reported by the codec
	 * @return the number of channels
	 */
	public abstract int getChannels();

	/**
	 * Gets the sample rate reported by the codec
	 * @return the sample rate
	 */
	public abstract int getSampleRate();

	/**
	 * Get the ID of the codec within the container
	 * @return the ID of ID's types this codec matches
	 */
	public abstract ID getCodecID();


}
