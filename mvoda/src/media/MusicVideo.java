package media;

import java.awt.image.BufferedImage;

import lombok.Getter;
import lombok.Setter;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

/**
 * Imp for music video class with Xuggler
 * @author tony
 *
 */

public class MusicVideo {

	@Getter private static final int SIZE_AUDIO_BUFFER = 1024;
	@Getter @Setter private long frames;
	@Getter @Setter private int audioChannels;
	@Getter @Setter private BufferedImage frameOfVideo;
	@Getter @Setter private String fileUNC;	
	@Getter @Setter private int width;
	@Getter @Setter private int height;
	private int numChannelsAudio;
	private int framesPerSecond;
	private int numFrames;
	private int audioStreamId;
	private int videoStreamId;

	private IContainer container;
	private IPixelFormat.Type pixFormat;
	private IStreamCoder audioCoder;
	private IStreamCoder videoCoder;
	private IAudioSamples audioSamples;


	public MusicVideo(String fileUNC) {
		this.fileUNC = fileUNC;
		container = IContainer.make();
		if (container.open(fileUNC, IContainer.Type.READ, null) <0) {
			throw new RuntimeException("failed to open");  
		}

		int numStreams = container.getNumStreams();  
		for(int i = 0; i < numStreams; i++) {  
			IStream stream = container.getStream(i);
			IStreamCoder coder = stream.getStreamCoder();
			int index = stream.getIndex();

			if (coder.getCodecType().equals(ICodec.Type.CODEC_TYPE_AUDIO)) {
				audioCoder = coder;
				audioStreamId = index;
			}
			if (coder.getCodecType().equals(ICodec.Type.CODEC_TYPE_VIDEO)) {
				videoCoder = coder;
				videoStreamId = index;
			}
		}
			audioCoder.open(null, null);
			videoCoder.open(null, null);

			width = videoCoder.getWidth();
			height = videoCoder.getHeight();
			numChannelsAudio = audioCoder != null ? audioCoder.getChannels() : 0;


		
	}




}
