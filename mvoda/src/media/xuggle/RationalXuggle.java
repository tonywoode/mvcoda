package media.xuggle;

import com.xuggle.xuggler.IRational;

import media.Rational;

public class RationalXuggle extends Rational {
	
	private IRational rational;

	public RationalXuggle(IRational rational) {
		this.rational = rational;
	}

}
