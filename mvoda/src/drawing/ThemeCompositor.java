package drawing;

import java.awt.Font;
import java.awt.image.BufferedImage;

import media.Decoder;
import media.MusicVideo;
import playlist.PlaylistEntry;
import themes.Theme;

/**
 * Arranges which elements in a theme go where on screen
 * @author tony
 *
 */
public class ThemeCompositor {

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
	private Theme theme;

	/**
	 * Must take a theme
	 * @param theme the theme to arrange
	 *
	 */
	public ThemeCompositor(Theme theme) {
		this.theme = theme;
	}

	/**
	 * Deals with compsiting the three included themes and the possibility of a default render
	 * @param playlistEntry
	 */
	public void makeThemeElements(PlaylistEntry playlistEntry) {
		//theme order = 0=classic 1=pop 2=urban
		switch (theme.getIndex()) {
		case 0:  makeTheBitsClassic(playlistEntry); break;
		case 1:  makeTheBitsPop(playlistEntry); break;
		case 2:  makeTheBitsUrban(playlistEntry); break;
		default: makeTheBitsDefault(playlistEntry);	break;
		}

	}

	private void makeTheBitsDefault(PlaylistEntry playlistEntry) {
		//TODO default
	}

	private void makeTheBitsClassic(PlaylistEntry playlistEntry) {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		transitionCompositor = new ImageCompositor(theme.getTransition());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		//for the classic number holder, we need to slot the number more to the left if its two digits
		if (playlistEntry.getPositionInPlaylist() >= 10) { numberText = new TextCompositor(Integer.toString( playlistEntry.getPositionInPlaylist() ), 67, 337); }
		else { numberText = new TextCompositor(Integer.toString( playlistEntry.getPositionInPlaylist() ), 73, 337); }
		numberText.setTextFont(new Font("Arial Narrow",1,30)); //TODO: the track and artist text are a per playlist entry thing, the others are PER PLAYLIST so don't need to run each time
		trackText = new TextCompositor(playlistEntry.getTrackName(), 100, 380);
		artistText = new TextCompositor(playlistEntry.getArtistName(), 100, 420);
		chartText = new TextCompositor("Classics of the 80's", 515, 75);
		chartText.setTextFont(new Font("Arial Narrow",1,18));		
	}

	private void makeTheBitsUrban(PlaylistEntry playlistEntry) {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		transitionCompositor = new ImageCompositor(theme.getTransition());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		trackText = new TextCompositor(playlistEntry.getTrackName(), 270, 485);
		artistText = new TextCompositor(playlistEntry.getArtistName(), 300, 525);		
	}


	private void makeTheBitsPop(PlaylistEntry playlistEntry) {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		numberText = new TextCompositor(Integer.toString( playlistEntry.getPositionInPlaylist() ), 285, 490);
		numberText.setTextFont(new Font("Arial Narrow",1,55));
		trackText = new TextCompositor(playlistEntry.getTrackName(), 390, 460);
		artistText = new TextCompositor(playlistEntry.getArtistName(), 380, 500);
		chartText = new TextCompositor("This Week's Fresh Music", 120, 75);
		chartText.setTextFont(new Font("Arial Narrow", 1, 18));		
	}


	public void renderThemeElements(BufferedImage videoFrame, Decoder decoder, MusicVideo video) throws Exception { //TODO: exception
		//theme order = 0=classic 1=pop 2=urban
		switch (theme.getIndex()) {
		case 0:  putTheBitsOnClassic(videoFrame, decoder, video);
		break;
		case 1:  putTheBitsOnPop(videoFrame, decoder, video);
		break;
		case 2:  putTheBitsOnUrban(videoFrame, decoder, video);
		break;
		default: putTheBitsOnDefault();
		break;
		}
	}

	private void putTheBitsOnDefault() {
		//TODO default
	}

	private void putTheBitsOnClassic(BufferedImage videoFrame, Decoder decoder, MusicVideo video) throws Exception{//TODO: exception

		videoFrame = logoCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getLogo().getInDuration(),video.getVidStreamDuration() - theme.getLogo().getInDuration() - theme.getLogo().getOutDuration(), videoFrame);	
		videoFrame = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),5000000, 3000000, videoFrame);
		videoFrame = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),14000000, 3000000, videoFrame);
		videoFrame = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getChart().getInDuration() + 1000000, 10000000, videoFrame);
		videoFrame = transitionCompositor.overlayNextImage(decoder.getVideoTimeStamp(),0, 4000000, videoFrame);
		videoFrame = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),5000000, 8000000, videoFrame);
		videoFrame = numberText.overlayNextFontFrame(numbersCompositor.isImOut(), videoFrame);
		videoFrame = trackText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
		videoFrame = artistText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
		videoFrame = chartText.overlayNextFontFrame(chartCompositor.isImOut(), videoFrame);
		videoFrame = trackText.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
		videoFrame = artistText.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
	}

	private void putTheBitsOnUrban(BufferedImage videoFrame, Decoder decoder, MusicVideo video) throws Exception{//TODO: exception

		videoFrame = logoCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getLogo().getInDuration(),video.getVidStreamDuration() - theme.getLogo().getInDuration() - theme.getLogo().getOutDuration(), videoFrame);	
		videoFrame = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2000000, 10000000, videoFrame);
		videoFrame = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),15000000, 2000000, videoFrame);
		videoFrame = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(), 2000000, 10000000, videoFrame);
		videoFrame = transitionCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2000, theme.getTransition().getDuration(25), videoFrame);
		videoFrame = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2000000, 7000000, videoFrame);
		videoFrame = trackText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
		videoFrame = artistText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
		videoFrame = trackText.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
		videoFrame = artistText.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
	}


	private void putTheBitsOnPop(BufferedImage videoFrame, Decoder decoder, MusicVideo video) throws Exception{//TODO: exception

		videoFrame = logoCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getLogo().getInDuration(),video.getVidStreamDuration() - theme.getLogo().getInDuration() - theme.getLogo().getOutDuration(), videoFrame);
		videoFrame = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),3000000, 5000000, videoFrame);
		videoFrame = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),14000000, 2000000, videoFrame);
		videoFrame = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2000000, 2000000, videoFrame);
		videoFrame = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),10000000, 2000000, videoFrame);
		videoFrame = numberText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame); //THIS number is tied to strap NOT number
		videoFrame = trackText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
		videoFrame = artistText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
		videoFrame = chartText.overlayNextFontFrame(chartCompositor.isImOut(), videoFrame);
		videoFrame = numberText.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
		videoFrame = trackText.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
		videoFrame = artistText.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
		videoFrame = chartText.overlayNextFontFrame(logoCompositor.isImOut(), videoFrame);
	}


	public void resetThemeElements() {
		//theme order = 0=classic 1=pop 2=urban
		switch (theme.getIndex()) {
		case 0:  resetTheBits(); break;
		case 1:  resetTheBitsPop(); break; //TODO: numbers has no transition - should be a null condition. Also elements should reset themselves
		case 2:  resetTheBits(); break;
		default: resetTheBits(); break;
		}

	}

	private void resetTheBits() {
		logoCompositor.resetFileUNC();
		strapCompositor.resetFileUNC();
		strapCompositor2.resetFileUNC();
		chartCompositor.resetFileUNC();
		transitionCompositor.resetFileUNC();
		numbersCompositor.resetFileUNC();
	}

	private void resetTheBitsPop() {
		logoCompositor.resetFileUNC();
		strapCompositor.resetFileUNC();
		strapCompositor2.resetFileUNC();
		chartCompositor.resetFileUNC();
		numbersCompositor.resetFileUNC();

	}


}
