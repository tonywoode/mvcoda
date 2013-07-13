package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class QChartHolder extends Chart {
	
	@Getter public int lastInFrame = 78;
	@Getter public int FirstOutFrame = 80;
	@Getter public int numberOfFrames = 132; //TODO: you just need to count the items in  this folder
	
	@Getter public int xOffsetSD = 0;
	@Getter public int yOffsetSD = 0;
	
	public QChartHolder(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QChartHolder";
}