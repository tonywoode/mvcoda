package media.types;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class Stream {

	/**
	 * Get the stream in the format of the media implementation
	 * @return on object containing the stream in the format of the media implementation
	 */
	public abstract Object getInternalStream();
	
}
