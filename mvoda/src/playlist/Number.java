package playlist;

import lombok.Getter;
import lombok.Setter;

public class Number {
	
	@Getter @Setter private static int number = 10; //better a number between 1 and 20 than starting at zero, since zero has no corresponding fileset
	//TODO: must be >=1
	//TODO: must not be more than the actual element numbers when they are pre-rendered!
}
