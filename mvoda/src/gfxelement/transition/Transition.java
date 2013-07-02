package gfxelement.transition;

import lombok.Getter;
import theme.Theme;
import gfxelement.GFXElement;

public abstract class Transition extends GFXElement {

	@Getter private Theme theme;
	@Getter private String directory;
	
	public Transition(Theme theme) {
		this.theme = theme;
		directory = theme.getDirectory() + "Transition/";
	}
}