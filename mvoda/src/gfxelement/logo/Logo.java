package gfxelement.logo;

import theme.Theme;
import lombok.Getter;
import gfxelement.GFXElement;

public abstract class Logo extends GFXElement {

	@Getter private Theme theme;
	@Getter private String directory;


	public Logo(Theme theme) {
		this.theme = theme;
		directory = theme.getDirectory() + "Logo";
	}
}
