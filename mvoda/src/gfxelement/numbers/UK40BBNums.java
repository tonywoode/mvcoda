package gfxelement.numbers;

import lombok.Getter;
import theme.Theme;

public class UK40BBNums extends Numbers {
	
	@Getter public int lastInFrame = 24;
	@Getter public int FirstOutFrame = 151;
	@Getter public int numberOfFrames = 176;
	
	@Getter public int xOffsetSD = 0;
	@Getter public int yOffsetSD = 0;
	
	public UK40BBNums(Theme theme, int num) {
		super(theme, num);
	}
	
	@Getter private String directory =  super.getDirectory() + getNum() + "UK40BBNums" + "/";
}