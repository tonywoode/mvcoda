package gfxelement.chart;

import theme.Theme;
import lombok.Getter;
import gfxelement.GFXElement;

public abstract class Chart extends GFXElement {

	@Getter private Theme theme;
	@Getter private String directory;


	public Chart(Theme theme) {
		this.theme = theme;
		directory = theme.getDirectory() + "Chart/";
	}
}
