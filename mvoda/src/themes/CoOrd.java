package themes;

import lombok.Getter;
import lombok.Setter;

/**
 * Means of simplifying specifying coordinates
 * @author tony
 *
 */
public class CoOrd {
	
	@Getter @Setter public int xOffsetSD;
	@Getter @Setter public int yOffsetSD;
	
	public CoOrd(int x, int y) { xOffsetSD = x;	yOffsetSD = y; }
}
