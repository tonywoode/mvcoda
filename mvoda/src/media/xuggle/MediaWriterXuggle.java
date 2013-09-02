package media.xuggle;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.xuggler.IAudioSamples;

import media.AudioSamples;
import media.MediaWriter;

public class MediaWriterXuggle extends MediaWriter {

	private IMediaWriter writer;
	
	public MediaWriterXuggle(IMediaWriter writer) {
		this.writer = writer;
	}

	@Override
	public void close() {
		writer.close();
	}

	@Override
	public void encodeAudio(int audioStreamIndex, AudioSamples audioSamples) {
		writer.encodeAudio(audioStreamIndex, (IAudioSamples)audioSamples.getInternalAudioSamples());
	}

	@Override
	public void encodeVideo(int i, BufferedImage videoFrame, long newVideoTimecode, TimeUnit microseconds) {
		writer.encodeVideo(i, videoFrame, newVideoTimecode, microseconds);
	}
	
	
	
}
