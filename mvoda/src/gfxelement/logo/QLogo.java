package gfxelement.logo;

import lombok.Getter;
import theme.Theme;

public class QLogo extends Logo {
	
	@Getter public int lastInFrame = 86;
	@Getter public int FirstOutFrame = 86;
	@Getter public int numberOfFrames = 86;
	
	public QLogo(Theme theme) {
		super(theme);
	}

	@Getter private String directory =  super.getDirectory() +  "QLogo";
}
