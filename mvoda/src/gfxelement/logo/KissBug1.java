package gfxelement.logo;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class KissBug1 extends Logo {
	
	@Getter public int lastInFrame = 29;
	@Getter public int FirstOutFrame = 44;
	@Getter public int numberOfFrames = 75;
	
	//@Getter @Setter public long inDuration = 2000; ///The in time for this element is two seconds
	//@Getter @Setter public long outDuration = 2000;

	
	public KissBug1(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "Kiss1Bug";
//TODO: well this is the end of a long-drawn out way to get an element, ie: returning first the theme dir, BUT that's how you've done dirs on the disk!


}
