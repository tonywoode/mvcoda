package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class UK40BBChart extends Chart {
	
	@Getter public int firstHoldFrame = -1;
	@Getter public int lastHoldFrame = -1;
	@Getter public int numberOfFrames = 1;
	
	@Getter public int xOffsetSD = 400;
	@Getter public int yOffsetSD = 0;
	
	public UK40BBChart(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "UK40BB";

	
	

}
