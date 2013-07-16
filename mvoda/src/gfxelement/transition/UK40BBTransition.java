package gfxelement.transition;

import lombok.Getter;
import theme.Theme;

public class UK40BBTransition extends Transition {
	
	@Getter public int lastInFrame = 16;
	@Getter public int FirstOutFrame = 16;
	@Getter public int numberOfFrames = 16;
	
	@Getter public int xOffsetSD = 0;
	@Getter public int yOffsetSD = 0;

	public UK40BBTransition(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "UK40BBTransition";
}