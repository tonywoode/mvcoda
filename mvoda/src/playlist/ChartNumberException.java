package playlist;

/**
 * Intended to signal problems with chart numbers. Important otherwise these will be rendered onscreen and may not be noticed
 * @author tony
 *
 */
public class ChartNumberException extends Exception {

	public ChartNumberException() {}

	public ChartNumberException(String text) { super(text); }

	public ChartNumberException(Throwable cause) { super(cause); }

	public ChartNumberException(String text, Throwable cause) {	super (text, cause); }


}
