package media.xuggle;

import media.AudioSamples;

import com.xuggle.xuggler.IAudioSamples;

public class AudioSamplesXuggle extends AudioSamples {
	private IAudioSamples audioSamples;
	
	public AudioSamplesXuggle(IAudioSamples audioSamples) {
		this.audioSamples = audioSamples;
		assert(audioSamples != null);
	}

	@Override
	public long getNextPresentationTimestamp() {
		return audioSamples.getNextPts();
	}

	@Override
	public void setTimeStamp(long timecode) {
		audioSamples.setTimeStamp(timecode);
	}

	@Override
	public Object getInternalAudioSamples() { return audioSamples; } //Necessary because we end with a call that needs IAudioSamples - see mediaWriterXuggle
}
