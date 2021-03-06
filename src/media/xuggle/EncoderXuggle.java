package media.xuggle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import media.Decoder;
import media.Encoder;
import media.MediaOpenException;
import media.MusicVideo;
import media.types.AudioSamples;
import media.types.MediaWriter;
import media.types.StreamCoder;
import media.xuggle.types.MediaWriterXuggle;
import playlist.ChartNumberException;
import playlist.Number;
import playlist.Playlist;
import playlist.PlaylistEntry;
import themes.Theme;
import util.FrameRate;

import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

import drawing.ThemeCompositor;

/**
 * Main Encoder for MV-CoDA. We take a playlist, a theme and an ouput filename and we arrange the decoding, composition and writing of the output file
 * @author tony
 *
 */
public class EncoderXuggle implements Encoder {

	/**
	 * Holds the music video that will be worked on by this class
	 */
	private MusicVideo video;
	
	/**
	 * The output media files name
	 */
	private String outFilename;
	
	/**
	 * The decoder associated with a music video
	 */
	private Decoder decoder;
	
	/**
	 * The IMediaWriter that write the output file
	 */
	private MediaWriter writer = null;
	
	/**
	 * The compositor that will compose a theme over the individual image files
	 */
	private ThemeCompositor themeCompositor;
	
	private long offset;

	/**
	 * By passing two UNCpaths to the constructor we specify and input and an output filename
	 * @param playlist the playlist to encode
	 * @param outFilename
	 */
	public EncoderXuggle(Playlist playlist, Theme theme,String outFilename) {
		this.outFilename = outFilename;
		themeCompositor = new ThemeCompositor(theme);
		render(playlist);
	}
	
	
	/**
	 * Creates a new music video with input filename and a new writer that will write to output filename, iterates through the packets of the music video
	 * encoding both, but allowing something to happen to the buffered images before the encode
	 * @param playlist the playlist to render
	 */
	@Override public void render(Playlist playlist) {
		
		//remember this is a CHART, so we want the number one to render LAST
		ArrayList<PlaylistEntry> reversedList = new ArrayList<PlaylistEntry>(playlist.getPlaylistEntries()); //copy array
		Collections.reverse(reversedList);
		
		video = playlist.getPlaylistEntries().get(0).getVideo(); //we have to first set a video because e.g.: line 361 below needs to set properties
													//so we choose the number from chart entry number one
		
		FrameRate.setFrameRate(video.getFramesPerSecondAsDouble()); //we also use chart entry number one to set the frame rate
		
		
		writer = getWriter(outFilename);
		
		//This is so that GFX element's can lookup a live number rather than be passed a playlist entry
		try { Number.setNumber( playlist.getPlaylistEntries().size() ); } 
		catch (ChartNumberException e1) { e1.printStackTrace(); } //the number to start on is the number of items in the playlist, irrespective
		
		try {	
			for (PlaylistEntry playlistEntry : reversedList) {
				
				themeCompositor.makeThemeElements(playlistEntry, playlist.getChartName()); //create the elements properties for the theme
				themeCompositor.resetThemeElements(); //reset any timers etc in the theme from the previous video
				try { video = new MusicVideoXuggle( playlistEntry.getFileUNC() ); }  //we have to new this here or GUI can't render more than once: decoderXuggle crashes out at HasNextPacket()
				catch (MediaOpenException e) { e.printStackTrace(); }
				decoder = video.getDecoder(); //get the decoder associated with this video
				renderNextVid(decoder, video);	
				/*The next argument is for GFX elements to lookup a live number for directory search, rather than be passed a playlist entry
				 It could be changed to Number.getNumber() -1....but number is required for live lookup during renders
				 experience of onair GFX shows it is wise to be extremely defensive when setting live chart numbers */
				if ( Number.getNumber() > 1 ) try { Number.setNumber(playlistEntry.getPositionInPlaylist() - 1);} 
				catch (ChartNumberException e) { e.printStackTrace(); } 
				
			}
		} 
		finally { 
			if (writer != null) { writer.close(); } //in Xuggler, null means more packets to go
			if (video != null)  {video.close(); }
		}
	}
	
	/**
	 * When passed a decoder instance and a music video, will arrange to compose GFXElements onto the decoded video packets and write the output the the media writer
	 * Prints timestamp information to the console
	 * @param decoder the decoder instance being used
	 * @param video a music video
	 */
	private void renderNextVid(Decoder decoder, MusicVideo video) {
	    long nextAudioTimecode = 0;
	    long nextVideoTimecode = 0;
	    
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
				writer.encodeVideo(0, videoFrame, newVideoTimecode, TimeUnit.MICROSECONDS);
				
			}
		}	
		offset = Math.max(nextVideoTimecode, nextAudioTimecode); //set timecode we pass on to be the LARGER of audio and video - this helps sync when concatenating
		
	}
	

	/**
	 * This is called by render(). It makes a new writer from the tool factory, adds a video and audio stream to it, and returns it
	 * @param filename
	 */
	@Override public MediaWriter getWriter(String filename) {
		MediaWriter writer = new MediaWriterXuggle(ToolFactory.makeWriter(filename));
		addVideoStreamTo(writer);
		StreamCoder audioCodec = video.getAudioCoder();
		if (audioCodec != null) { addAudioStreamTo(writer, audioCodec); }
		return writer;
	}

	/**
	 * This is called by getWriter(). It adds the video stream to the MediWriter you pass in i.e.: so its ready for writing out 
	 * At the time rate and using the codec the class specifies
	 * @param writer
	 */
	@Override public void addVideoStreamTo(MediaWriter writer) {
			//IRational frameRate = IRational.make(video.getFramesPerSecondAsDouble()); - see Adapter
			int outputWidth = video.getWidth();
			int outputHeight = video.getHeight();
			writer.addVideoStream(video.getVideoStreamIndex(),video.getVideoStreamID(),video.getVideoCodecID(),video.getFramesPerSecondAsDouble(),outputWidth,outputHeight);
	
	}
	
	/**
	 * This is called by getWriter(). It adds the audio stream to the MediWriter you pass in i.e.: so its ready for writing out 
	 * using the codec that get's passed to it. At the time rate and using the codec the class specifies
	 * @param writer
	 * @param audioCodec
	 */
	@Override public void addAudioStreamTo(MediaWriter writer, StreamCoder audioCodec) {
		int numAudioChannels = audioCodec.getChannels();
		int audioSampleRate = audioCodec.getSampleRate();
		ICodec.ID codecId = audioCodec.getCodecID();
		writer.addAudioStream(video.getAudioStreamIndex(),video.getAudioStreamID(),codecId,numAudioChannels,audioSampleRate);
	}
}


