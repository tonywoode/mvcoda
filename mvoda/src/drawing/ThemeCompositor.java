package drawing;

import java.awt.image.BufferedImage;

import media.Decoder;
import media.MusicVideo;
import playlist.PlaylistEntry;
import themes.GFXElementException;
import themes.Theme;
import util.FrameRate;

/**
 * Arranges which elements in a theme go where on screen. This class should be replaced with generic calls and 
 * injections from the elements themselves and from theme options, as it only contains data and a lot of repetition.
 * @author tony
 *
 */
public class ThemeCompositor {

	/**
	 * An image compositor to be used with a logo
	 */
	private ImageCompositor logoCompositor;
	
	/**
	 * An image compositor to be used with a strap
	 */
	private ImageCompositor strapCompositor;
	
	/**
	 * An image compositor to be used with a 2nd strap
	 */
	private ImageCompositor strapCompositor2;
	
	/**
	 * An image compositor to be used with a chart name graphic
	 */
	private ImageCompositor chartCompositor;
	
	/**
	 * An image compositor to be used with a trainsition sequence
	 */
	private ImageCompositor transitionCompositor;
	
	/**
	 * An image compositor to be used with a numbers element
	 */
	private ImageCompositor numbersCompositor;
	
	/**
	 * An image compositor to be used with a special GFX element
	 */
	private ImageCompositor specialCompositor;
	
	/**
	 * A text compositor to be used to render numbers 
	 */
	private TextCompositor numberText;
	
	/**
	 * A text compositor to be used to render track names
	 */
	private TextCompositor trackText;
	
	/**
	 * A text compositor to be used to render artist names
	 */
	private TextCompositor artistText;
	
	/**
	 * A text compositor to be used to redner track info
	 */
	private TextCompositor trackInfo;
	
	/**
	 * A text compositor to be used to render chart names
	 */
	private TextCompositor chartText;
	
	/**
	 * A Theme ie: set of GFX elements
	 */
	private Theme theme;
	
	/**
	 * The time basis by which to multiply seconds, obtained from the FrameRate class
	 */
	private long tb = FrameRate.getTimeBasis();

	/*
	 * ALL SINGLULAR 1-3 DIGIT MAGIC NUMBERS IN THIS CLASS ARE ONSCREEN COORDINATES, WHEREAS ALL (MAGIC NUMBER * TB) ARE TIMECODES IN SECONDS
	 */

	/**
	 * Must take a theme
	 * @param theme the theme to arrange
	 *
	 */
	public ThemeCompositor(Theme theme) { this.theme = theme; }

	/**
	 * Deals with compositing the three included themes and the possibility of a default render
	 * @param playlistEntry an entry in the playlist ie: music vid plus metadata
	 */
	public void makeThemeElements(PlaylistEntry playlistEntry, String chartName)  {
		//theme order = 0=classic 1=pop 2=urban
		switch (theme.getIndex()) {
		case 0:  setThemeOptionsClassic(playlistEntry, chartName); break;
		case 1:  setThemeOptionsPop(playlistEntry, chartName); break;
		case 2:  setThemeOptionsUrban(playlistEntry, chartName); break;
		default: setThemeOptionsDefault(playlistEntry, chartName);	break;
		}

	}
	/**
	 * Sets params for a default Theme
	 * @param playlistEntry
	 */
	private void setThemeOptionsDefault(PlaylistEntry playlistEntry, String chartName) {
		if (theme.getLogo() != null) logoCompositor = new ImageCompositor(theme.getLogo());
		if (theme.getStrap() != null) strapCompositor = new ImageCompositor(theme.getStrap());
		if (theme.getStrap() != null) strapCompositor2 = new ImageCompositor(theme.getStrap());
		if (theme.getChart() != null) chartCompositor = new ImageCompositor(theme.getChart());
		if (theme.getTransition() != null) transitionCompositor = new ImageCompositor(theme.getTransition());
		if (theme.getNumbers() != null) numbersCompositor = new ImageCompositor(theme.getNumbers());
		if (theme.getSpecial() != null) specialCompositor = new ImageCompositor(theme.getSpecial());
		artistText = new TextCompositor(playlistEntry.getArtistName(), 165, 450); //TODO: rather random text ordering requires refactor
		trackText = new TextCompositor(playlistEntry.getTrackName(), 165, 480);
		trackInfo = new TextCompositor(playlistEntry.getTrackInfo(), 165, 465);
	}

	/**
	 * Sets params for the classic theme
	 * @param playlistEntry
	 */
	private void setThemeOptionsClassic(PlaylistEntry playlistEntry, String chartName) {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		transitionCompositor = new ImageCompositor(theme.getTransition());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		//for the classic number holder, the holder is very small. We need to slot the number more to the left if its two digits
		if (playlistEntry.getPositionInPlaylist() >= 10) { numberText = new TextCompositor(Integer.toString( playlistEntry.getPositionInPlaylist() ), 69, 331); }
		else { numberText = new TextCompositor(Integer.toString( playlistEntry.getPositionInPlaylist() ), 75, 331); }
		artistText = new TextCompositor(playlistEntry.getArtistName(), 100, 377);
		trackText = new TextCompositor(playlistEntry.getTrackName(), 100, 417);
		chartText = new TextCompositor(chartName, 494, 74);	
	}

	/**
	 * Sets params for the urban theme
	 * @param playlistEntry
	 */
	private void setThemeOptionsUrban(PlaylistEntry playlistEntry, String chartName) {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		transitionCompositor = new ImageCompositor(theme.getTransition());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		artistText = new TextCompositor(playlistEntry.getArtistName(), 165, 450);	
		trackText = new TextCompositor(playlistEntry.getTrackName(), 165, 480);
		trackInfo = new TextCompositor(playlistEntry.getTrackInfo(), 165, 465);

	}

	/**
	 * Sets params for the pop theme
	 * @param playlistEntry
	 */
	private void setThemeOptionsPop(PlaylistEntry playlistEntry, String chartName) {
		logoCompositor = new ImageCompositor(theme.getLogo());
		strapCompositor = new ImageCompositor(theme.getStrap());
		strapCompositor2 = new ImageCompositor(theme.getStrap());
		chartCompositor = new ImageCompositor(theme.getChart());
		numbersCompositor = new ImageCompositor(theme.getNumbers());
		numberText = new TextNumberCompositor(Integer.toString( playlistEntry.getPositionInPlaylist() ), 285, 490);
		artistText = new TextCompositor(playlistEntry.getArtistName(), 388, 460);
		trackText = new TextCompositor(playlistEntry.getTrackName(), 378, 500);
		chartText = new TextChartCompositor(chartName, 120, 75);	
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
		default: renderDefault(videoFrame, decoder, video);
		break;
		}
	}

	/**
	 * component and timing options for rendering the default theme onscreen, based on urban theme but with a special
	 */
	private void renderDefault(BufferedImage videoFrame, Decoder decoder, MusicVideo video) {
		try {
			if (theme.getLogo() != null) videoFrame = logoCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getLogo().getInDuration(),video.getVidStreamDuration() - theme.getLogo().getInDuration() - theme.getLogo().getOutDuration(), videoFrame);	
			if (theme.getStrap() != null) videoFrame = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2 * tb, 10 * tb, videoFrame);
			if (theme.getStrap() != null) videoFrame = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),15 * tb, 2* tb, videoFrame);
			if (theme.getChart() != null) videoFrame = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(), 2 * tb, 10 * tb, videoFrame);
			if (theme.getTransition() != null) videoFrame = transitionCompositor.overlayNextImage(decoder.getVideoTimeStamp(),0 , theme.getTransition().getDuration(), videoFrame);
			if (theme.getNumbers() != null) videoFrame = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2 * tb, 7 * tb, videoFrame);
			if (theme.getSpecial() != null) videoFrame = specialCompositor.overlayNextImage(decoder.getVideoTimeStamp(),5 * tb, 4 * tb, videoFrame);
			if (theme.getStrap() != null) videoFrame = trackText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
			if (theme.getStrap() != null) videoFrame = artistText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
			if (theme.getStrap() != null) videoFrame = trackInfo.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
		} catch (GFXElementException e) { e.printStackTrace(); }
	}

	/**
	 * component and timing options for rendering the Classic theme onscreen
	 * @param videoFrame the input image to compose over
	 * @param decoder the decoder associated with the image to compose over
	 * @param video the video associated witht the image to compose over
	 */
	private void renderClassic(BufferedImage videoFrame, Decoder decoder, MusicVideo video) {

		try {
			videoFrame = logoCompositor.overlayNextImage(
					decoder.getVideoTimeStamp(),theme.getLogo().getInDuration(),
					/*the logo here is a reverse out element, therefore, since in this version of MV-CoDA we are putting the logo on and off 
					 * at the start and close of EACH video, the desired duration will be: that videos duration, minus the logo's in-animation,
					 * and also minus it's out-animation AT the factor of the speedup that has been set for the element.
					 * These figures are obtained by getOutDuration from the GFXElement itself */
					video.getVidStreamDuration() - theme.getLogo().getInDuration() - ( theme.getLogo().getOutDuration() ), videoFrame );	
			videoFrame = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),5 * tb, 2 * tb, videoFrame);
			videoFrame = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),12 * tb, 3 * tb, videoFrame);
			videoFrame = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(),theme.getChart().getInDuration() + 1 * tb, 10 * tb, videoFrame);
			videoFrame = transitionCompositor.overlayNextImage(decoder.getVideoTimeStamp(),0, 4 * tb, videoFrame);
			videoFrame = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),5 * tb, 8 * tb, videoFrame);
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
			videoFrame = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2 * tb, 10 * tb, videoFrame);
			videoFrame = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),15 * tb, 2 * tb, videoFrame);
			videoFrame = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(), 2 * tb, 10 * tb, videoFrame);
			videoFrame = transitionCompositor.overlayNextImage(decoder.getVideoTimeStamp(),0 , theme.getTransition().getDuration(), videoFrame);
			videoFrame = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2 * tb, 7 * tb, videoFrame);
			videoFrame = trackText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
			videoFrame = artistText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame);
			videoFrame = trackInfo.overlayNextFontFrame(strapCompositor2.isImOut(), videoFrame);
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
			videoFrame = strapCompositor.overlayNextImage(decoder.getVideoTimeStamp(),3 * tb, 5 * tb, videoFrame);
			videoFrame = strapCompositor2.overlayNextImage(decoder.getVideoTimeStamp(),14 * tb, 2 * tb, videoFrame);
			videoFrame = chartCompositor.overlayNextImage(decoder.getVideoTimeStamp(),2 * tb, 2 * tb, videoFrame);
			videoFrame = numbersCompositor.overlayNextImage(decoder.getVideoTimeStamp(),10 * tb, 2 * tb, videoFrame);
			videoFrame = numberText.overlayNextFontFrame(strapCompositor.isImOut(), videoFrame); //For this chart, number TEXT is tied to strap, NOT tied to number
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
	 * Resets chart elements - meaning sets the iterator of filenames back to zero for each element so the next video can be composited
	 */
	public void resetThemeElements() {
		//TODO: Elements should reset themselves
		if (logoCompositor != null) logoCompositor.resetFileUNC();
		if (strapCompositor != null) strapCompositor.resetFileUNC();
		if (strapCompositor2 != null) strapCompositor2.resetFileUNC();
		if (chartCompositor != null) chartCompositor.resetFileUNC();
		if (transitionCompositor != null) transitionCompositor.resetFileUNC(); 
		if (numbersCompositor != null) numbersCompositor.resetFileUNC();

	}




}
