package xstream;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Getter;
import lombok.Setter;

@XStreamAlias("AnimatedGFXElement")
public class AnimatedGFXElement extends GFXElement {

	@Getter @Setter public int firstHoldFrame;
	@Getter @Setter public int lastHoldFrame;
	@Getter @Setter public int numberOfFrames;
	
	@Getter @Setter boolean reverse;
	@Getter @Setter boolean loop;
	@Getter @Setter int speed;
	
}
