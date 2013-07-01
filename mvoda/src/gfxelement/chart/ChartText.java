package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class ChartText extends Chart {

	
	public ChartText(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "ChartText";

}
