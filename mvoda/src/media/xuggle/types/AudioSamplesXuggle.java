package media.xuggle.types;

import media.types.AudioSamples;

import com.xuggle.xuggler.IAudioSamples;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public class AudioSamplesXuggle extends AudioSamples {
	
	private IAudioSamples audioSamples;
	
	public AudioSamplesXuggle(IAudioSamples audioSamples) {
		this.audioSamples = audioSamples;
		assert(audioSamples != null);
	}

	@Override public long getNextPresentationTimestamp() { return audioSamples.getNextPts(); }

	@Override public void setTimeStamp(long timecode) { audioSamples.setTimeStamp(timecode); }

	@Override public Object getInternalAudioSamples() { return audioSamples; } //Necessary because we end with a call that needs IAudioSamples - see mediaWriterXuggle

	@Override public boolean isComplete() {	return audioSamples.isComplete(); }

	@Override public long getTimeStamp() { return audioSamples.getTimeStamp(); }

	@Override public String getFormattedTimeStamp() { return audioSamples.getFormattedTimeStamp(); }
}
