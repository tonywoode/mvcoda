package gfxelement.strap;

import lombok.Getter;
import theme.Theme;

public class Periscope extends Strap {


	public Periscope(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "PeriscopeFrames";
}