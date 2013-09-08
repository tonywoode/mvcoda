package media.types;


/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public abstract class Packet {

	public abstract int getSize();

	public abstract int getStreamIndex();

	public abstract Object getInternalPacket();

}
