package media.types;

public abstract class StreamCoder {

  public abstract int decodeAudio(AudioSamples audioSamples, Packet packet, int offset);

  public abstract int decodeVideo(VideoPicture picture, Packet packet, int offset);

  public abstract Object getInternalCoder();
}
