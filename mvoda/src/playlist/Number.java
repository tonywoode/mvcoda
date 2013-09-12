package playlist;

import lombok.Getter;
import view.ChartNumberException;

/**
 * Sets the current chart number for live renders ie: which postition are we at in the music video chart
 * @author tony
 *
 */
public class Number {
	
	@Getter private static int number = 10; //better a number between 1 and 20 than starting at zero, since zero has no corresponding fileset
	
	public static void setNumber(int number) throws ChartNumberException {
		if (number >= 1 ) {	Number.number = number;	}
		else throw new ChartNumberException("chart number set to less than 1");
	}
	//TODO: must not be more than the actual element numbers when they are pre-rendered!
}
