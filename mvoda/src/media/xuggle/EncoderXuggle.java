package media.xuggle;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import media.Decoder;
import media.Encoder;
import media.MusicVideo;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStreamCoder;

import drawing.ImageCompositor;

/**
 * Basic methods to deal with buffered images we get from music video packets so that we can manipulate them and recode a video. Its basically "do something then encode"
 * @author Tony
 *
 */
public class EncoderXuggle implements Encoder {

	private static final int VIDEO_STREAM_INDEX = 0;
	private static final int AUDIO_STREAM_INDEX = 1;	//TODO: why are these things all hard coded?
	private static final int VIDEO_STREAM_ID = 0;
	private static final int AUDIO_STREAM_ID = 0;
	private static final ICodec.ID VIDEO_CODEC = ICodec.ID.CODEC_ID_MPEG4;
	private MusicVideo video;
	private String outFilename;
	
	private Decoder decoder;

	/**
	 * By passing two UNCpaths to the constructor we specify and input and an output filename
	 * @param filename
	 * @param outFilename
	 */
	public EncoderXuggle(final String filename,String outFilename) {
		this.outFilename = outFilename;
		render(filename);
	}

	/**
	 * Creates a new music video with input filename and a new writer that will write to output filename, iterates through the packets of the music video
	 * encoding both, but allowing something to happen to the buffered images before the encode
	 * @param filename
	 */
	@Override
	public void render(String filename) { //TODO: hang on the filename is instantiated with new, why do we need this in the sig? Or should these be static methods - should the class be a static class?

		IMediaWriter writer = null;
		try {
			video = new MusicVideoXuggle(filename);
			decoder = video.getDecoder();
			writer = getWriter(outFilename);
			long frame = 0;
			long lastFrame = video.getNumVidFrames();
			while (decoder.hasNextPacket()) {
				if (decoder.getVideoFrame() != null) {frame++;} // don't increase counter if not a video frame

				IAudioSamples audioSamples = decoder.getAudioSamples();
				if (audioSamples != null) {
					writer.encodeAudio(AUDIO_STREAM_INDEX, audioSamples);
				}
				BufferedImage videoFrame = decoder.getVideoFrame();
				if (videoFrame != null) {			
					System.out.println("at video timestamp: " + decoder.getFormattedTimestamp());
					//ShowImageInFrame im = new ShowImageInFrame(videoFrame); //yup we are getting images....
					String overlayFile = "../../../Repo/mvoda/mvoda/Theme/Pop/Logo/4MLogoFrames/4M68.png";
					Image over = ImageIO.read(new File(overlayFile));
					BufferedImage overlay = (BufferedImage) over;
					ImageCompositor overlayframes = new ImageCompositor(videoFrame, overlay);
					BufferedImage composite = overlayframes.overlay();
					
					writer.encodeVideo(0, composite, decoder.getTimeStamp(), TimeUnit.MILLISECONDS);
					if ((frame +1) >= lastFrame) break;
				}
			}

		} catch (Exception ex) { //TODO: what ANY exception? Why aren't we saying we throw any then?
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (RuntimeException ex) { //TODO: only a runtime?!?!
				}
			}
			if (video != null) video.close();
		}
	}

	/**
	 * This is called by render(). It makes a new writer from the tool factory, adds a video and audio stream to it, and returns it
	 * @param filename
	 */
	@Override
	public IMediaWriter getWriter(String filename) {
		IMediaWriter writer = ToolFactory.makeWriter(filename);
		addVideoStreamTo(writer);
		IStreamCoder audioCodec = video.getAudioCoder();
		if (audioCodec != null) {addAudioStreamTo(writer, audioCodec);}
		return writer;
	}

	/**
	 * This is called by getWriter(). It adds the video stream to the MediWriter you pass in i.e.: so its ready for writing out 
	 * At the time rate and using the codec the class specifies
	 * @param writer
	 */
	@Override
	public void addVideoStreamTo(IMediaWriter writer) {
		IRational frameRate = IRational.make(video.getFramesPerSecondAsDouble());
		int outputWidth = video.getWidth();
		int outputHeight = video.getHeight();
		writer.addVideoStream(VIDEO_STREAM_INDEX,VIDEO_STREAM_ID,VIDEO_CODEC,frameRate,outputWidth,outputHeight);
	}

	/**
	 * This is called by getWriter(). It adds the audio stream to the MediWriter you pass in i.e.: so its ready for writing out 
	 * using the codec that get's passed to it. At the time rate and using the codec the class specifies
	 * @param writer
	 * @param audioCodec
	 */
	@Override
	public void addAudioStreamTo(IMediaWriter writer, IStreamCoder audioCodec) {//TODO: what's the point of passing the codec in but having the other things fields?
		int numAudioChannels = audioCodec.getChannels();
		int audioSampleRate = audioCodec.getSampleRate();
		ICodec.ID codecId = audioCodec.getCodecID();
		writer.addAudioStream(AUDIO_STREAM_INDEX,AUDIO_STREAM_ID,codecId,numAudioChannels,audioSampleRate);
	}
}


