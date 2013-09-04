package media.xuggle;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import playlist.Playlist;
import playlist.PlaylistEntry;
import playlist.Number;
import themes.Theme;

import media.AudioSamples;
import media.Decoder;
import media.Encoder;
import media.MediaWriter;
import media.MusicVideo;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IRational;
import com.xuggle.xuggler.IStreamCoder;

import drawing.ImageCompositor;
import drawing.TextCompositor;
import drawing.ThemeCompositor;
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
	
	private MediaWriter writer = null;
	
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
	
	private ThemeCompositor themeCompositor;
	
	long offset;

	/**
	 * By passing two UNCpaths to the constructor we specify and input and an output filename
	 * @param filename
	 * @param outFilename
	 */
	public EncoderXuggle(Playlist playlist, Theme theme,String outFilename) {
		this.outFilename = outFilename;
		this.theme = theme;
		themeCompositor = new ThemeCompositor(theme);
		render(playlist);
	}
	
	

	/**
	 * Creates a new music video with input filename and a new writer that will write to output filename, iterates through the packets of the music video
	 * encoding both, but allowing something to happen to the buffered images before the encode
	 * @param filename
	 */
	@Override
	public void render(Playlist playlist) {
		
		//remember this is a CHART, so we want the number one to render LAST
		ArrayList<PlaylistEntry> reversedList = new ArrayList<PlaylistEntry>(playlist.getPlaylistEntries()); //copy array
		Collections.reverse(reversedList);
		
		video = playlist.getNextEntry(0).getVideo(); //TODO: we have to first set a video becuase eg: line 361 below needs it to set properties
		//so we choose the number from chart entry number one
		writer = getWriter(outFilename);
		
		//This is so that GFX element's can lookup a live number rather than be passed a playlist entry
		Number.setNumber( playlist.getSize() ); //the number to start on is the number of items in the playlist, irrespective
		
		try {
			//decoder2 = video2.getDecoder();		
			for (PlaylistEntry playlistEntry : reversedList) {
				
				themeCompositor.makeThemeElements(playlistEntry);
				//makeTheBitsClassic(); //if it's not in the loop you'll end up with the same number file being called each time round
				themeCompositor.resetThemeElements();
				//resetTheBits();
				video = new MusicVideoXuggle( playlistEntry.getFileUNC() ); //TODO: we have to new this up here else the GUI can't render more than once.....see decoderXuggle it crashes out at HasNextPacket()
				//decoder = new DecoderXuggle(video); //TODO: Thought this might fix problem where you can only do one render, maybe we need a new encoder too?
				decoder = video.getDecoder(); //make a new decoder at this point? Decoder temp = new Decoder(video)
				renderNextVid(decoder, video);	
				Number.setNumber(playlistEntry.getPositionInPlaylist() - 1); //This is so that GFX element's can lookup a live number for directory search rather than be passed a playlist entry
				//change it back to Number.getNumber() -1 if you want....
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
	
	
	public void renderNextVid(Decoder decoder, MusicVideo video) throws Exception {
	    //System.out.println(decoder.getFormattedAudioTimestamp());
	    //videoTimecode =  videoTimecodeFromLastVid + decoder.getVideoTimeStamp();
	    //audioTimecode = audioTimecodeFromLastVid + decoder.getAudioTimeStamp();
	    long nextAudioTimecode = 0;
	    long nextVideoTimecode = 0;
	    //long offset = 0;
		while (decoder.hasNextPacket()) {
			AudioSamples audioSamples = decoder.getAudioSamples();
			if (audioSamples != null && decoder.getVideoTimeStamp() > 0) { //the second test ensures audio MP4 packets don't go out of sync
				
				long newAudioTimecode = decoder.getAudioTimeStamp() + offset;
				nextAudioTimecode = decoder.getAudioSamples().getNextPresentationTimestamp();
				audioSamples.setTimeStamp(newAudioTimecode);
				writer.encodeAudio(video.getAudioStreamIndex(), audioSamples);
				
				System.out.printf("%7s\t%7s%15d\t\t%10s%15d\t%12s%13s\n", "AUDIO", "Offset",newAudioTimecode, "Relative:", decoder.getAudioTimeStamp(), "Formatted:", decoder.getFormattedAudioTimestamp());
			}
			
			BufferedImage videoFrame = decoder.getVideoFrame();						
			if (videoFrame != null) {
				long originalVideoTimecode = decoder.getVideoTimeStamp();
				long newVideoTimecode =  decoder.getVideoTimeStamp() + offset;
				nextVideoTimecode = originalVideoTimecode + offset;
				System.out.printf("%7s\t%7s%15d\t\t%10s%15d\t%12s%13s\n","VIDEO", "Offset", newVideoTimecode, "Relative:", decoder.getVideoTimeStamp(), "Formatted:", decoder.getFormattedVideoTimestamp());
				themeCompositor.renderThemeElements(videoFrame, decoder, video);
				//putTheBitsOnClassic(videoFrame); //!!!!!!!!!!!!!!!!!!!!!!!!HERE YOU NEED PUT THE BITS ON POP - YES POP!!!!	
				writer.encodeVideo(0, videoFrame, newVideoTimecode, TimeUnit.MICROSECONDS); //TODO: sort out the naming of videoFrame and Composite. THAT'S confusing!
				
			}
		}
		
		offset = Math.max(nextVideoTimecode, nextAudioTimecode);//set timecode we pass on to be the LARGER of audio and video - this helps sync when concatenating
		//if (videoTimecode + 1 > nextAudioTimecode) { System.out.println("The video is larger at: " + videoTimecode + 1); }
		//System.out.println("next audio timecode is predicted as" + nextAudioTimecode);
		//videoTimecodeFromLastVid =  offset;
		//audioTimecodeFromLastVid = offset;
		
	}
	

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


