package media.xuggle;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import playlist.Playlist;
import playlist.PlaylistEntry;
import xstream.Theme;

import media.Decoder;
import media.Encoder;
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
public class EncoderXuggle implements Encoder {

	private MusicVideo video;
	private String outFilename;
	private BufferedImage composite;
	private Decoder decoder;
	private Theme theme;
	
	private IMediaWriter writer = null;
	
	private long videoTimecode;
	private long videoTimecodeFromLastVid;
	private long audioTimecode;
	private long audioTimecodeFromLastVid;
	
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
	public EncoderXuggle(Playlist playlist, Theme theme,String outFilename) {
		//this.video = video;
		//this.video2 = video2;
		this.outFilename = outFilename;
		this.theme = theme;
		render(playlist);
	}
	
	
	

	/**
	 * Creates a new music video with input filename and a new writer that will write to output filename, iterates through the packets of the music video
	 * encoding both, but allowing something to happen to the buffered images before the encode
	 * @param filename
	 */
	//this is for Q
	@Override
	public void render(Playlist playlist) {
		
		video = playlist.getNextEntry(0).getVideo(); //TODO: bummer we have to first set a video becuase eg: line 361 below needs it to set properties
		writer = getWriter(outFilename);
		
		try {
			//decoder2 = video2.getDecoder();	
			
			makeTheBitsPop();
			
			for (PlaylistEntry playlistEntry : playlist.getPlaylistEntries()) {	
				resetTheBitsPop();
				video = playlistEntry.getVideo(); 
				decoder = video.getDecoder(); //make a new decoder at this point? Decoder temp = new Decoder(video)
				renderNextVid(decoder);	
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
	}
	
	
	
	
	public void renderNextVid(Decoder decoder) throws Exception {
		//long frame = 0;
	    //long lastFrame = video.getNumVidFrames();
	    //System.out.println(decoder.getFormattedAudioTimestamp());
	    videoTimecode =  videoTimecodeFromLastVid + decoder.getVideoTimeStamp();
	    audioTimecode = audioTimecodeFromLastVid + decoder.getAudioTimeStamp();
		while (decoder.hasNextPacket()) {
			//if (decoder.getVideoFrame() != null) {frame++;} // don't increase counter if not a video frame
			IAudioSamples audioSamples = decoder.getAudioSamples();
			if (audioSamples != null) {
				audioTimecode = audioTimecodeFromLastVid + decoder.getAudioTimeStamp();
				audioSamples.setTimeStamp(audioTimecode);
				writer.encodeAudio(video.getAudioStreamIndex(), audioSamples);
				
				System.out.printf("%7s%15d%10s%15d%12s%13s\n", "AUDIO:", audioTimecode, "Relative:", decoder.getAudioTimeStamp(), "Formatted:", decoder.getFormattedAudioTimestamp());
				//System.out.println("at relative Audio timecode: " + audioTimecode);
			}
			
			BufferedImage videoFrame = decoder.getVideoFrame();						
			if (videoFrame != null) {
				videoTimecode =  videoTimecodeFromLastVid + decoder.getVideoTimeStamp();
			
				System.out.printf("%7s%15d%10s%15d%12s%13s\n","VIDEO:", videoTimecode, "Relative:", decoder.getVideoTimeStamp(), "Formatted:", decoder.getFormattedVideoTimestamp());
				//System.out.println("Combined Video timecode: " + videoTimecode);
				
				putTheBitsOnPop(videoFrame); //!!!!!!!!!!!!!!!!!!!!!!!!HERE YOU NEED PUT THE BITS ON POP - YES POP!!!!
				
				writer.encodeVideo(0, videoFrame, videoTimecode, TimeUnit.MICROSECONDS); //TODO: sort out the naming of videoFrame and Composite. THAT'S confusing!
				
			}
			//if ((frame +1) >= lastFrame) {System.out.println("ITS THE LAST FRAME!!!!!!!!!!!!!!!!!!!!!!!!!!!!");break; }
		}
		long offset = Math.max(videoTimecode, audioTimecode);//set timecode we pass on to be the LARGER of audio and video - this helps sync when concatenating
		videoTimecodeFromLastVid =  offset;
		audioTimecodeFromLastVid = offset;
		
	}
	
	
	public void makeTheBitsClassic() {
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
	
	public void makeTheBitsUrban() {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		transitionCompositor = new ImageCompositor(theme.getTransition());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		trackText = new TextCompositor("This is the track", 270, 480);
		artistText = new TextCompositor("This is the artist", 300, 500);
		chartText = new TextCompositor("Classics of the 80's", 515, 75);
		chartText.setTextFont(new Font("Arial Narrow",1,18));		
	}
	
	
	public void makeTheBitsPop() {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		numberText = new TextCompositor("5", 285, 490);
		trackText = new TextCompositor("This is the track", 390, 460);
		artistText = new TextCompositor("This is the artist", 380, 500);
		chartText = new TextCompositor("This Week's Fresh Music", 515, 75);
		chartText.setTextFont(new Font("Arial Narrow",1, 18));		
	}
	
	
	public void putTheBitsOn(BufferedImage videoFrame) throws Exception{
		composite = videoFrame;
		
		composite = logoCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getLogo().getInDuration(),video.getVidStreamDuration() - theme.getLogo().getInDuration() - theme.getLogo().getOutDuration(), composite);
		//composite = logoCompositor.overlayNextImage(decoder.getTimeStamp(),0,16680, composite);
		
		composite = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),7000000, 10000000, composite);
		composite = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),15000000, 2000000, composite);//composite);
		composite = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getChart().getInDuration() + 1000000, 10000000, composite);
		composite = transitionCompositor.overlayNextImage(decoder.getVideoTimeStamp(),0, 4000000, composite);
		composite = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),5000000, 10000000, composite);
		//composite = numberText.overlayNextFontFrame(numbersCompositor.isImOut(), composite); //!!!!!PROBABLY NEED THIS FOR CLASSIC BUT NOT FOR URBAN
		composite = trackText.overlayNextFontFrame(strapCompositor.isImOut(), composite);
		composite = artistText.overlayNextFontFrame(strapCompositor.isImOut(), composite);
		composite = chartText.overlayNextFontFrame(chartCompositor.isImOut(), composite);
	}
	
	
	public void putTheBitsOnPop(BufferedImage videoFrame) throws Exception{
		composite = videoFrame;
		
		composite = logoCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getLogo().getInDuration(),video.getVidStreamDuration() - theme.getLogo().getInDuration() - theme.getLogo().getOutDuration(), composite);
		composite = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),3000000, 5000000, composite);
		composite = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),14000000, 2000000, composite);//composite);
		composite = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getChart().getInDuration() + 1000000, 10000000, composite);
		composite = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),10000000, 2000000, composite);
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
	
	public void resetTheBitsPop() {
		logoCompositor.resetFileUNC();
		strapCompositor.resetFileUNC();
		strapCompositor2.resetFileUNC();
		chartCompositor.resetFileUNC();
		//transitionCompositor.resetFileUNC();
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
	public IMediaWriter getWriter(String filename) {
		IMediaWriter writer = ToolFactory.makeWriter(filename);
		addVideoStreamTo(writer);
		IStreamCoder audioCodec = video.getAudioCoder();
		if (audioCodec != null) {addAudioStreamTo(writer, audioCodec);}
		return writer;
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


