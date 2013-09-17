package media.xuggle.types;

import media.types.AudioSamples;

import com.xuggle.xuggler.IAudioSamples;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public class AudioSamplesXuggle extends AudioSamples {
	
	/**
	 * Holds an internal audio samples variable
	 */
	private IAudioSamples audioSamples;
	
	/**
	 * sets the audio samples to the xuggle imp
	 * @param audioSamples
	 */
	public AudioSamplesXuggle(IAudioSamples audioSamples) {	this.audioSamples = audioSamples; }

	/**
	 * Gets the next presetnation timestamp from the xuggle imp
	 */
	@Override public long getNextPresentationTimestamp() { return audioSamples.getNextPts(); }

	/**
	 * sets the next time stamp of the audio samples in the xuggle imp
	 */
	@Override public void setTimeStamp(long timecode) { audioSamples.setTimeStamp(timecode); }

	/**
	 * gets the actual audio sample object from the xuggle imp
	 */
	@Override public Object getInternalAudioSamples() { return audioSamples; } //Necessary because we end with a call that needs IAudioSamples - see mediaWriterXuggle

	/**
	 * Returns from the xuggle imp whether the audio samples is complete
	 */
	@Override public boolean isComplete() {	return audioSamples.isComplete(); }

	/**
	 * gets the timestamp from the xuggle imp for the audio samples
	 */
	@Override public long getTimeStamp() { return audioSamples.getTimeStamp(); }

	/**
	 * gets the timestamp (formatted inhuman readable form) from the xuggle imp for the audio samples
	 */
	@Override public String getFormattedTimeStamp() { return audioSamples.getFormattedTimeStamp(); }
}
