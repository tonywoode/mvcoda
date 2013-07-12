package gfxelement.numbers;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class TWFM extends Numbers {
	
	@Getter public int lastInFrame = 8;
	@Getter public int FirstOutFrame = 64;
	@Getter public int numberOfFrames = 76;

	//@Getter @Setter public long inDuration = 1000; ///The in time for this element is two seconds
	//@Getter @Setter public long outDuration = 1000;
	
	public TWFM(Theme theme, int num) {
		super(theme, num);
	}
	
	@Getter private String directory =  super.getDirectory() +  "Animated/Num" + getNum() + "/";
}