package xstream;

import lombok.Getter;
import lombok.Setter;

public class AnimatedGFXElement extends GFXElement {

	@Getter @Setter public int firstHoldFrame;
	@Getter @Setter public int lastHoldFrame;
	@Getter @Setter public int numberOfFrames;
	
	@Getter @Setter boolean reverse;
	@Getter @Setter boolean loop;
	@Getter @Setter int speed;
	
}
