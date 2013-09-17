package themes;

import lombok.Getter;
import lombok.Setter;

/**
 * Frame data describes GFXElements' frame properties for elements that have an animate-in and/or animate-out section (which is most of them) - this means:
 * 		* Where does the animate-in end? - one frame before firstHoldFrame
 * 		* For how long does the "hold" section last in the seqeuence - from firstHoldFrame to lastHoldFrame
 * 		* Where does the animate-out begin? one frame after lastHoldFrame
 * @author tony
 *
 */
public class FrameData {
	
	/**
	 * At what frame does the element begin to hold onscreen in its sequence
	 */
	@Getter @Setter public int firstHoldFrame;
	
	/**
	 * Which is the last frame the element holds on screen in its seequence
	 */
	@Getter @Setter public int lastHoldFrame;
	
	/**
	 * How many frames in total comprise the element
	 */
	@Getter @Setter public int numberOfFrames; //TDOO: should be a calulated field
	
	public FrameData(int firstHoldFrame, int lastHoldFrame, int numberOfFrames) {
	this.firstHoldFrame = firstHoldFrame;
	this.lastHoldFrame = lastHoldFrame;
	this.numberOfFrames = numberOfFrames;
	}

}
