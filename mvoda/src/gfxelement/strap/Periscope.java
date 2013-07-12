package gfxelement.strap;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class Periscope extends Strap {
	
	@Getter public int lastInFrame = 20;
	@Getter public int FirstOutFrame = 71;
	@Getter public int numberOfFrames = 101;

	//@Getter @Setter public long inDuration = 2000; ///The in time for this element is two seconds
	//@Getter @Setter public long outDuration = 2000;
	
	public Periscope(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "PeriscopeFrames";
}