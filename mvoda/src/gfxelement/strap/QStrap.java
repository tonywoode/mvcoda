package gfxelement.strap;

import lombok.Getter;
import lombok.Setter;
import theme.Theme;

public class QStrap extends Strap {
	
	@Getter public int lastInFrame = 78;
	@Getter public int FirstOutFrame = 80;
	@Getter public int numberOfFrames = 132; //TODO: you just need to count the items in  this folder

	//@Getter public long inDuration = lastInFrame / 25 * 1000; ///The in time for this element is two seconds
	//@Getter public long outDuration = (132 - FirstOutFrame) /25 * 1000;
	
	
	
	
	public QStrap(Theme theme) {
		super(theme);
	}
	
	@Getter private String directory =  super.getDirectory() +  "QStrap";
}