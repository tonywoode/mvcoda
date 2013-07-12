package gfxelement.strap;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class UK40BB extends Strap {

	@Getter public int lastInFrame = 32; //TODO:actually its a looping one.....;
	@Getter public int FirstOutFrame = 0;
	@Getter public int numberOfFrames = 32;
	
	//@Getter @Setter public long inDuration = 2000; ///The in time for this element is two seconds
	//@Getter @Setter public long outDuration = 2000;
	
	public UK40BB(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "UK40BB";
}