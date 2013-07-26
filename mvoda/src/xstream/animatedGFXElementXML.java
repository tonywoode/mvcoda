package xstream;

import lombok.Getter;
import lombok.Setter;

public class animatedGFXElementXML extends gfxElementXML {

	@Getter @Setter public int firstHoldFrame;
	@Getter @Setter public int lastHoldFrame;
	@Getter @Setter public int numberOfFrames;
	
	@Getter @Setter boolean reverse;
	@Getter @Setter boolean loop;
	@Getter @Setter int speed;
	
}
