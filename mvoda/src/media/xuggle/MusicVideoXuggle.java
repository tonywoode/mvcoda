package media.xuggle;

import lombok.Getter;
import media.Decoder;
import media.MusicVideo;

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

	@Getter private IContainer container;
	@Getter private IStreamCoder audioCoder;
	@Getter private IStreamCoder videoCoder;
	@Getter private IPixelFormat.Type pixFormat;
	@Getter private IRational framesPerSecond;
	@Getter private double framesPerSecondAsDouble;
	@Getter private ICodec.ID videoCodecID;


	@Getter private String fileUNC;	
	@Getter private int width;
	@Getter private int height;
	@Getter private int numChannelsAudio;	
	@Getter private int audioStreamIndex = -1; //set to negative because 0 is a valid ID
	@Getter private int videoStreamIndex = -1; //set to negative because 0 is a valid ID
	@Getter private int audioStreamID = -1;
	@Getter private int videoStreamID = -1;
	@Getter private long containerDuration; //always in microseconds
	@Getter private long vidStreamDuration; //in whatever time units the format uses, somewhat complicated
	@Getter private long numVidFrames;
	private int numStreams;

	@Getter private Decoder decoder;

	/**
	 * Constructor takes only a filename, if that filename is a video FFMpeg can read i.e.: has audio and/or video streams codecs that can decode
	 * we will set the stream ID's and return an open container as the music video, with its properties available to inspect
	 * @param fileUNC
	 */
	public MusicVideoXuggle(String fileUNC) {
		this.fileUNC = fileUNC;
		this.decoder = new DecoderXuggle(this); //how do we get rid of this? It can get called here so it doesn't NEED any properties at this point...
		container = IContainer.make(); //create a new container object
		if (container.open(fileUNC, IContainer.Type.READ, null) <0) { //populate with the UNC you passed in
			throw new RuntimeException(fileUNC + ": failed to open");  
		}

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
				vidStreamDuration = stream.getDuration();
				System.out.println("numerator is " + stream.getTimeBase().getNumerator());
				System.out.println("denomiator is " + stream.getTimeBase().getDenominator());
				System.out.println( "real time is therefore: " + vidStreamDuration / stream.getTimeBase().getNumerator() * stream.getTimeBase().getDenominator() );
				videoCodecID = coder.getCodecID();
			}
		}

		//error if we haven't found any streams
		if ( videoStreamIndex < 0 && audioStreamIndex < 0 ) { throw new RuntimeException( fileUNC + " Doesn't contain audio or video streams" );}
		if ( audioCoder != null && ( audioCoder.open(null, null) < 0 ) ) { throw new RuntimeException(fileUNC + ": audio can't be opened");}
		if ( videoCoder != null && ( videoCoder.open(null, null) < 0 ) ) { throw new RuntimeException(fileUNC + ": video can't be opened");}

		//get some properties of the coders now that we have them
		width = videoCoder.getWidth();
		height = videoCoder.getHeight();
		pixFormat = videoCoder.getPixelType();
		//now we have width and height, lets convert to BGR24
		decoder.makeResampler(width, height);
		numChannelsAudio = audioCoder.getChannels();
		framesPerSecond = videoCoder.getFrameRate();
		framesPerSecondAsDouble = videoCoder.getFrameRate().getDouble();
		containerDuration = ( container.getDuration() == Global.NO_PTS ? 0 : container.getDuration() /1000 ); //gives result in milliseconds

	}

	/**
	 * Closes the container that represents the music video
	 */
	@Override
	public void close() {
		if (videoCoder != null)
		{
			videoCoder.close();
			videoCoder = null;
		}
		if (audioCoder != null)
		{
			audioCoder.close();
			audioCoder = null;
		}
		if (container !=null)
		{
			container.close();
			container = null;
		}
	}


	/**
	 * Prints out formatted information about the media file
	 */
	@Override
	public String toString() {
		String str = "";
		str += String.format("File path: %s", fileUNC);
		str += String.format("\nFile Size (bytes): %d", container.getFileSize() );
		str += String.format("\nBit Rate: %d", container.getBitRate() );
		str += String.format("\nNumber of streams: %d", numStreams);
		str += String.format("\nDuration (ms): %d", containerDuration);
		// iterate through the streams to print their meta data
		for (int i = 0; i < numStreams; i++) {
			IStream stream = container.getStream(i); // find the stream object
			IStreamCoder coder = stream.getStreamCoder(); 	// get the pre-configured decoder that can decode this stream;

			str += String.format ("\n......Stream %d Info.....", i);
			str += String.format("\ntype: %s; ", coder.getCodecType());
			str += String.format("\ncodec: %s; ", coder.getCodecID());
			str += String.format("\nduration (number of frames): %s; ", stream.getDuration());
			str += String.format("\nstart time: %s; ", container.getStartTime());
			str += String.format("\ntimebase: %d/%d; ",	stream.getTimeBase().getNumerator(), stream.getTimeBase().getDenominator());
			str += String.format("\ncoder tb: %d/%d; ",	coder.getTimeBase().getNumerator(), coder.getTimeBase().getDenominator());

			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
				str += String.format("\nsample rate: %d; ", coder.getSampleRate());
				str += String.format("\nchannels: %d; ", coder.getChannels());
				str += String.format("\nformat: %s", coder.getSampleFormat());
			} 
			else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
				str += String.format("\nwidth: %d; ", coder.getWidth());
				str += String.format("\nheight: %d; ", coder.getHeight());
				str += String.format("\nformat: %s; ", coder.getPixelType());
				str += String.format("\nframe-rate: %5.2f; ", coder.getFrameRate().getDouble());
			}
			str += String.format ("\n...End of Stream %d Info..\n", i);
		}
		return str;
	}

}
