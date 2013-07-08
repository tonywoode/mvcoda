package gfxelement.chart;

import lombok.Getter;
import theme.Theme;

public class FMTop20Chart extends Chart {

	@Getter public long inTime = 2000; ///The in time for this element is two seconds
	@Getter public long outTime = 2000;
	
	public FMTop20Chart(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "FMTop20Chart";
	

}
