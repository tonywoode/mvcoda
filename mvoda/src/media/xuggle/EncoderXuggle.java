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
	private BufferedImage composite;
	private String overlayFile = "../../../Repo/mvoda/mvoda/Theme/Pop/Logo/4MLogoFrames/4M68.png";

	private Decoder decoder;

	/**
	 * By passing two UNCpaths to the constructor we specify and input and an output filename
	 * @param filename
	 * @param outFilename
	 */
	public EncoderXuggle(MusicVideo video,String outFilename) {
		this.video = video;
		this.outFilename = outFilename;
		render();
	}

	/**
	 * Creates a new music video with input filename and a new writer that will write to output filename, iterates through the packets of the music video
	 * encoding both, but allowing something to happen to the buffered images before the encode
	 * @param filename
	 */
	@Override
	public void render() {

		IMediaWriter writer = null;
		try {
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
				BufferedImage videoFrame = decoder.getVideoFrame(); //TODO: here they are they need to be somewhere else!!!!
				BufferedImage overlay = ImageIO.read(new File(overlayFile));
				if (videoFrame != null) {
					composite = overlayImage(videoFrame, overlay);	
					writer.encodeVideo(0, composite, decoder.getTimeStamp(), TimeUnit.MILLISECONDS);
				}
				if ((frame +1) >= lastFrame) { break; }
			}


		} catch (Exception ex) { //TODO: what ANY exception? Why aren't we saying we throw any then?
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (RuntimeException ex) { //TODO: only a runtime?!?!
					ex.printStackTrace();
				}
			}
			if (video != null) video.close();

		}
	}

	/**
	 * This is called by render(). When passed two buffered images, will arrange to overlay the latter over the former using the image manipulation classes
	 * @return the composite image
	 * @throws Exception
	 */
	public BufferedImage overlayImage(BufferedImage videoFrame, BufferedImage overlay) throws Exception {
		System.out.println("at video timestamp: " + decoder.getFormattedTimestamp());
		//ShowImageInFrame im = new ShowImageInFrame(videoFrame); //un-comment to see if we are getting images - though be aware will frame EVERY image
		ImageCompositor overlayframes = new ImageCompositor(videoFrame, overlay);
		composite = overlayframes.overlay();
		return composite;
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


