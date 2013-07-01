package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class ChartStrap extends Chart {

	
	public ChartStrap(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "ChartStrap";

}
