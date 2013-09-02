/**
 * 
 */
package media.xuggle;

import java.awt.image.BufferedImage;

import lombok.Getter;
import media.AudioSamples;
import media.Decoder;
import media.MusicVideo;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IPixelFormat.Type;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.BgrConverter;

/**
 * @author Tony
 *
 */
public class DecoderXuggle implements Decoder {

	public static final int CONVERT_MICRO_TO_MILLISEC = 1000;
	public static final Type XUGGLER_PIX_TYPE = IPixelFormat.Type.BGR24; //Xuggler can only work with BGR24 pixel type as a fundamental limitation //TODO: throw this dependency up but bypass interface?
	public static final int SIZE_AUDIO_BUFFER = 1024; //TODO: why is this hard coded? Could I just remove it?

	private int outputWidth;
	private int outputHeight;
	private IVideoResampler resampler;
	private BgrConverter converter;


	private IAudioSamples audioSamples;
	@Getter private BufferedImage videoFrame;	
	@Getter private long videoTimeStamp;
	@Getter private String formattedVideoTimestamp;
	@Getter private long audioTimeStamp;
	@Getter private String formattedAudioTimestamp;

	private MusicVideo video;

	public DecoderXuggle(MusicVideo video) {
		this.video = video;
	}
	
	public AudioSamples getAudioSamples() {
		return audioSamples != null ? new AudioSamplesXuggle(audioSamples) : null;
	}
	
	/**
	 * Checks whether a packet passed to it is audio or video. If either is passed calls readVideo or readAudio respectively and returns true
	 * @throws RuntimeException
	 */
	@Override
	public boolean hasNextPacket() throws RuntimeException {

		IPacket packet = IPacket.make();
		audioSamples = null; //TODO: why do these need to be here? take them out you only get 7 seconds decode, they don't seem to need to be in the loop
		videoFrame = null;
		while (video.getContainer().readNextPacket(packet) >= 0) {
			int index = packet.getStreamIndex();
			if ( index == video.getAudioStreamIndex() ) {
				readAudio(packet);
				return true;
			} else if ( index == video.getVideoStreamIndex() ) {
				readVideo(packet);
				return true;
			} else {
				continue;
			}
		}
		return false; //return false if none of the remaining packets are either audio or video
	}
	
	
	/**
	 * If the video codec of the video is not in pix format BGR24 we make it so, otherwise we aren't going to get a picture out of it
	 * TODO: investigate and really this should be capable of converting back to YUV420 surely?
	 * @param outputWidth
	 * @param outputHeight
	 */
	@Override
	public void makeResampler(int outputWidth, int outputHeight) {

		this.outputWidth = outputWidth;
		this.outputHeight = outputHeight;

		if (video.getPixFormat() != XUGGLER_PIX_TYPE) {
			resampler = IVideoResampler.make(outputWidth, outputHeight, XUGGLER_PIX_TYPE, video.getWidth(), video.getHeight(), video.getPixFormat());
			if (resampler == null) {throw new RuntimeException("Problem generating resampler");}
			converter = new BgrConverter( XUGGLER_PIX_TYPE, outputWidth, outputHeight, video.getWidth(), video.getHeight() );
		}
	}

	/**
	 * When passed a video packet, will decode the packet until a single VideoPicture is decoded. 
	 * If the VideoPicture isn't in BGR24 format, we will resample it to make it so. We convert the result to a BufferedImage which is stored in the class
	 * @param packet
	 * @throws RuntimeException
	 */
	@Override
	public void readVideo(IPacket packet) throws RuntimeException {

		IVideoPicture picture = IVideoPicture.make( video.getPixFormat(), video.getWidth(), video.getHeight() );

		int offset = 0;
		while (offset < packet.getSize()) {

			int bytesDecoded = video.getVideoCoder().decodeVideo(picture, packet, offset);
			if (bytesDecoded < 0) {throw new RuntimeException("Problem decoding video");}
			offset += bytesDecoded;
			if (picture.isComplete()) {
				/*we DIVIDE BY 1000 TO GET TIMESTAMP IN MILLI FROM MICROSECONDS*/
				videoTimeStamp = picture.getTimeStamp();// / CONVERT_MICRO_TO_MILLISEC; //We get a timestamp for the picture for the re-encode
				//videoTimeStampInMicros = picture.getTimeStamp();
				formattedVideoTimestamp = picture.getFormattedTimeStamp(); //we also get a human readable timestamp for troubleshooting
				IVideoPicture resampled = picture;
				if (picture.getPixelType() != XUGGLER_PIX_TYPE) {
					//default output pix format for resampler WILL be XUGGLER_PIX_TYPE, in future we could set XUGGLER_PIX_TYPE to resampler as resampler implements IConfigurable
					resampled = IVideoPicture.make(resampler.getOutputPixelFormat(), outputWidth, outputHeight);
					if (resampler.resample(resampled, picture) < 0) { throw new RuntimeException("Problem resampling video"); }
				}
				videoFrame = converter.toImage(resampled);
				//break; //TODO: delete?
			}
		}
	}

	/**
	 * When passed an audio packet, will decode to audio samples and store in audioSamples field in class
	 * @param packet
	 * @throws RuntimeException
	 */
	@Override
	public void readAudio(IPacket packet) throws RuntimeException {

		audioSamples = IAudioSamples.make(SIZE_AUDIO_BUFFER, video.getNumChannelsAudio());
		
		int offset = 0;
		while (offset < packet.getSize()) {
			int numBytesDecoded = video.getAudioCoder().decodeAudio(audioSamples, packet, offset);
			if (numBytesDecoded < 0) {throw new RuntimeException("Problem decoding audio samples");}
			offset += numBytesDecoded;
			
			
			//if (audioSamples.isComplete()) { break;	} //TODO: delete?
			if (audioSamples.isComplete()) {
			audioTimeStamp = audioSamples.getTimeStamp();// / CONVERT_MICRO_TO_MILLISEC;
			formattedAudioTimestamp = audioSamples.getFormattedTimeStamp();
			}
		}
	}

}
