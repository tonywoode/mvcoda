package gfxelement.numbers;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class UKBB40NumHolder extends Numbers {
	
	@Getter public int lastInFrame = 16;
	@Getter public int FirstOutFrame = 0;
	@Getter public int numberOfFrames = 176;

	//@Getter @Setter public long inDuration = 1000; ///The in time for this element is two seconds
	//@Getter @Setter public long outDuration = 1000;
	
	public UKBB40NumHolder(Theme theme, int num) {
		super(theme, num);
	}
	
	@Getter private String directory =  super.getDirectory() +  "BBNumHolder" + "/";
}