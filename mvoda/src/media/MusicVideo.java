package media;

import java.awt.image.BufferedImage;

import lombok.Getter;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.BgrConverter;

/**
 * Implementation class for music video interface using Xuggler as the media interface to FFMpeg
 * @author tony
 *
 */

public class MusicVideo {

	public static final int SIZE_AUDIO_BUFFER = 1024; //TODO: why is this hard coded? Could I just remove it?

	private IContainer container;
	@Getter private IStreamCoder audioCoder;
	private IStreamCoder videoCoder;
	@Getter private IAudioSamples audioSamples;
	private BgrConverter converter;
	private IPixelFormat.Type format;
	private IVideoResampler resampler;

	private int outputWidth;
	private int outputHeight;

	@Getter private long frames;
	@Getter private int audioChannels;
	@Getter private String fileUNC;	
	@Getter private int width;
	@Getter private int height;
	@Getter private int numChannelsAudio;
	@Getter private IRational framesPerSecond;
	@Getter private double framesPerSecondAsDouble;
	@Getter private int numFrames;
	@Getter private int audioStreamId = -1; //set to negative because 0 is a valid ID
	@Getter private int videoStreamId = -1; //set to negative because 0 is a valid ID
	@Getter private long containerDuration; //always in microseconds
	@Getter private long vidStreamDuration; //in whatever time units the format uses, somewhat complicated
	@Getter private BufferedImage videoFrame;
	@Getter private long numVidFrames;
	@Getter private long timeStamp;
	@Getter private String formattedTimestamp;



	/**
	 * Constructor takes only a filename, if that filename is a video FFMpeg can read i.e.: has audio and/or video streams codecs that can decode
	 * we will set the stream ID's and return an open container as the music video, with its properties available to inspect
	 * @param fileUNC
	 */
	public MusicVideo(String fileUNC) {
		this.fileUNC = fileUNC;
		container = IContainer.make(); //create a new container onject
		if (container.open(fileUNC, IContainer.Type.READ, null) <0) { //populate with the UNC you passed in
			throw new RuntimeException(fileUNC + ": failed to open");  
		}

		//then iterate through the container trying to find the video and audio streams
		int numStreams = container.getNumStreams();  
		for(int i = 0; i < numStreams; i++) {  
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			int index = stream.getIndex();

			//while neither stream is found, check for both in the streams, when found get their peroperties
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
		makeResampler(width, height);
		numChannelsAudio = audioCoder.getChannels();
		framesPerSecond = videoCoder.getFrameRate();
		framesPerSecondAsDouble = videoCoder.getFrameRate().getDouble();
		containerDuration = ( container.getDuration() == Global.NO_PTS ? 0 : container.getDuration()/1000 ); //gives result in milliseconds
	}

	/**
	 * If the video codec of the video is not in pix format BGR24 we make it so, otherwise we aren't going to get a picture out of it
	 * TODO: investigate and really this should be capable of converting back to YUV420 surely?
	 * @param outputWidth
	 * @param outputHeight
	 */
	private void makeResampler(int outputWidth, int outputHeight) {

		this.outputWidth = outputWidth;
		this.outputHeight = outputHeight;

		format = videoCoder.getPixelType();
		if (format != IPixelFormat.Type.BGR24) {

			resampler = IVideoResampler.make(outputWidth, outputHeight, IPixelFormat.Type.BGR24, width, height, format);

			if (resampler == null) {throw new RuntimeException("Problem generating resampler");}
			converter = new BgrConverter( IPixelFormat.Type.BGR24, outputWidth, outputHeight, width, height	);
		}
	}

	/**
	 * When passed a video packet, will decode the packet until a single VideoPicture is decoded. 
	 * If the VideoPicture isn't in BGR24 format, we will resample it to make it so. We convert the result to a BufferedImage which is stored in the class
	 * @param packet
	 * @throws RuntimeException
	 */
	private void readVideo(IPacket packet) throws RuntimeException {

		IVideoPicture picture = IVideoPicture.make(format, width, height);

		int offset = 0;
		while (offset < packet.getSize()) {

			int numBytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
			if (numBytesDecoded < 0) {throw new RuntimeException("Problem decoding video");}
			offset += numBytesDecoded;

			if (picture.isComplete()) {
				/*we DIVIDE BY 1000 TO GET TIMESTAMP IN MILLI FROM MICROSECONDS*/
				timeStamp = picture.getTimeStamp() / 1000; //I put this here to try to get a timestamp for drawOntoVideo
				formattedTimestamp = picture.getFormattedTimeStamp();
				IVideoPicture resampled = picture;
				if (picture.getPixelType() != IPixelFormat.Type.BGR24) {
					resampled = IVideoPicture.make(IPixelFormat.Type.BGR24, outputWidth, outputHeight);
					if (resampler.resample(resampled, picture) < 0) {
						throw new RuntimeException("Problem resampling video");
					}
				}
				videoFrame = converter.toImage(resampled);
				break;
			}
		}
	}

	/**
	 * When passed an audio packet, will decode to audio samples and store in audioSamples field in class
	 * @param packet
	 * @throws RuntimeException
	 */
	private void readAudio(IPacket packet) throws RuntimeException {

		audioSamples = IAudioSamples.make(SIZE_AUDIO_BUFFER, numChannelsAudio);

		int offset = 0;
		while (offset < packet.getSize()) {

			int numBytesDecoded = audioCoder.decodeAudio(audioSamples, packet, offset);
			if (numBytesDecoded < 0) {throw new RuntimeException("Problem decoding audio samples");}
			offset += numBytesDecoded;
			if (audioSamples.isComplete()) {
				break;
			}
		}
	}

	/**
	 * Checks whether a packet passed to it is audio or video. If either is passed calls readVideo or readAudio respectively and returns true
	 * @return
	 * @throws RuntimeException
	 */
	public boolean hasNextPacket() throws RuntimeException {

		IPacket packet = IPacket.make();
		while (container.readNextPacket(packet) >= 0) {

			audioSamples = null;
			videoFrame = null;

			int index = packet.getStreamIndex();
			if (index == audioStreamId) {
				readAudio(packet);
				return true;
			} else if (index == videoStreamId) {
				readVideo(packet);
				return true;
			} else {
				continue;
			}
		}
		return false; //return false if none of the remaining packets are neither audio or video
	}

	/**
	 * Closes the container that represents the music video
	 */
	public void close() {
		container.close();
	}
}
