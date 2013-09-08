package media.types;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.xuggle.xuggler.ICodec.ID;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class MediaWriter {

	public abstract void close();

	public abstract void encodeAudio(int audioStreamIndex, AudioSamples audioSamples);

	public abstract void encodeVideo(int i, BufferedImage videoFrame, long newVideoTimecode, TimeUnit microseconds);

	public abstract void addVideoStream(int videoStreamIndex, int videoStreamID,
			ID videoCodecID, double frameRateAsDouble, int outputWidth, //note here IRational has become double - caller may need to convert
			int outputHeight);


	public abstract void addAudioStream(int audioStreamIndex, int audioStreamID,
			ID codecId, int numAudioChannels, int audioSampleRate);
}
