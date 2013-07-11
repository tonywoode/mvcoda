package gfxelement.numbers;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class TWFM1 extends Numbers {

	@Getter @Setter public long inDuration = 1000; ///The in time for this element is two seconds
	@Getter @Setter public long outDuration = 1000;
	
	public TWFM1(Theme theme, int num) {
		super(theme, num);
	}
	
	@Getter private String directory =  super.getDirectory() +  "Animated/Num" + getNum() + "/";
}