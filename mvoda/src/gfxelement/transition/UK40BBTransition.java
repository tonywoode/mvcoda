package gfxelement.transition;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class UK40BBTransition extends Transition {
	
	@Getter public int lastInFrame = 16;
	@Getter public int FirstOutFrame = 0;
	@Getter public int numberOfFrames = 16;

	//@Getter @Setter public long inDuration = 2000; ///The in time for this element is two seconds TODO//if you don't know the framerate, how do you know the duration?
	//@Getter @Setter public long outDuration = 2000;

	public UK40BBTransition(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "UK40BBTransition";
}