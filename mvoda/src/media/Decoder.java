/**
 * 
 */
package media;

import java.awt.image.BufferedImage;

import lombok.Getter;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.BgrConverter;

/**
 * @author Tony
 *
 */
public class Decoder {
	
	public static final int SIZE_AUDIO_BUFFER = 1024; //TODO: why is this hard coded? Could I just remove it?
	
	private int outputWidth;
	private int outputHeight;
	private IVideoResampler resampler;
	@Getter private IAudioSamples audioSamples;
	@Getter private BufferedImage videoFrame;
	
	private BgrConverter converter;
	private MusicVideo video;
	
	@Getter private long timeStamp;
	@Getter private String formattedTimestamp;

	public Decoder(MusicVideo video) {
		this.video = video;
	}
	/**
	 * If the video codec of the video is not in pix format BGR24 we make it so, otherwise we aren't going to get a picture out of it
	 * TODO: investigate and really this should be capable of converting back to YUV420 surely?
	 * @param outputWidth
	 * @param outputHeight
	 */
	public void makeResampler(int outputWidth, int outputHeight) {

		this.outputWidth = outputWidth;
		this.outputHeight = outputHeight;

		if (video.getPixFormat() != IPixelFormat.Type.BGR24) {

			resampler = IVideoResampler.make(outputWidth, outputHeight, IPixelFormat.Type.BGR24, video.getWidth(), video.getHeight(), video.getPixFormat());

			if (resampler == null) {throw new RuntimeException("Problem generating resampler");}
			converter = new BgrConverter( IPixelFormat.Type.BGR24, outputWidth, outputHeight, video.getWidth(), video.getHeight() );
		}
	}

	/**
	 * When passed a video packet, will decode the packet until a single VideoPicture is decoded. 
	 * If the VideoPicture isn't in BGR24 format, we will resample it to make it so. We convert the result to a BufferedImage which is stored in the class
	 * @param packet
	 * @throws RuntimeException
	 */
	private void readVideo(IPacket packet) throws RuntimeException {

		IVideoPicture picture = IVideoPicture.make( video.getPixFormat(), video.getWidth(), video.getHeight() );

		int offset = 0;
		while (offset < packet.getSize()) {

			int numBytesDecoded = video.getVideoCoder().decodeVideo(picture, packet, offset);
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

		audioSamples = IAudioSamples.make(SIZE_AUDIO_BUFFER, video.getNumChannelsAudio());

		int offset = 0;
		while (offset < packet.getSize()) {

			int numBytesDecoded = video.getAudioCoder().decodeAudio(audioSamples, packet, offset);
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
		while (video.getContainer().readNextPacket(packet) >= 0) {

			audioSamples = null;
			videoFrame = null;

			int index = packet.getStreamIndex();
			if ( index == video.getAudioStreamId() ) {
				readAudio(packet);
				return true;
			} else if ( index == video.getVideoStreamId() ) {
				readVideo(packet);
				return true;
			} else {
				continue;
			}
		}
		return false; //return false if none of the remaining packets are neither audio or video
	}


}
