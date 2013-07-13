package gfxelement.strap;

import lombok.Getter;
import theme.Theme;

public class QStrap extends Strap {
	
	@Getter public int lastInFrame = 66;
	@Getter public int FirstOutFrame = 68;
	@Getter public int numberOfFrames = 131;
	
	@Getter public int xOffsetSD = 200;
	@Getter public int yOffsetSD = 200;
	
	

	public QStrap(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QStrap";
}