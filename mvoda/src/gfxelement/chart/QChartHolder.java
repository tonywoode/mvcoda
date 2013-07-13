package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class QChartHolder extends Chart {
	
	@Getter public int lastInFrame = 38;
	@Getter public int FirstOutFrame = 39;
	@Getter public int numberOfFrames = 132; //TODO: you just need to count the items in  this folder
	
	@Getter public int xOffsetSD = 120;
	@Getter public int yOffsetSD = -40;
	
	public QChartHolder(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QChartHolder";
}