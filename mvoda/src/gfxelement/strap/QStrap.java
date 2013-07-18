package gfxelement.strap;

import lombok.Getter;
import theme.Theme;

public class QStrap extends Strap {
	
	@Getter public int firstHoldFrame = 66;
	@Getter public int lastHoldFrame = 67;
	@Getter public int numberOfFrames = 131;
	
	@Getter public int xOffsetSD = -33;
	@Getter public int yOffsetSD = 230;
	
	@Getter boolean reverse = false;
	@Getter boolean loop = false;
	@Getter int speed = 1;
	
	

	public QStrap(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QStrap";
}