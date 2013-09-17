package media.types;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class VideoPicture {

	/**
	 * gets an individual decoded video picture in the format of the underlying media imlpementation
	 * @return an object of the picture in the format of the media implementation
	 */
	public abstract Object getInternalVideoPicture();

}
