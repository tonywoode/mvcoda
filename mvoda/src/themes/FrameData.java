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
	
	@Getter @Setter public int firstHoldFrame;
	@Getter @Setter public int lastHoldFrame;
	@Getter @Setter public int numberOfFrames;
	
	public FrameData(int firstHoldFrame, int lastHoldFrame, int numberOfFrames) {
	this.firstHoldFrame = firstHoldFrame;
	this.lastHoldFrame = lastHoldFrame;
	this.numberOfFrames = numberOfFrames;
	}

}
