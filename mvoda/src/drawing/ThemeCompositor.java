package drawing;

import java.awt.image.BufferedImage;

import media.Decoder;
import media.MusicVideo;
import playlist.PlaylistEntry;
import themes.Theme;
import view.GFXElementException;

/**
 * Arranges which elements in a theme go where on screen. This class should be replaced with generic calls and 
 * injections as it only contains data and a lot of repetition
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
	 * Deals with compositing the three included themes and the possibility of a default render
	 * @param playlistEntry an entry in the playlist ie: music vid plus metadata
	 */
	public void makeThemeElements(PlaylistEntry playlistEntry) {
		//theme order = 0=classic 1=pop 2=urban
		switch (theme.getIndex()) {
		case 0:  setThemeOptionsClassic(playlistEntry); break;
		case 1:  setThemeOptionsPop(playlistEntry); break;
		case 2:  setThemeOptionsUrban(playlistEntry); break;
		default: makeTheBitsDefault(playlistEntry);	break;
		}

	}
	/**
	 * Sets params for a default Theme
	 * @param playlistEntry
	 */
	private void makeTheBitsDefault(PlaylistEntry playlistEntry) {
		//TODO DEFAULT SETTINGS FOR A NEW THEME
	}

	/**
	 * Sets params for the classic theme
	 * @param playlistEntry
	 */
	private void setThemeOptionsClassic(PlaylistEntry playlistEntry) {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		transitionCompositor = new ImageCompositor(theme.getTransition());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		//for the classic number holder, we need to slot the number more to the left if its two digits
		if (playlistEntry.getPositionInPlaylist() >= 10) { numberText = new TextCompositor(Integer.toString( playlistEntry.getPositionInPlaylist() ), 67, 337); }
		else { numberText = new TextCompositor(Integer.toString( playlistEntry.getPositionInPlaylist() ), 73, 337); }
		//TextCompositor.setTextFont(new Font("Arial Narrow",1,30)); 
		trackText = new TextCompositor(playlistEntry.getTrackName(), 100, 380);
		artistText = new TextCompositor(playlistEntry.getArtistName(), 100, 420);
		chartText = new TextCompositor("Classics of the 80's", 515, 75);
		//TextCompositor.setTextFont(new Font("Arial Narrow",1,18));		
	}

	/**
	 * Sets params for the urban theme
	 * @param playlistEntry
	 */
	private void setThemeOptionsUrban(PlaylistEntry playlistEntry) {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		transitionCompositor = new ImageCompositor(theme.getTransition());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		trackText = new TextCompositor(playlistEntry.getTrackName(), 270, 485);
		artistText = new TextCompositor(playlistEntry.getArtistName(), 300, 525);		
	}

	/**
	 * Sets params for the pop theme
	 * @param playlistEntry
	 */
	private void setThemeOptionsPop(PlaylistEntry playlistEntry) {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		numberText = new TextCompositor(Integer.toString( playlistEntry.getPositionInPlaylist() ), 285, 490);
		//TextCompositor.setTextFont(new Font("Arial Narrow",1,55));
		trackText = new TextCompositor(playlistEntry.getTrackName(), 390, 460);
		artistText = new TextCompositor(playlistEntry.getArtistName(), 380, 500);
		chartText = new TextCompositor("This Week's Fresh Music", 120, 75);
		//TextCompositor.setTextFont(new Font("Arial Narrow", 1, 18));		
	}

	/**
	 * makes GFX rendering decisions using the data provided in this class
	 * @param videoFrame overlay from video file
	 * @param decoder decoder assoicated with file
	 * @param video video associated with file
	 * @throws Exception
	 */
	public void renderThemeElements(BufferedImage videoFrame, Decoder decoder, MusicVideo video)  {
		//theme order = 0=classic 1=pop 2=urban
		switch (theme.getIndex()) {
		case 0:  renderClassic(videoFrame, decoder, video);
		break;
		case 1:  renderPop(videoFrame, decoder, video);
		break;
		case 2:  renderUrban(videoFrame, decoder, video);
		break;
		default: renderDefault();
		break;
		}
	}

	/**
	 * component and timing options for rendering the default theme onscreen
	 */
	private void renderDefault() {
		//TODO DEFAULT
	}

	/**
	 * component and timing options for rendering the Classic theme onscreen
	 * @param videoFrame the input image to compose over
	 * @param decoder the decoder associated with the image to compose over
	 * @param video the video associated witht the image to compose over
	 */
	private void renderClassic(BufferedImage videoFrame, Decoder decoder, MusicVideo video) {

		try {
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
		} catch (GFXElementException e) { e.printStackTrace(); }
	}

	/**
	 * component and timing options for rendering the Urban theme onscreen
	 * @param videoFrame the input image to compose over
	 * @param decoder the decoder associated with the image to compose over
	 * @param video the video associated witht the image to compose over
	 */
	private void renderUrban(BufferedImage videoFrame, Decoder decoder, MusicVideo video) {

		try {
			videoFrame = logoCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getLogo().getInDuration(),video.getVidStreamDuration() - theme.getLogo().getInDuration() - theme.getLogo().getOutDuration(), videoFrame);	
			videoFrame = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2000000, 10000000, videoFrame);
			videoFrame = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),15000000, 2000000, videoFrame);
			videoFrame = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(), 2000000, 10000000, videoFrame);
			videoFrame = transitionCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2000, theme.getTransition().getDuration(), videoFrame);
			videoFrame = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2000000, 7000000, videoFrame);
			videoFrame = trackText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
			videoFrame = artistText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
			videoFrame = trackText.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
			videoFrame = artistText.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
		} catch (GFXElementException e) { e.printStackTrace(); }
	}

	/**
	 * component and timing options for rendering the Pop theme onscreen
	 * @param videoFrame the input image to compose over
	 * @param decoder the decoder associated with the image to compose over
	 * @param video the video associated witht the image to compose over
	 */
	private void renderPop(BufferedImage videoFrame, Decoder decoder, MusicVideo video) {

		try {
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
		} catch (GFXElementException e) { e.printStackTrace(); }
	}

	/**
	 * Decides how to reset theme elements bases on their characteristics
	 */
	public void resetThemeElements() {
		//theme order = 0=classic 1=pop 2=urban
		switch (theme.getIndex()) {
		case 0:  resetElementsDefault(); break;
		case 1:  resetElementsPop(); break; //TODO: numbers has no transition - should be a null condition. Also elements should reset themselves
		case 2:  resetElementsDefault(); break;
		default: resetElementsDefault(); break;
		}

	}

	/**
	 * Resets GFX elements generically for the next pass
	 */
	private void resetElementsDefault() {
		logoCompositor.resetFileUNC();
		strapCompositor.resetFileUNC();
		strapCompositor2.resetFileUNC();
		chartCompositor.resetFileUNC();
		transitionCompositor.resetFileUNC();
		numbersCompositor.resetFileUNC();
	}

	/**
	 * resets the pop elements, and any future elements that do not have a transistion
	 */
	//TODO: negate the need for this method 
	private void resetElementsPop() {
		logoCompositor.resetFileUNC();
		strapCompositor.resetFileUNC();
		strapCompositor2.resetFileUNC();
		chartCompositor.resetFileUNC();
		numbersCompositor.resetFileUNC();

	}


}
