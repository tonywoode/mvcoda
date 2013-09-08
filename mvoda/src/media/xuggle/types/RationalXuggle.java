package media.xuggle.types;

import com.xuggle.xuggler.IRational;

import media.types.Rational;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public class RationalXuggle extends Rational {
	
	private IRational rational;

	public RationalXuggle(IRational rational) {
		this.rational = rational;
	}

}
