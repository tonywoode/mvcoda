package gfxelement.transition;

import lombok.Getter;
import theme.Theme;

public class UK40BBTransition extends Transition {


	public UK40BBTransition(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "UK40BBTransition";
}