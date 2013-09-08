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

  public abstract Object getInternalCoder();

public abstract int getChannels();

public abstract int getSampleRate();

public abstract ID getCodecID();


}
