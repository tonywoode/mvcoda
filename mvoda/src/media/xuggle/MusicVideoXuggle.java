package media.xuggle;

import java.util.logging.Logger;

import lombok.Getter;
import media.Decoder;
import media.MusicVideo;
import media.types.Container;
import media.types.Rational;
import media.types.StreamCoder;
import media.xuggle.types.ContainerXuggle;
import media.xuggle.types.RationalXuggle;
import media.xuggle.types.StreamCoderXuggle;
import util.FileUtil;
import util.FrameRate;
import view.MediaOpenException;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

/**
 * Implementation class for music video interface using Xuggler as the media interface to FFMpeg
 * @author tony
 *
 */
public class MusicVideoXuggle implements MusicVideo {

	public final static Logger LOGGER = Logger.getLogger(MusicVideoXuggle.class.getName()); //get a logger for this class
	
	@Getter private double framesPerSecondAsDouble;
	@Getter private ICodec.ID videoCodecID;
	@Getter private IPixelFormat.Type pixFormat;
	
	private IContainer container;
	private IStreamCoder audioCoder;
	private IStreamCoder videoCoder;
	private IRational framesPerSecond;
	

	/* The following methods are for adaptation, we need to use Xuggler's imp internally, 
	so we only return the abstract interface for clients. We return null to caller if necessary */
	public Container getContainer() { return container != null ? new ContainerXuggle(container) : null;	}
	
	public StreamCoder getAudioCoder() { return audioCoder != null ? new StreamCoderXuggle(audioCoder) : null; }
	
	public StreamCoder getVideoCoder() { return videoCoder != null ? new StreamCoderXuggle(videoCoder) : null; }
		
	public Rational getFramesPerSecond() { return framesPerSecond != null ? new RationalXuggle(framesPerSecond) : null;	}

	@Getter private String fileUNC;	
	@Getter private String filetype;
	@Getter private int width;
	@Getter private int height;
	@Getter private int numChannelsAudio;	
	@Getter private int audioStreamIndex = -1; //set to negative because 0 is a valid stream ID
	@Getter private int videoStreamIndex = -1; //set to negative because 0 is a valid stream ID
	@Getter private int audioStreamID = -1;
	@Getter private int videoStreamID = -1;
	@Getter private long containerDuration; //always in microseconds
	@Getter private long frameRateDivisor;
	@Getter private long vidStreamDuration;
	@Getter private long numVidFrames;
	private int numStreams;

	@Getter private Decoder decoder;

	/**
	 * Constructor takes only a filename, if that filename is a video FFMpeg can read i.e.: has audio and/or video streams codecs that can decode
	 * we will set the stream ID's and return an open container as the music video, with its properties available to inspect
	 * @param fileUNC the path to the music video
	 * @throws MediaOpenException 
	 */
	public MusicVideoXuggle(String fileUNC) throws MediaOpenException {
		this.fileUNC = fileUNC;
		this.decoder = new DecoderXuggle(this); //make sure we hold a reference to a specific decoder
		container = IContainer.make(); //create a new container object
		//populate with the UNC you passed in
		if (container.open(fileUNC, IContainer.Type.READ, null) <0) { throw new MediaOpenException(fileUNC + ": failed to open"); }
		filetype = FileUtil.getFiletype(fileUNC); //we may use the filetype later

		//then iterate through the container trying to find the video and audio streams
		numStreams = container.getNumStreams();  
		for(int i = 0; i < numStreams; i++) {  
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			int index = stream.getIndex();
			int id = stream.getId();

			//while neither stream is found, check for both in the streams, when found get their properties
			if ( audioStreamIndex < 0 && coder.getCodecType().equals(ICodec.Type.CODEC_TYPE_AUDIO) ) {
				audioCoder = coder;
				audioStreamIndex = index;
				audioStreamID = id;
			}
			if ( videoStreamIndex < 0 && coder.getCodecType().equals(ICodec.Type.CODEC_TYPE_VIDEO) ) {
				videoCoder = coder;
				videoStreamIndex = index;
				videoStreamID = id;
				numVidFrames = stream.getNumFrames();
				frameRateDivisor = stream.getTimeBase().getNumerator() * stream.getTimeBase().getDenominator();
				//Xugglers stream duration is in whatever time units the format uses, so we'll use time base denominator and numerator and convert micro to millis
				vidStreamDuration = stream.getDuration() / frameRateDivisor * FrameRate.getTimeBasis(); //microsenconds
										LOGGER.info("numerator is " + stream.getTimeBase().getNumerator());
										LOGGER.info("denomiator is " + stream.getTimeBase().getDenominator());
										LOGGER.info( "real time is therefore: " + vidStreamDuration);
				videoCodecID = coder.getCodecID();
			}
		}

		//error if we haven't found any streams
		if ( videoStreamIndex < 0 && audioStreamIndex < 0 ) { throw new  MediaOpenException( fileUNC + " Doesn't contain audio or video streams" );}
		if ( audioCoder != null && ( audioCoder.open(null, null) < 0 ) ) { throw new  MediaOpenException(fileUNC + ": audio can't be opened");}
		if ( videoCoder != null && ( videoCoder.open(null, null) < 0 ) ) { throw new  MediaOpenException(fileUNC + ": video can't be opened");}

		//get some properties of the coders now that we have them
		width = videoCoder.getWidth();
		height = videoCoder.getHeight();
		pixFormat = videoCoder.getPixelType();
		
		//now we have width and height, we convert to BGR24
		decoder.makeResampler(width, height);
		numChannelsAudio = audioCoder.getChannels();
		framesPerSecond = videoCoder.getFrameRate();
		framesPerSecondAsDouble = videoCoder.getFrameRate().getDouble();
		containerDuration = ( container.getDuration() == Global.NO_PTS ? 0 : container.getDuration()  ); //gives result in microseconds

	}

	/**
	 * Closes the container that represents the music video
	 */
	@Override public void close() { 
		if (videoCoder != null) { videoCoder.close(); videoCoder = null; }
		if (audioCoder != null)	{ audioCoder.close(); audioCoder = null; }
		if (container !=null) { container.close(); container = null; }
	}


	/**
	 * Prints out formatted information about the media file
	 */
	@Override public String toString() {
		String str = "";
		str += String.format("File path: %s", fileUNC);
		str += String.format("\nFile Size (bytes): %d", container.getFileSize() );
		str += String.format("\tBit Rate: %d", container.getBitRate() );
		str += String.format("\nNumber of streams: %d", numStreams);
		str += String.format("\tDuration (microseconds): %d", containerDuration);
		// iterate through the streams to print their meta data
		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i); // find the stream object
			IStreamCoder coder = stream.getStreamCoder(); 	// get the pre-configured decoder that can decode this stream;

			str += String.format ("\n......Stream %d Info.....", i);
			str += String.format("\ntype: %s ", coder.getCodecType());
			str += String.format("\tcodec: %s ", coder.getCodecID());
			str += String.format("\nduration (number of frames): %s ", stream.getDuration());
			str += String.format("\tstart time: %s ", container.getStartTime());
			str += String.format("\ntimebase: %d/%d ",	stream.getTimeBase().getNumerator(), stream.getTimeBase().getDenominator());
			str += String.format("\tcoder tb: %d/%d ",	coder.getTimeBase().getNumerator(), coder.getTimeBase().getDenominator());

			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				str += String.format("\nsample rate: %d ", coder.getSampleRate());
				str += String.format("\tchannels: %d ", coder.getChannels());
				str += String.format("\nformat: %s", coder.getSampleFormat());
			} 
			else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				str += String.format("\nwidth: %d ", coder.getWidth());
				str += String.format("\theight: %d ", coder.getHeight());
				str += String.format("\nformat: %s ", coder.getPixelType());
				str += String.format("\tframe-rate: %5.2f ", coder.getFrameRate().getDouble());
			}
		}
		return str;
	}

}
