package media.types;

import com.xuggle.xuggler.ICodec.ID;

public abstract class StreamCoder {

  public abstract int decodeAudio(AudioSamples audioSamples, Packet packet, int offset);

  public abstract int decodeVideo(VideoPicture picture, Packet packet, int offset);

  public abstract Object getInternalCoder();

public abstract int getChannels();

public abstract int getSampleRate();

public abstract ID getCodecID();


}
