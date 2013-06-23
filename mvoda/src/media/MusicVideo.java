package media;

import lombok.Getter;

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

public class MusicVideo {

	@Getter private IContainer container;
	@Getter private IStreamCoder audioCoder;
	@Getter private IStreamCoder videoCoder;
	@Getter private IPixelFormat.Type pixFormat;
	@Getter private IRational framesPerSecond;
	@Getter private double framesPerSecondAsDouble;


	@Getter private String fileUNC;	
	@Getter private int width;
	@Getter private int height;
	@Getter private int numChannelsAudio;	
	@Getter private int audioStreamId = -1; //set to negative because 0 is a valid ID
	@Getter private int videoStreamId = -1; //set to negative because 0 is a valid ID
	@Getter private long containerDuration; //always in microseconds
	@Getter private long vidStreamDuration; //in whatever time units the format uses, somewhat complicated
	@Getter private long numVidFrames;
	
	@Getter private Decoder decoder;

	/**
	 * Constructor takes only a filename, if that filename is a video FFMpeg can read i.e.: has audio and/or video streams codecs that can decode
	 * we will set the stream ID's and return an open container as the music video, with its properties available to inspect
	 * @param fileUNC
	 */
	public MusicVideo(String fileUNC) {
		this.fileUNC = fileUNC;
		this.decoder = new Decoder(this); //how do we get rid of this? It can get called here so it doesn't NEED any properties at this point...
		container = IContainer.make(); //create a new container object
		if (container.open(fileUNC, IContainer.Type.READ, null) <0) { //populate with the UNC you passed in
			throw new RuntimeException(fileUNC + ": failed to open");  
		}

		//then iterate through the container trying to find the video and audio streams
		int numStreams = container.getNumStreams();  
		for(int i = 0; i < numStreams; i++) {  
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			int index = stream.getIndex();

			//while neither stream is found, check for both in the streams, when found get their properties
			if ( audioStreamId < 0 && coder.getCodecType().equals(ICodec.Type.CODEC_TYPE_AUDIO) ) {
				audioCoder = coder;
				audioStreamId = index;
			}
			if ( videoStreamId < 0 && coder.getCodecType().equals(ICodec.Type.CODEC_TYPE_VIDEO) ) {
				videoCoder = coder;
				videoStreamId = index;
				numVidFrames = stream.getNumFrames();
				vidStreamDuration = stream.getDuration();
			}
		}
		
		//error if we haven't found any streams
		if ( videoStreamId < 0 && audioStreamId < 0 ) { throw new RuntimeException( fileUNC + " Doesn't contain audio or video streams" );}
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
		containerDuration = ( container.getDuration() == Global.NO_PTS ? 0 : container.getDuration()/1000 ); //gives result in milliseconds
	    
	}

	/**
	 * Closes the container that represents the music video
	 */
	public void close() {
		container.close();
	}
}
