package xstream;

import lombok.Getter;
import lombok.Setter;

public class CoOrd {
	
	@Getter @Setter public int xOffsetSD;
	@Getter @Setter public int yOffsetSD;
	
	public CoOrd(int x, int y) {
	xOffsetSD = x;
	yOffsetSD = y;
	
	}
	


}
