package media;

/**
 * Signals that the media implementnation is not going to be able to process inputs
 * @author tony
 *
 */
public class MediaOpenException extends Exception {

	public MediaOpenException() {}

	public MediaOpenException(String text) { super(text); }

	public MediaOpenException(Throwable cause) { super(cause); }

	public MediaOpenException(String text, Throwable cause) { super (text, cause); }

}
