package media.xuggle.types;

import com.xuggle.xuggler.IRational;

import media.types.Rational;

public class RationalXuggle extends Rational {
	
	private IRational rational;

	public RationalXuggle(IRational rational) {
		this.rational = rational;
	}

}
