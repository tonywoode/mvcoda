package gfxelement.numbers;

import lombok.Getter;
import theme.Theme;

public class TWFM extends Numbers {
	
	@Getter public int lastInFrame = 8;
	@Getter public int FirstOutFrame = 64;
	@Getter public int numberOfFrames = 76;
	
	@Getter public int xOffsetSD = -680;
	@Getter public int yOffsetSD = 0;
	
	public TWFM(Theme theme, int num) {
		super(theme, num);
	}
	
	@Getter private String directory =  super.getDirectory() +  "Animated/Num" + getNum() + "/";
}