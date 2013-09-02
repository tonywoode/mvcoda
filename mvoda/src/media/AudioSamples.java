package media;

public abstract class AudioSamples {
	public abstract long getNextPresentationTimestamp();
	public abstract void setTimeStamp(long timecode);
	public abstract Object getInternalAudioSamples();
}
