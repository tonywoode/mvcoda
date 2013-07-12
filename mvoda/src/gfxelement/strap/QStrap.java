package gfxelement.strap;

import lombok.Getter;
import theme.Theme;

public class QStrap extends Strap {
	
	@Getter public int lastInFrame = 78;
	@Getter public int FirstOutFrame = 80;
	@Getter public int numberOfFrames = 132; //TODO: you just need to count the items in  this folder
	
	public QStrap(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QStrap";
}