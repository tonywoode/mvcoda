package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class FMTop20Chart extends Chart {
	
	@Getter public int lastInFrame = 9;
	@Getter public int FirstOutFrame = 43;
	@Getter public int numberOfFrames = 52;
	
	@Getter public int xOffsetSD = 0;
	@Getter public int yOffsetSD = 0;
	
	public FMTop20Chart(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "FMTop20Chart";

	
	

}
