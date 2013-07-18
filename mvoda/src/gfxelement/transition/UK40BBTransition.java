package gfxelement.transition;

import lombok.Getter;
import theme.Theme;

public class UK40BBTransition extends Transition {
	
	@Getter public int firstHoldFrame = -1;
	@Getter public int lastHoldFrame = -1;
	@Getter public int numberOfFrames = 16;
	
	@Getter public int xOffsetSD = 0;
	@Getter public int yOffsetSD = 0;
	
	@Getter boolean reverse = false;
	@Getter boolean loop = false;
	@Getter int speed = 1;

	public UK40BBTransition(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "UK40BBTransition";
}