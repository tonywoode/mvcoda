package themes;

import lombok.Getter;
import lombok.Setter;

/**
 * Means of simplifying specifying coordinates
 * @author tony
 *
 */
public class CoOrd {
	
	/**
	 * X position onscreen in a standard definition picture
	 */
	@Getter @Setter public int xOffsetSD;
	
	/**
	 * Y position onscreen in a standard definition picture
	 */
	@Getter @Setter public int yOffsetSD;
	
	public CoOrd(int x, int y) { xOffsetSD = x;	yOffsetSD = y; }
}
