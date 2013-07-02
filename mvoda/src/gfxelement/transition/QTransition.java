package gfxelement.transition;

import lombok.Getter;
import theme.Theme;

public class QTransition extends Transition {


	public QTransition(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QTransition";
}