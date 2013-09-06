package media.types;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IPacket;

public abstract class StreamCoder {

public abstract int decodeAudio(IAudioSamples audioSamples, IPacket packet,
			int offset);
		


}
