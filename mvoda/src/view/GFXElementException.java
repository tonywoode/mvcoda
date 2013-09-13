package view;

/**
 * Signals problems with GFX Elements that would cause them not to render, or worse render incorrectly, which may not be noticed...
 * @author tony
 *
 */
public class GFXElementException extends Exception {

	public GFXElementException() {}

	public GFXElementException(String text) { super(text); }

	public GFXElementException(Throwable cause)	{ super(cause); }

	public GFXElementException(String text, Throwable cause) { super (text, cause); }


}
