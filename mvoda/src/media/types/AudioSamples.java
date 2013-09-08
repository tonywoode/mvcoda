package media.types;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class AudioSamples {
	
	public abstract long getNextPresentationTimestamp();
	
	public abstract void setTimeStamp(long timecode);
	
	public abstract Object getInternalAudioSamples();

	public abstract boolean isComplete();

	public abstract long getTimeStamp();

	public abstract String getFormattedTimeStamp();	
}