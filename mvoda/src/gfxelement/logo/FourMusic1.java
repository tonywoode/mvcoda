package gfxelement.logo;


import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class FourMusic1 extends Logo {

	@Getter @Setter public long inDuration = 2000; ///The in time for this element is two seconds
	@Getter @Setter public long outDuration = 3000;
	
	public FourMusic1(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "4MLogoFrames";
//TODO: well this is the end of a long-drawn out way to get an element, ie: returning first the theme dir, BUT that's how you've done dirs on the disk!
	
	
}
