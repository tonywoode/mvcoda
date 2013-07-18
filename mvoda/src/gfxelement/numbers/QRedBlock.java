package gfxelement.numbers;

import lombok.Getter;
import theme.Theme;

public class QRedBlock extends Numbers {
	
	@Getter public int firstHoldFrame = 54;
	@Getter public int lastHoldFrame = 55;
	@Getter public int numberOfFrames = 78;
	
	@Getter public int xOffsetSD = 24;
	@Getter public int yOffsetSD = 194;
	
	@Getter boolean reverse = false;
	@Getter boolean loop = false;
	@Getter int speed = 1;
	
	public QRedBlock(Theme theme, int num) {
		super(theme, num);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QRedBlockLarger" + "/";
}