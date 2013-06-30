package gfxelement.logo;

import lombok.Getter;
import theme.Theme;

public class FourMusic1 extends Logo {

	
	public FourMusic1(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "4MLogoFrames";
//TODO: well this is the end of a long-drawn out way to get an element, ie: returning first the theme dir, BUT that's how you've done dirs on the disk!
}
