package media.xuggle.types;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec.ID;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;

import media.types.AudioSamples;
import media.types.Packet;
import media.types.StreamCoder;
import media.types.VideoPicture;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public class StreamCoderXuggle extends StreamCoder {
	
	/**
	 * Holds a reference to a xuggle stream codec
	 */
	private IStreamCoder streamCoder;
	
	/**
	 * returns a xuggle stream codec as a stream coder
	 * @param streamCoder the stream coder
	 */
	public StreamCoderXuggle(IStreamCoder streamCoder) { this.streamCoder = streamCoder; }
	
	/**
	 * decodes audio using the xuggle stream codec
	 */
	@Override public int decodeAudio(AudioSamples audioSamples, Packet packet, int offset) {
		IAudioSamples xuggleAudioSamples = (IAudioSamples)audioSamples.getInternalAudioSamples();
		IPacket xugglePacket = (IPacket)packet.getInternalPacket();
		
		return streamCoder.decodeAudio(xuggleAudioSamples, xugglePacket, offset);
	}

	/**
	 * decodes video using the xuggle stream codec
	 */
	@Override public int decodeVideo(VideoPicture picture, Packet packet, int offset) {
		IVideoPicture xuggleVideoPicture = (IVideoPicture)picture.getInternalVideoPicture();
		IPacket xugglePacket = (IPacket)packet.getInternalPacket();
		
		return streamCoder.decodeVideo(xuggleVideoPicture, xugglePacket, offset);
	}

	/**
	 * Returns the codec in xuggle native format which is IStreamCoder and needs to be cast as such
	 */
	@Override public Object getInternalCoder() { return streamCoder; }

	/**
	 * Gets the number of channels from xuggle as reported for this codec
	 */
	@Override public int getChannels() { return streamCoder.getChannels(); }

	/**
	 * Gets the sample rate from xuggle as reported for this codec
	 */
	@Override public int getSampleRate() { return streamCoder.getSampleRate(); }

	/**
	 * Gets the codec ID for this codec as codec ID type
	 */
	@Override public ID getCodecID() { return streamCoder.getCodecID();	}

}
