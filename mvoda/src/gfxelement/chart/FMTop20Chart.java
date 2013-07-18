package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class FMTop20Chart extends Chart {
	
	@Getter public int firstHoldFrame = 10;
	@Getter public int lastHoldFrame = 43;
	@Getter public int numberOfFrames = 52;
	
	@Getter public int xOffsetSD = 0;
	@Getter public int yOffsetSD = 0;
	
	@Getter boolean reverse = false;
	@Getter boolean loop = false;
	@Getter int speed = 1;
	
	public FMTop20Chart(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "FMTop20Chart";

	
	

}
