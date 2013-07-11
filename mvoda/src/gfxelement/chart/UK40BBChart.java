package gfxelement.chart;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class UK40BBChart extends Chart {

	@Setter @Getter public long inDuration = 2000; ///TODO: there IS no in or out duration....
	@Setter @Getter public long outDuration = 2000;
	
	public UK40BBChart(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "UK40BB";

	
	

}
