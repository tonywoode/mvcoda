package gfxelement.numbers;

import lombok.Getter;
import theme.Theme;

public class UKBB40NumHolder extends Numbers {
	
	@Getter public int lastInFrame = 16;
	@Getter public int FirstOutFrame = 0;
	@Getter public int numberOfFrames = 176;
	
	public UKBB40NumHolder(Theme theme, int num) {
		super(theme, num);
	}
	
	@Getter private String directory =  super.getDirectory() +  "BBNumHolder" + "/";
}