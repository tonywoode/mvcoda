package themes;

import util.FrameRate;
import util.XMLReader;
import util.XMLWriter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import lombok.Getter;
import lombok.Setter;

/**
 * Describes a GFXElement that has motion therefore needs iteration control over a music video.
 * In addition to textual elements such as name, author etc, an animated element has:
 *  	FrameData: information about which frames in the sequence are the animate-in/out or hold points
 *  	CoOrdinates: an element may be composited appropriately for a video picture, or may need positioning
 *  	Animation data: an element may need to be looped in its middle "holding" point, or may reverse to animate-out
 * The class is serialisable and each GFXElement is combined into a Theme on disk - see {@link XMLReader} {@link XMLWriter}
 * @author tony
 *
 */
@XStreamAlias("AnimatedGFXElement") public class AnimatedGFXElement extends GFXElement {

/**
 * The length of time it takes this element to reach its hold point
 */
	@XStreamOmitField private long inDuration;
	
	/**
	 * The length of time it takes this element to end its hold point and animate offscreen
	 */
	@XStreamOmitField private long outDuration;

	/**
	 * Value object with information about which frames numbers signal changes in the element
	 */
	@Getter @Setter public FrameData frameData;
	
	/**
	 * Value object with information about how the element animates when it is onscreen
	 */
	@Getter @Setter public AnimationData animationData;

	public AnimatedGFXElement(
			String themeName,
			String itemName, 
			String elementName, 
			String author, 
			String version, 
			CoOrd coOrd, 
			FrameData frameData, 
			AnimationData animationData) {

		super(themeName, itemName, elementName, author, version, coOrd); //send super the elements for a standard gfxElement
		this.frameData = frameData;
		this.animationData = animationData;
	}


	//have these getters so we don't have to call clasA.classB.getxoffset() in the Media manipulation classes
	public int getFirstHoldFrame() { return frameData.getFirstHoldFrame(); }
	public int getLastHoldFrame() { return frameData.getLastHoldFrame(); }
	public int getNumberOfFrames() { return frameData.getNumberOfFrames(); }

	@Override public boolean isReverse() { return animationData.isReverse(); }
	@Override public boolean isLoop() { return animationData.isLoop(); }
	@Override public boolean isNumbers() { return animationData.isNumbers(); }
	@Override public int getSpeed() { return animationData.getSpeed(); } //factor by which we speed up the out. This is a common trick for some reverse-out animations

	/**
	 * Calculates the InDuration required for an animated element by looking at the FirstHoldFrame that was specified in the XML
	 */
	@Override public long getInDuration() {
		inDuration = FrameRate.convertFrameToTime(getFirstHoldFrame() - 1);
		return inDuration;
	}

	/**
	 * Calculates the OutDuration required for an animated element by looking at values like numberOfFrames, isReverse, speedUp that were specified in the XML
	 */
	@Override public long getOutDuration() throws IllegalArgumentException {
		//if we have a reverse element, we need to use the inverse of the usual manner of getting duration AND know what speed we want the animate out to be
		if ( getSpeed() == 0 ) throw new IllegalArgumentException("Error getting OutDuration of " + super.getItemName() + " : speedup for a GFX element cannot be zero");
		if ( isReverse() ) { outDuration = FrameRate.convertFrameToTime( getNumberOfFrames() / getSpeed() ); }
		else { outDuration = FrameRate.convertFrameToTime(getNumberOfFrames() - getLastHoldFrame() + 1); }
		return outDuration;
	}

}
