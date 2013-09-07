package media.types;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.xuggle.xuggler.ICodec.ID;



public abstract class MediaWriter {

	public abstract void close();

	public abstract void encodeAudio(int audioStreamIndex, AudioSamples audioSamples);

	public abstract void encodeVideo(int i, BufferedImage videoFrame, long newVideoTimecode, TimeUnit microseconds);

	public abstract void addVideoStream(int videoStreamIndex, int videoStreamID,
			ID videoCodecID, Rational frameRate, int outputWidth,
			int outputHeight);

	public abstract void addAudioStream(int audioStreamIndex, int audioStreamID,
			ID codecId, int numAudioChannels, int audioSampleRate);
}
