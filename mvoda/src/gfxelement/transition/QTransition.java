package gfxelement.transition;

import lombok.Getter;
import theme.Theme;

public class QTransition extends Transition {

	@Getter public long inTime = 2000; ///The in time for this element is two seconds
	@Getter public long outTime = 2000;
	
	public QTransition(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QTransition";
}