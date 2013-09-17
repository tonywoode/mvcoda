package media.types;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class AudioSamples {
	
	/**
	 * Get the next timestamp returned from the packet
	 * @return the next timestamp
	 */
	public abstract long getNextPresentationTimestamp();
	
	/**
	 * Set the timestamp to write for a packet when encoding
	 * @param timecode the timecode to write
	 */
	public abstract void setTimeStamp(long timecode);
	
	/**
	 * Gets the audio samples as presented by the underlying media implementation
	 * @return an object of the underlying media implementations audio samples type
	 */
	public abstract Object getInternalAudioSamples();

	/**
	 * returns true when the audio sample has been fully read
	 * @return true when read
	 */
	public abstract boolean isComplete();

	/**
	 * Gets the timestamp that has been set for these audio samples 
	 * @return the timestamp set
	 */
	public abstract long getTimeStamp();

	/**
	 * Gets the timestamp that has been set for these audio samples in human readable format
	 * @return the timestamp set in human readable format
	 */
	public abstract String getFormattedTimeStamp();	
}