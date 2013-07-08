package gfxelement.transition;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class QTransition extends Transition {

	@Getter @Setter public long inTime = 2000; ///The in time for this element is two seconds
	@Getter @Setter public long outTime = 2000;
	
	public QTransition(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QTransition";
}