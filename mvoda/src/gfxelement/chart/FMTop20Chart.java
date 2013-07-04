package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class FMTop20Chart extends Chart {

	
	public FMTop20Chart(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "FMTop20Chart";
	

}
