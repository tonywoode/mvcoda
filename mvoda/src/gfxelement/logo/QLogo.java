package gfxelement.logo;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class QLogo extends Logo {
	
	@Getter public int lastInFrame = 86;
	@Getter public int FirstOutFrame = 0;
	@Getter public int numberOfFrames = 86;

	//@Getter @Setter public long inDuration = 3440; ///The in time for this element TODO: in what framerate eh?
	//@Getter @Setter public long outDuration = 0; //Zero or below means you're supposed to reverse-animate it out
	
	public QLogo(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "QLogo";
//TODO: well this is the end of a long-drawn out way to get an element, ie: returning first the theme dir, BUT that's how you've done dirs on the disk!
}
