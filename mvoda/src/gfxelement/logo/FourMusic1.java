package gfxelement.logo;


import lombok.Getter;
import theme.Theme;

public class FourMusic1 extends Logo {
	
	@Getter public int firstHoldFrame = 51;
	@Getter public int lastHoldFrame = 79;
	@Getter public int numberOfFrames = 126;
	
	@Getter public int xOffsetSD = 0;
	@Getter public int yOffsetSD = 0;
	
	public FourMusic1(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "4M1BugFrames";
//TODO: well this is the end of a long-drawn out way to get an element, ie: returning first the theme dir, BUT that's how you've done dirs on the disk!
	
	
}
