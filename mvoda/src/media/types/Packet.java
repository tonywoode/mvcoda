package media.types;


public abstract class Packet {

	public abstract int getSize();

	public abstract int getStreamIndex();

	public abstract Object getInternalPacket();

}
