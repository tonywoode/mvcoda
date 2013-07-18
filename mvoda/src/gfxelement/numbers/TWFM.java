package gfxelement.numbers;

import lombok.Getter;
import theme.Theme;

public class TWFM extends Numbers {
	
	@Getter public int firstHoldFrame = 9;
	@Getter public int lastHoldFrame = 63;
	@Getter public int numberOfFrames = 76;
	
	@Getter public int xOffsetSD = -680;
	@Getter public int yOffsetSD = 0;
	
	@Getter boolean reverse = false;
	@Getter boolean loop = false;
	@Getter int speed = 1;
	
	public TWFM(Theme theme, int num) {
		super(theme, num);
	}
	
	@Getter private String directory =  super.getDirectory() +  "Animated/Num" + getNum() + "/";
}