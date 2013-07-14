package gfxelement.numbers;

import lombok.Getter;
import theme.Theme;

public class QRedBlock extends Numbers {
	
	@Getter public int lastInFrame = 53;
	@Getter public int FirstOutFrame = 55;
	@Getter public int numberOfFrames = 78;
	
	@Getter public int xOffsetSD = 24;
	@Getter public int yOffsetSD = 194;
	
	public QRedBlock(Theme theme, int num) {
		super(theme, num);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QRedBlockLarger" + "/";
}