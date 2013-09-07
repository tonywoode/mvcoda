package media.xuggle.types;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;

import media.types.AudioSamples;
import media.types.Packet;
import media.types.StreamCoder;
import media.types.VideoPicture;

public class StreamCoderXuggle extends StreamCoder {
	private IStreamCoder streamCoder;
	
	public StreamCoderXuggle(IStreamCoder streamCoder) {
		this.streamCoder = streamCoder;
	}
	
	@Override
	public int decodeAudio(AudioSamples audioSamples, Packet packet, int offset) {
		IAudioSamples xuggleAudioSamples = (IAudioSamples)audioSamples.getInternalAudioSamples();
		IPacket xugglePacket = (IPacket)packet.getInternalPacket();
		
		return streamCoder.decodeAudio(xuggleAudioSamples, xugglePacket, offset);
	}

	@Override
	public int decodeVideo(VideoPicture picture, Packet packet, int offset) {
		IVideoPicture xuggleVideoPicture = (IVideoPicture)picture.getInternalVideoPicture();
		IPacket xugglePacket = (IPacket)packet.getInternalPacket();
		
		return streamCoder.decodeVideo(xuggleVideoPicture, xugglePacket, offset);
	}

	@Override
	public Object getInternalCoder() {
		return streamCoder;
	}

}
