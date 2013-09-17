package media.types;


/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class Packet {

	/**
	 * Get the size of a packet
	 * @return the size of a packet
	 */
	public abstract int getSize();

	/**
	 * Gets the stream index the packet has originated from
	 * @return the stream index of the packet
	 */
	public abstract int getStreamIndex();

	/**
	 * Returns the packet in the format of the media framework
	 * @return an object representing the packet in the format of the media framework
	 */
	public abstract Object getInternalPacket();

}
