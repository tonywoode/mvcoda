package gfxelement.strap;

import lombok.Getter;
import theme.Theme;

public class UK40BB extends Strap {

	@Getter public int lastInFrame = 25; //TODO:actually its a looping one.....;
	@Getter public int FirstOutFrame = 151;
	@Getter public int numberOfFrames = 176;
	
	@Getter public int xOffsetSD = 0;
	@Getter public int yOffsetSD = 0;
	
	public UK40BB(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "UK40BB";
}