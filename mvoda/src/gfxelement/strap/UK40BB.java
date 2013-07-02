package gfxelement.strap;

import lombok.Getter;
import theme.Theme;

public class UK40BB extends Strap {


	public UK40BB(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "UK40BB";
}