package media;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.xuggler.IStreamCoder;

public interface Encoder {

	/**
	 * Creates a new music video with input filename and a new writer that will write to output filename, iterates through the packets of the music video
	 * encoding both, but allowing something to happen to the buffered images before the encode
	 * @param filename
	 */
	public abstract void render(String filename);

	/**
	 * This is called by render(). It makes a new writer from the tool factory, adds a video and audio stream to it, and returns it
	 * @param filename
	 * @return
	 */
	public abstract IMediaWriter getWriter(String filename);

	/**
	 * This is called by getWriter(). It adds the video stream to the MediWriter you pass in i.e.: so its ready for writing out 
	 * At the time rate and using the codec the class specifies
	 * @param writer
	 */
	public abstract void addVideoStreamTo(IMediaWriter writer);

	/**
	 * This is called by getWriter(). It adds the audio stream to the MediWriter you pass in i.e.: so its ready for writing out 
	 * using the codec that get's passed to it. At the time rate and using the codec the class specifies
	 * @param writer
	 * @param audioCodec
	 */
	public abstract void addAudioStreamTo(IMediaWriter writer,
			IStreamCoder audioCodec);

}