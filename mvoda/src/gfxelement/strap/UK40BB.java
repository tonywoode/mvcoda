package gfxelement.strap;

import lombok.Getter;
import theme.Theme;

public class UK40BB extends Strap {


	@Getter public long inTime = 2000; ///The in time for this element is two seconds
	@Getter public long outTime = 2000;
	
	public UK40BB(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "UK40BB";
}