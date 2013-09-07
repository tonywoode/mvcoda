package media.xuggle.types;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec.ID;
import com.xuggle.xuggler.IRational;

import media.types.AudioSamples;
import media.types.MediaWriter;
import media.types.Rational;

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
		//In the below call it was necessary to cast this and put this method in the decoder interface because we have to call a method in xuggle that needs IAudioSamples 
		writer.encodeAudio(audioStreamIndex, (IAudioSamples)audioSamples.getInternalAudioSamples());
	}

	@Override
	public void encodeVideo(int i, BufferedImage videoFrame, long newVideoTimecode, TimeUnit microseconds) {
		writer.encodeVideo(i, videoFrame, newVideoTimecode, microseconds);
	}

	@Override
	public void addVideoStream(int videoStreamIndex, int videoStreamID,
			ID videoCodecID, Rational frameRate, int outputWidth,
			int outputHeight) {
		writer.addVideoStream(videoStreamIndex, videoStreamID,
				videoCodecID, outputWidth,
				outputHeight);
		
	}

	@Override
	public void addAudioStream(int audioStreamIndex, int audioStreamID,
			ID codecId, int numAudioChannels, int audioSampleRate) {
		writer.addAudioStream(audioStreamIndex, audioStreamID,
				codecId, numAudioChannels, audioSampleRate);
		
	}
	
	
	
}
