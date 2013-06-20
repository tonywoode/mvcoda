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
 * Imp for music video class with Xuggler
 * @author tony
 *
 */

public class MusicVideo {


	private IContainer container;
	private IPixelFormat.Type pixFormat;
	@Getter private IStreamCoder audioCoder;
	private IStreamCoder videoCoder;
	@Getter private IAudioSamples audioSamples;
	private BgrConverter converter;
	private IPixelFormat.Type format;
	private IVideoResampler resampler;
	
	private int outputWidth;
	private int outputHeight;

	@Getter private static final int SIZE_AUDIO_BUFFER = 1024;
	@Getter private long frames;
	@Getter private int audioChannels;
	@Getter private String fileUNC;	
	@Getter private int width;
	@Getter private int height;
	@Getter private int numChannelsAudio;
	@Getter private IRational framesPerSecond;
	@Getter private double framesPerSecondAsDouble;
	@Getter private int numFrames;
	@Getter private int audioStreamId;
	@Getter private int videoStreamId;
	@Getter private long duration;
	@Getter private BufferedImage videoFrame;
	@Getter private long numVidFrames;
	@Getter private long timeStamp;
	

	/**
	 * Constructor takes only a filename, returns an open container with its properties available
	 * @param fileUNC
	 */
	public MusicVideo(String fileUNC) {
		this.fileUNC = fileUNC;
		container = IContainer.make(); //create a new container onject
		if (container.open(fileUNC, IContainer.Type.READ, null) <0) { //populate with the UNC you passed in
			throw new RuntimeException(fileUNC + ": failed to open");  
		}

		//then iterate through the container trying to find the video and audio streams

		audioStreamId = -1; videoStreamId = -1; //set these to negative

		int numStreams = container.getNumStreams();  
		for(int i = 0; i < numStreams; i++) {  
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			int index = stream.getIndex();

			//while neither stream is found, check for both in the streams
			if ( audioStreamId < 0 && coder.getCodecType().equals(ICodec.Type.CODEC_TYPE_AUDIO) ) {
				audioCoder = coder;
				audioStreamId = index;
			}
			if ( videoStreamId < 0 && coder.getCodecType().equals(ICodec.Type.CODEC_TYPE_VIDEO) ) {
				videoCoder = coder;
				videoStreamId = index;
				numVidFrames = stream.getNumFrames();
			}
		}

		//error if we haven't found any streams
		if ( videoStreamId < 0 && audioStreamId < 0 ) {
			throw new RuntimeException( fileUNC + " Doesn't contain audio or video streams" );
		}
		if ( audioCoder != null && ( audioCoder.open(null, null) < 0 ) ) { throw new RuntimeException(fileUNC + ": audio can't be opened");}
		if ( videoCoder != null && ( videoCoder.open(null, null) < 0 ) ) { throw new RuntimeException(fileUNC + ": video can't be opened");}

		width = videoCoder.getWidth();
		height = videoCoder.getHeight();
		makeResampler(width, height);
		numChannelsAudio = audioCoder.getChannels();
		framesPerSecond = videoCoder.getFrameRate();
		framesPerSecondAsDouble = videoCoder.getFrameRate().getDouble();
		//numFrames = videoCoder. //how DO YOU WORK OUT THE NUMBER OF FRAMES? CAN'T FIND DURATION?
		duration = ( container.getDuration() == Global.NO_PTS ? 0 : container.getDuration()/1000 ); //milliseconds
	}
	
	
private void makeResampler(int outputWidth, int outputHeight) {
		
		this.outputWidth = outputWidth;
		this.outputHeight = outputHeight;
		
		format = videoCoder.getPixelType();
		
		if (format != IPixelFormat.Type.BGR24) {
			
			resampler = IVideoResampler.make(
				outputWidth, outputHeight, IPixelFormat.Type.BGR24,
				width, height, format
			);
			
			if (resampler == null) {
				throw new RuntimeException("Unable to make a resampler");
			}
			
			converter = new BgrConverter(
				IPixelFormat.Type.BGR24,
				outputWidth, outputHeight,
				width, height
			);
		}
	}

private void readVideo(IPacket packet) throws RuntimeException {
	
	IVideoPicture picture = IVideoPicture.make(format, width, height);
	
	int offset = 0;
	while (offset < packet.getSize()) {
		
		int numBytesDecoded = videoCoder.decodeVideo(picture, packet, offset);
		if (numBytesDecoded < 0) {
			throw new RuntimeException("Could not decode video");
		}
		offset += numBytesDecoded;
		timeStamp = picture.getTimeStamp(); //I put this here to try to get a timestamp for drawOntoVideo
		if (picture.isComplete()) {
			
			IVideoPicture resampled = picture;
			if (picture.getPixelType() != IPixelFormat.Type.BGR24) {
				resampled = IVideoPicture.make(IPixelFormat.Type.BGR24, outputWidth, outputHeight);
				timeStamp = resampled.getTimeStamp(); //I put this here to try to get a timestamp for drawOntoVideo
				if (resampler.resample(resampled, picture) < 0) {
					throw new RuntimeException("Could not resample video");
				}
			}
			
			videoFrame = converter.toImage(resampled);
			break;
		}
	}
}

private void readAudio(IPacket packet) throws RuntimeException {
	
	audioSamples = IAudioSamples.make(SIZE_AUDIO_BUFFER, numChannelsAudio);
	
	int offset = 0;
	while (offset < packet.getSize()) {
		
		int numBytesDecoded = audioCoder.decodeAudio(audioSamples, packet, offset);
		if (numBytesDecoded < 0) {
			throw new RuntimeException("Unable to decode audio samples");
		}
		
		offset += numBytesDecoded;
		
		if (audioSamples.isComplete()) {
			break;
		}
	}
}

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
	
	return false;
}
	


	
	public void close() {
		container.close();
	}
}
