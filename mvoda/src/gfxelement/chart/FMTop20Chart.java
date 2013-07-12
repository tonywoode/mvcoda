package gfxelement.chart;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class FMTop20Chart extends Chart {
	
	@Getter public int lastInFrame = 9;
	@Getter public int FirstOutFrame = 43;
	@Getter public int numberOfFrames = 52;

	//@Setter @Getter public long inDuration = 2000; ///The in time for this element is two seconds
	//@Setter @Getter public long outDuration = 2000;
	
	public FMTop20Chart(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "FMTop20Chart";

	
	

}
