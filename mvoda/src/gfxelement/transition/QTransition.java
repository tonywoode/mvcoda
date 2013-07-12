package gfxelement.transition;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class QTransition extends Transition {
	
	@Getter public int lastInFrame = 32;
	@Getter public int FirstOutFrame = 0;
	@Getter public int numberOfFrames = 32;

	@Getter @Setter public long inDuration = 2000; ///The in time for this element is two seconds
	@Getter @Setter public long outDuration = 2000;
	
	public QTransition(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QTransition";
}