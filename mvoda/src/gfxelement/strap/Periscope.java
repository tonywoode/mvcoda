package gfxelement.strap;

import lombok.Getter;
import theme.Theme;

public class Periscope extends Strap {
	
	@Getter public int firstHoldFrame = 20;
	@Getter public int lastHoldFrame = 70;
	@Getter public int numberOfFrames = 101;
	
	@Getter public int xOffsetSD = 0;
	@Getter public int yOffsetSD = 0;

	public Periscope(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "PeriscopeFrames";
}