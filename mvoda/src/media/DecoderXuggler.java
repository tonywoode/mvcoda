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
public class DecoderXuggler implements Decoder {
	
	private int outputWidth;
	private int outputHeight;
	private IVideoResampler resampler;
	private BgrConverter converter;
	
	
	@Getter private IAudioSamples audioSamples;
	@Getter private BufferedImage videoFrame;	
	@Getter private long timeStamp;
	@Getter private String formattedTimestamp;
	
	private MusicVideo video;

	public DecoderXuggler(MusicVideo video) {
		this.video = video;
	}
	/* (non-Javadoc)
	 * @see media.Decoder#makeResampler(int, int)
	 */
	@Override
	public void makeResampler(int outputWidth, int outputHeight) {

		this.outputWidth = outputWidth;
		this.outputHeight = outputHeight;

		if (video.getPixFormat() != IPixelFormat.Type.BGR24) {

			resampler = IVideoResampler.make(outputWidth, outputHeight, IPixelFormat.Type.BGR24, video.getWidth(), video.getHeight(), video.getPixFormat());

			if (resampler == null) {throw new RuntimeException("Problem generating resampler");}
			converter = new BgrConverter( IPixelFormat.Type.BGR24, outputWidth, outputHeight, video.getWidth(), video.getHeight() );
		}
	}

	/* (non-Javadoc)
	 * @see media.Decoder#readVideo(com.xuggle.xuggler.IPacket)
	 */
	@Override
	public void readVideo(IPacket packet) throws RuntimeException {

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

	/* (non-Javadoc)
	 * @see media.Decoder#readAudio(com.xuggle.xuggler.IPacket)
	 */
	@Override
	public void readAudio(IPacket packet) throws RuntimeException {

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

	/* (non-Javadoc)
	 * @see media.Decoder#hasNextPacket()
	 */
	@Override
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
