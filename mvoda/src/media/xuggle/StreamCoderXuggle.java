package media.xuggle;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStreamCoder;

import media.StreamCoder;

public class StreamCoderXuggle extends StreamCoder {
private IStreamCoder streamCoder;

public StreamCoderXuggle(IStreamCoder streamCoder) {
	this.streamCoder = streamCoder;
}

@Override
public int decodeAudio(IAudioSamples audioSamples, IPacket packet, int offset) {
	return streamCoder.decodeAudio(audioSamples, packet, offset);
}

}
