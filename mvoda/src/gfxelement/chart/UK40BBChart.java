package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class UK40BBChart extends Chart {
	
	@Getter public int lastInFrame = 1;
	@Getter public int FirstOutFrame = 1;
	@Getter public int numberOfFrames = 1;
	
	public UK40BBChart(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "UK40BB";

	
	

}
