package media.xuggle;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import playlist.Playlist;
import themes.Theme;

import media.Decoder;
import media.Encoder;
import media.MediaWriter;
import media.MusicVideo;
//import theme.Theme;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStreamCoder;

import drawing.ImageCompositor;
import drawing.TextCompositor;
/**
 * Basic methods to deal with buffered images we get from music video packets so that we can manipulate them and recode a video. Its basically "do something then encode"
 * @author Tony
 *
 */
public class EncoderXuggleMultipleFixedDecoders implements Encoder {

	private MusicVideo video;
	private MusicVideo video2;
	private MusicVideo video3;
	private MusicVideo video4;
	private MusicVideo video5;
	private MusicVideo video6;
	private MusicVideo video7;
	private MusicVideo video8;
	private MusicVideo video9;
	private MusicVideo video10;
	private String outFilename;
	private BufferedImage composite;
	private Decoder decoder;
	private Decoder decoder2;
	private Decoder decoder3;
	private Decoder decoder4;
	private Decoder decoder5;
	private Decoder decoder6;
	private Decoder decoder7;
	private Decoder decoder8;
	private Decoder decoder9;
	private Decoder decoder10;
	private Theme theme;
	
	private MediaWriter writer = null;
	
	private long timecode;
	private long timecodeFromVideoOne;
	
	private ImageCompositor logoCompositor;
	private ImageCompositor strapCompositor;
	private ImageCompositor strapCompositor2;
	private ImageCompositor chartCompositor;
	private ImageCompositor transitionCompositor;
	private ImageCompositor numbersCompositor;
	private TextCompositor numberText;
	private TextCompositor trackText;
	private TextCompositor artistText;
	private TextCompositor chartText;

	/**
	 * By passing two UNCpaths to the constructor we specify and input and an output filename
	 * @param filename
	 * @param outFilename
	 */
	public EncoderXuggleMultipleFixedDecoders(Playlist playlist, Theme theme,String outFilename) {
		//this.video = video;
		//this.video2 = video2;
		video = playlist.getNextEntry(0).getVideo();
		video2 = playlist.getNextEntry(1).getVideo();
		video3 = playlist.getNextEntry(2).getVideo();
		video4 = playlist.getNextEntry(3).getVideo();
		video5 = playlist.getNextEntry(4).getVideo();
		video6 = playlist.getNextEntry(5).getVideo();
		video7 = playlist.getNextEntry(6).getVideo();
		video8 = playlist.getNextEntry(7).getVideo();
		video9 = playlist.getNextEntry(8).getVideo();
		video10 = playlist.getNextEntry(9).getVideo();
		this.outFilename = outFilename;
		this.theme = theme;
		render(playlist);//now using the playlist just trying to get it to run against new method
	}

	/**
	 * Creates a new music video with input filename and a new writer that will write to output filename, iterates through the packets of the music video
	 * encoding both, but allowing something to happen to the buffered images before the encode
	 * @param filename
	 */
	//this is for Q
	@Override
	public void render(Playlist playlist) {

		
		try {
			decoder = video.getDecoder();
			decoder2 = video2.getDecoder();	
			decoder3 = video3.getDecoder();	
			decoder4 = video4.getDecoder();	
			decoder5 = video5.getDecoder();	
			decoder6 = video6.getDecoder();	
			decoder7 = video7.getDecoder();	
			decoder8 = video8.getDecoder();	
			decoder9 = video9.getDecoder();
			decoder10 = video10.getDecoder();	
			writer = getWriter(outFilename);
			makeTheBits();		
			renderNextVid(decoder);
			resetTheBits();
			renderNextVid(decoder2);
			resetTheBits();
			renderNextVid(decoder3);
			resetTheBits();
			renderNextVid(decoder4);
			resetTheBits();
			renderNextVid(decoder5);
			resetTheBits();
			renderNextVid(decoder6);
			resetTheBits();
			renderNextVid(decoder7);
			resetTheBits();
			renderNextVid(decoder8);
			resetTheBits();
			renderNextVid(decoder9);
			resetTheBits();
			renderNextVid(decoder10);
			
		} catch (Exception ex) { //TODO: what ANY exception? Why aren't we saying we throw any then?
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (RuntimeException ex) { //TODO: only a runtime?!?!
					ex.printStackTrace();
				}
			}
			if (video != null) video.close();

		}
	}
	
	
	
	
	public void renderNextVid(Decoder decoder) throws Exception {
		long frame = 0;
	    long lastFrame = video.getNumVidFrames();
	    timecode =  timecodeFromVideoOne + decoder.getVideoTimeStamp();
		while (decoder.hasNextPacket()) {
			if (decoder.getVideoFrame() != null) {frame++;} // don't increase counter if not a video frame
			
			if (decoder.getAudioSamples() != null) {
				writer.encodeAudio(video.getAudioStreamIndex(), decoder.getAudioSamples());
			}
			
			BufferedImage videoFrame = decoder.getVideoFrame();						
			if (videoFrame != null) {
				timecode =  timecodeFromVideoOne + decoder.getVideoTimeStamp();
				//System.out.println("Duration of logo: " + theme.getLogo().getDuration(video.getFrameRateDivisor()));
				System.out.println("at video timestamp: " + decoder.getVideoTimeStamp() + " - formattted: "+ decoder.getFormattedVideoTimestamp());
				System.out.println("at timecode: " + timecode);
				
				putTheBitsOn(videoFrame);
				
				writer.encodeVideo(0, videoFrame, timecode, TimeUnit.MICROSECONDS); //TODO: sort out the naming of videoFrame and Composite. THAT'S confusing!
				
			}
			if ((frame +1) >= lastFrame) {break; }
		}
		timecodeFromVideoOne =  decoder.getVideoTimeStamp();
	}
	
	
	public void makeTheBits() {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		transitionCompositor = new ImageCompositor(theme.getTransition());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		numberText = new TextCompositor("5", 72, 337);
		trackText = new TextCompositor("This is the track", 100, 380);
		artistText = new TextCompositor("This is the artist", 100, 420);
		chartText = new TextCompositor("Classics of the 80's", 515, 75);
		chartText.setTextFont(new Font("Arial Narrow",1,18));		
	}
	
	public void putTheBitsOn(BufferedImage videoFrame) throws Exception{
		composite = videoFrame;
		
		composite = logoCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getLogo().getInDuration(),video.getVidStreamDuration() - theme.getLogo().getInDuration() - theme.getLogo().getOutDuration(), composite);
		//composite = logoCompositor.overlayNextImage(decoder.getTimeStamp(),0,16680, composite);
		
		composite = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),7000000, 11000000, composite);
		composite = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),150000000, 20000000, composite);//composite);
		composite = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getChart().getInDuration() + 1000000, 10000000, composite);
		composite = transitionCompositor.overlayNextImage(decoder.getVideoTimeStamp(),0, 4000000, composite);
		composite = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),5000000, 10000000, composite);
		composite = numberText.overlayNextFontFrame(numbersCompositor.isImOut(), composite);
		composite = trackText.overlayNextFontFrame(strapCompositor.isImOut(), composite);
		composite = artistText.overlayNextFontFrame(strapCompositor.isImOut(), composite);
		composite = chartText.overlayNextFontFrame(chartCompositor.isImOut(), composite);
		
	}
	
	public void resetTheBits() {
		logoCompositor.resetFileUNC();
		strapCompositor.resetFileUNC();
		strapCompositor2.resetFileUNC();
		chartCompositor.resetFileUNC();
		transitionCompositor.resetFileUNC();
		numbersCompositor.resetFileUNC();
		//numberText.resetFileUNC();
		//trackText.resetFileUNC();
		//artistText.resetFileUNC();
		//chartText.resetFileUNC();
		//chartText.resetFileUNC();
	}
	
	
	
	
	
	//Kiss Urban big beat chart
	/*@Override
	public void render() {

		IMediaWriter writer = null;
		try {
			decoder = video.getDecoder();
			writer = getWriter(outFilename);
			long frame = 0;
			long lastFrame = video.getNumVidFrames();
			ImageCompositor logoCompositor = new ImageCompositor(theme.getLogo());
			ImageCompositor strapCompositor = new ImageCompositor(theme.getStrap());
			ImageCompositor strapCompositor2 = new ImageCompositor(theme.getStrap());
			ImageCompositor chartCompositor = new ImageCompositor(theme.getChart());
			ImageCompositor transitionCompositor = new ImageCompositor(theme.getTransition());
			ImageCompositor numbersCompositor = new ImageCompositor(theme.getNumbers());
			TextCompositor trackText = new TextCompositor("This is the track", 270, 480);	
			TextCompositor artistText = new TextCompositor("This is the artist", 300, 500);
			
			
			
			while (decoder.hasNextPacket()) {
				if (decoder.getVideoFrame() != null) {frame++;} // don't increase counter if not a video frame

				IAudioSamples audioSamples = decoder.getAudioSamples();
				if (audioSamples != null) {
					writer.encodeAudio(video.getAudioStreamIndex(), audioSamples);
				}
				
				BufferedImage videoFrame = decoder.getVideoFrame(); //TODO: here they are they need to be somewhere else!!!!								
				if (videoFrame != null) {
					//System.out.println("Duration of logo: " + theme.getLogo().getDuration(video.getFrameRateDivisor()));
					System.out.println("at video timestamp: " + decoder.getTimeStamp() + " - formattted: "+ decoder.getFormattedTimestamp());
					composite = videoFrame;
					composite = logoCompositor.overlayNextImage(decoder.getTimeStamp(),theme.getLogo().getInDuration(),video.getVidStreamDuration() - 4000, composite);
					
					composite = strapCompositor.overlayNextImage(decoder.getTimeStamp(),2000, 10000, composite);
					composite = strapCompositor2.overlayNextImage(decoder.getTimeStamp(),15000, 2000, composite);
					composite = chartCompositor.overlayNextImage(decoder.getTimeStamp(),2000, 5000, composite);
					composite = transitionCompositor.overlayNextImage(decoder.getTimeStamp(),2000, theme.getTransition().getDuration(25), composite);
					composite = numbersCompositor.overlayNextImage(decoder.getTimeStamp(),2000, 7000, composite);
					composite = trackText.overlayNextFontFrame(strapCompositor.isImOut(), composite);
					composite = artistText.overlayNextFontFrame(strapCompositor.isImOut(), composite);
					composite = trackText.overlayNextFontFrame(strapCompositor2.isImOut(), composite);
					composite = artistText.overlayNextFontFrame(strapCompositor2.isImOut(), composite);
					
					writer.encodeVideo(0, composite, decoder.getTimeStamp(), TimeUnit.MILLISECONDS);
				}
				if ((frame +1) >= lastFrame) { break; }
			}


		} catch (Exception ex) { //TODO: what ANY exception? Why aren't we saying we throw any then?
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (RuntimeException ex) { //TODO: only a runtime?!?!
					ex.printStackTrace();
				}
			}
			if (video != null) video.close();

		}
	}*/
	
	//this render saved as it has perfect settings for pop chart
	/*@Override
	public void render() {

		IMediaWriter writer = null;
		try {
			decoder = video.getDecoder();
			writer = getWriter(outFilename);
			long frame = 0;
			long lastFrame = video.getNumVidFrames();
			ImageCompositor strapCompositor = new ImageCompositor(theme.getStrap());
			ImageCompositor strapCompositor2 = new ImageCompositor(theme.getStrap());
			ImageCompositor logoCompositor = new ImageCompositor(theme.getLogo());
			ImageCompositor chartCompositor = new ImageCompositor(theme.getChart());
			ImageCompositor numbersCompositor = new ImageCompositor(theme.getNumbers());
			
			TextCompositor numberCompositor = new TextCompositor("5", 285,490);
			numberCompositor.setTextFont(new Font("Arial Narrow",1,55));
			
			TextCompositor trackCompositor = new TextCompositor("This is the track", 390, 460);
			
			TextCompositor artistCompositor = new TextCompositor("This is the artist", 380, 500);
		
			TextCompositor chartTextCompositor = new TextCompositor("THIS WEEK'S FRESH MUSIC", 120, 75);
			chartTextCompositor.setTextFont(new Font("Arial Narrow",1,18));
		
			
			
			
			while (decoder.hasNextPacket()) {
				if (decoder.getVideoFrame() != null) {frame++;} // don't increase counter if not a video frame

				IAudioSamples audioSamples = decoder.getAudioSamples();
				if (audioSamples != null) {
					writer.encodeAudio(video.getAudioStreamIndex(), audioSamples);
				}
				
				BufferedImage videoFrame = decoder.getVideoFrame(); //TODO: here they are they need to be somewhere else!!!!								
				if (videoFrame != null) {
					//System.out.println("Duration of logo: " + theme.getLogo().getDuration(video.getFrameRateDivisor()));
					System.out.println("at video timestamp: " + decoder.getTimeStamp() + " - formattted: "+ decoder.getFormattedTimestamp());
					composite = videoFrame;
					composite = logoCompositor.overlayNextImage(decoder.getTimeStamp(),theme.getLogo().getInDuration(),video.getVidStreamDuration() - 8000, composite);
					//TODO: something wrong in line above with out duration - it should have ample time to fade off but doesn't...
					composite = strapCompositor.overlayNextImage(decoder.getTimeStamp(),3000, 5000, composite);
					composite = strapCompositor2.overlayNextImage(decoder.getTimeStamp(),14000, 2000, composite);//composite);
					composite = chartCompositor.overlayNextImage(decoder.getTimeStamp(),2000, 2000, composite);
					composite = numbersCompositor.overlayNextImage(decoder.getTimeStamp(),10000, 2000, composite);
					
					composite = numberCompositor.overlayNextFontFrame(strapCompositor.isImOut(), composite);
					composite = trackCompositor.overlayNextFontFrame(strapCompositor.isImOut(), composite);
					composite = artistCompositor.overlayNextFontFrame(strapCompositor.isImOut(), composite);
					
					
					composite = numberCompositor.overlayNextFontFrame(strapCompositor2.isImOut(), composite);
					composite = trackCompositor.overlayNextFontFrame(strapCompositor2.isImOut(), composite);
					composite = artistCompositor.overlayNextFontFrame(strapCompositor2.isImOut(), composite);
					
					composite = chartTextCompositor.overlayNextFontFrame(logoCompositor.isImOut(), composite);
					
					writer.encodeVideo(0, composite, decoder.getTimeStamp(), TimeUnit.MILLISECONDS);
				}
				if ((frame +1) >= lastFrame) { break; }
			}


		} catch (Exception ex) { //TODO: what ANY exception? Why aren't we saying we throw any then?
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (RuntimeException ex) { //TODO: only a runtime?!?!
					ex.printStackTrace();
				}
			}
			if (video != null) video.close();

		}
	}*/
	

	/**
	 * This is called by render(). It makes a new writer from the tool factory, adds a video and audio stream to it, and returns it
	 * @param filename
	 */
	@Override
	public MediaWriter getWriter(String filename) {
		IMediaWriter writer = ToolFactory.makeWriter(filename);
		addVideoStreamTo(writer);
		IStreamCoder audioCodec = video.getAudioCoder();
		if (audioCodec != null) {addAudioStreamTo(writer, audioCodec);}
		return new MediaWriterXuggle(writer);
	}

	/**
	 * This is called by getWriter(). It adds the video stream to the MediWriter you pass in i.e.: so its ready for writing out 
	 * At the time rate and using the codec the class specifies
	 * @param writer
	 */
	@Override
	public void addVideoStreamTo(IMediaWriter writer) {
		IRational frameRate = IRational.make(video.getFramesPerSecondAsDouble());
		int outputWidth = video.getWidth();
		int outputHeight = video.getHeight();
		writer.addVideoStream(video.getVideoStreamIndex(),video.getVideoStreamID(),video.getVideoCodecID(),frameRate,outputWidth,outputHeight);
	}

	/**
	 * This is called by getWriter(). It adds the audio stream to the MediWriter you pass in i.e.: so its ready for writing out 
	 * using the codec that get's passed to it. At the time rate and using the codec the class specifies
	 * @param writer
	 * @param audioCodec
	 */
	@Override
	public void addAudioStreamTo(IMediaWriter writer, IStreamCoder audioCodec) {//TODO: what's the point of passing the codec in but having the other things fields?
		int numAudioChannels = audioCodec.getChannels();
		int audioSampleRate = audioCodec.getSampleRate();
		ICodec.ID codecId = audioCodec.getCodecID();
		writer.addAudioStream(video.getAudioStreamIndex(),video.getAudioStreamID(),codecId,numAudioChannels,audioSampleRate);
	}
}


