package media.types;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class ContainerType {

	/**
	 * Gets the type of the container as an oject of the underlying implementations type
	 * @return the object representing the container type
	 */
	public abstract Object getContainerType();

}
