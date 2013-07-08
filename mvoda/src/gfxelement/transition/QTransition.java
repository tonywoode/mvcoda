package gfxelement.transition;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class QTransition extends Transition {

	@Getter @Setter public long inDuration = 2000; ///The in time for this element is two seconds
	@Getter @Setter public long outDuration = 2000;
	
	public QTransition(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QTransition";
}