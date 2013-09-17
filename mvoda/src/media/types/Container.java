package media.types;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class Container {

	/**
	 * When passed a packet, will read that packet from the container
	 * @param packet
	 */
	public abstract int readNextPacket(Packet packet);

	/**
	 * Opens a media container (eg: MP4)
	 * @param fileUNC the path to the file holding the container
	 * @param read the type of container
	 * @param format the format of the container
	 */
	public abstract int open(String fileUNC, ContainerType read, ContainerFormat format);

	/**
	 * Gets the stream at an index position in a container
	 * @param i the index of the stream
	 * @return the strem
	 */
	public abstract Stream getStream(int i);

	/**
	 * Gets the number of streams in a container
	 * @return the numberof streams
	 */
	public abstract int getNumStreams();

	/**
	 * Gets the duration of the media the container reports
	 * @return the duration reported
	 */
	public abstract long getDuration();

	/**
	 * Closes the container
	 */
	public abstract void close();

	/**
	 * Gets the filesize of the file representing the container
	 * @return the filesize
	 */
	public abstract long getFileSize();

	/**
	 * Gets the bit-rate specified in the container
	 * @return the bit rate
	 */
	public abstract int getBitRate();

	/**
	 * Gets the starting timecode of the media as reported by the container
	 * @return the starting timecode
	 */
	public abstract long getStartTime();
}
	
	
	


