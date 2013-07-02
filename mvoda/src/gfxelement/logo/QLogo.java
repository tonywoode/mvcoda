package gfxelement.logo;

import lombok.Getter;
import theme.Theme;

public class QLogo extends Logo {

	
	public QLogo(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "QLogo";
//TODO: well this is the end of a long-drawn out way to get an element, ie: returning first the theme dir, BUT that's how you've done dirs on the disk!
}
