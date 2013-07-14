package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class QChartBlack extends Chart {
	
	@Getter public int lastInFrame = 48;
	@Getter public int FirstOutFrame = 48;
	@Getter public int numberOfFrames = 48; //TODO: you just need to count the items in  this folder
	
	@Getter public int xOffsetSD = 480;
	@Getter public int yOffsetSD = 0;
	
	public QChartBlack(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QChartBlack";
}