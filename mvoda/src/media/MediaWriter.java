package media;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public abstract class MediaWriter {

	public abstract void close();

	public abstract void encodeAudio(int audioStreamIndex, AudioSamples audioSamples);

	public abstract void encodeVideo(int i, BufferedImage videoFrame, long newVideoTimecode, TimeUnit microseconds);
}
