package xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;

@XStreamAlias("AnimatedGFXElement")
public class AnimatedGFXElement extends GFXElement {

	@Getter @Setter public FrameData frameData;
	@Getter @Setter public AnimationData animationData;

	public AnimatedGFXElement(String itemName, 
			String rootPath, 
			String author, 
			String version, 
			CoOrd coOrd, 
			FrameData frameData, 
			AnimationData animationData) {
		
		super(itemName, rootPath, author, version, coOrd); //send super the elements for a standard gfxElement
		this.frameData = frameData;
		this.animationData = animationData;
	}


	//have these getters so we don't have to call clasA.classB.getxoffset() in the Media manipulation classes
	public int getFirstHoldFrame() { return frameData.getFirstHoldFrame(); }
	public int getLastHoldFrame() { return frameData.getLastHoldFrame(); }
	public int getNumberOfFrames() { return frameData.getNumberOfFrames(); }


	//don't need any of these anymore as we've abstracteed them to the two classes
	/*@Getter @Setter public int firstHoldFrame;
	@Getter @Setter public int lastHoldFrame;
	@Getter @Setter public int numberOfFrames;

	@Getter @Setter boolean reverse;
	@Getter @Setter boolean loop;
	@Getter @Setter int speed;*/

}
