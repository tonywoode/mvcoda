
package media.xuggle;

import java.awt.image.BufferedImage;

import lombok.Getter;
import media.Decoder;
import media.MusicVideo;
import media.types.AudioSamples;
import media.types.Packet;
import media.xuggle.types.AudioSamplesXuggle;
import media.xuggle.types.PacketXuggle;
import media.xuggle.types.VideoPictureXuggle;

import com.sun.media.jfxmedia.MediaException;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IPixelFormat.Type;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.BgrConverter;

/**
 * Main decoder Imp for MV-CoDA. Takes Music videos and uses their information to decode contents
 * @author Tony
 *
 */
public class DecoderXuggle implements Decoder {
	
	/*
	 * Some inspiration for this class and elements of the encoder taken from http://syntaxcandy.blogspot.co.uk/2011/03/video-with-gps-overlay.html
	 * Particularly the idea of simplifying calls where possible, using hasNext..methods, and hard coding the buffer size
	 * however, this is not so different to how Xuggle do things...
	 */

	public static final int CONVERT_MICRO_TO_MILLISEC = 1000; //Xuggler is best in microseconds
	public static final Type XUGGLER_PIX_TYPE = IPixelFormat.Type.BGR24; //Xuggler can only work with BGR24 pixel type as a fundamental limitation
	public static final int SIZE_AUDIO_BUFFER = 1024; //Xuggler works well with 1024 buffer size

	@Getter private BufferedImage videoFrame;	
	@Getter private long videoTimeStamp;
	@Getter private String formattedVideoTimestamp;
	@Getter private long audioTimeStamp;
	@Getter private String formattedAudioTimestamp;
	
	private int outputWidth;
	private int outputHeight;
	private IVideoResampler resampler;
	private BgrConverter converter;
	private AudioSamples audioSamples;
	
	private MusicVideo video;

	public DecoderXuggle(MusicVideo video) {
		this.video = video;
	}

	public AudioSamples getAudioSamples() { //part of the adapting framework
		return audioSamples;
	}

	/**
	 * Checks whether a packet passed to it is audio or video. If either is passed calls readVideo or readAudio respectively and returns true
	 * @throws RuntimeException
	 */
	@Override public boolean hasNextPacket() throws RuntimeException {

		Packet packet = new PacketXuggle(IPacket.make());
		audioSamples = null; //take these out, you only get 7 seconds decode
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
	 * @param outputWidth
	 * @param outputHeight
	 */
	@Override public void makeResampler(int outputWidth, int outputHeight) {
		//TODO: investigate as Xuggler really this should be capable of also converting back to YUV420
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
	@Override public void readVideo(Packet packet) {

		//for adaptation
		VideoPictureXuggle picture = new VideoPictureXuggle(IVideoPicture.make( video.getPixFormat(), video.getWidth(), video.getHeight()));

		int offset = 0;
		while (offset < packet.getSize()) {

			int bytesDecoded = video.getVideoCoder().decodeVideo(picture, packet, offset);
			if (bytesDecoded < 0) {throw new MediaException("Problem decoding video");}
			offset += bytesDecoded;
			if (picture.isComplete()) {
				videoTimeStamp = picture.getTimeStamp();//We get a timestamp for the picture for the re-encode
				formattedVideoTimestamp = picture.getFormattedTimeStamp(); //we also get a human readable timestamp for troubleshooting
				IVideoPicture resampled = (IVideoPicture)picture.getInternalVideoPicture();
				if (picture.getPixelType() != XUGGLER_PIX_TYPE) {
					//default output pix format for resampler WILL be XUGGLER_PIX_TYPE, in future we could set XUGGLER_PIX_TYPE to resampler as resampler implements IConfigurable
					resampled = IVideoPicture.make(resampler.getOutputPixelFormat(), outputWidth, outputHeight);
					if (resampler.resample(resampled, (IVideoPicture)picture.getInternalVideoPicture()) < 0) { throw new RuntimeException("Problem resampling video"); }
				}
				videoFrame = converter.toImage(resampled);
			}
		}
	}

	/**
	 * When passed an audio packet, will decode to audio samples and store in audioSamples field in class
	 * @param packet
	 * @throws RuntimeException
	 */
	@Override public void readAudio(Packet packet) {

		//for adaptation
		audioSamples = new AudioSamplesXuggle(IAudioSamples.make(SIZE_AUDIO_BUFFER, video.getNumChannelsAudio()));

		int offset = 0;
		while (offset < packet.getSize()) {
			int numBytesDecoded = video.getAudioCoder().decodeAudio(audioSamples, packet, offset);
			if (numBytesDecoded < 0) {throw new MediaException("Problem decoding audio samples");}
			offset += numBytesDecoded;
			if (audioSamples.isComplete()) {
				audioTimeStamp = audioSamples.getTimeStamp();
				formattedAudioTimestamp = audioSamples.getFormattedTimeStamp();
			}
		}
	}

}
