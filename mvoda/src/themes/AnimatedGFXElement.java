package themes;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import lombok.Getter;
import lombok.Setter;

@XStreamAlias("AnimatedGFXElement")
public class AnimatedGFXElement extends GFXElement {
	
	
	 @XStreamOmitField private long inDuration;
	 @XStreamOmitField private long outDuration;

	@Getter @Setter public FrameData frameData;
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

	//TODO:pay attention to these two overrides when you doc
	@Override public long getInDuration() {	
		inDuration = convertFrameToTime(getFirstHoldFrame() - 1);
		return inDuration;
	}
	
	@Override
	public long getOutDuration() {
		//if we have a reverse element, we need to use the inverse of the usual manner of getting duration AND know what speed we want the animate out to be
		if (isReverse()) { outDuration = ( convertFrameToTime(getNumberOfFrames() - (getNumberOfFrames() - getLastHoldFrame() + 1)) / getSpeed() ); }
		else { outDuration = convertFrameToTime(getNumberOfFrames() - getLastHoldFrame() + 1); }//TODO: make sure framerate is never going to be zero
		return outDuration;
	}

}
