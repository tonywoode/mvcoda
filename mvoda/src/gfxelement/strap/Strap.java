package gfxelement.strap;

import lombok.Getter;
import theme.Theme;
import gfxelement.GFXElement;

public abstract class Strap extends GFXElement {

	@Getter private Theme theme;
	@Getter private String directory;
	
	public Strap(Theme theme) {
		this.theme = theme;
		directory = theme.getDirectory() + "Strap/";
	}
}