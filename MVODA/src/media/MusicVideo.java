package media;

import java.awt.image.BufferedImage;

import lombok.Getter;
import lombok.Setter;

import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStreamCoder;



public class MusicVideo {
	
	@Getter private static final int SIZE_AUDIO_BUFFER = 1024;
	@Getter @Setter private long frames;
	@Getter @Setter private int audioChannels;
	@Getter @Setter private BufferedImage frameOfVideo;
	@Getter @Setter private String name;	
	@Getter @Setter private int width;
	@Getter @Setter private int height;
	@Getter @Setter private int framesPerSecond;
	@Getter @Setter private int numFrames;

	
	private IContainer container;
	private IPixelFormat.Type pixFormat;
	private IStreamCoder audioCoder;
	private IStreamCoder videoCoder;
	private IAudioSamples audioSamples;
	
	

	public MusicVideo(String name) {
		this.name = name;
	}


}
