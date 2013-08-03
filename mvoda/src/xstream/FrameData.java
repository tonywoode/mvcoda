package xstream;

import lombok.Getter;
import lombok.Setter;

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
