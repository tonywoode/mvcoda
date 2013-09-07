package media.types;

public abstract class Container {

	public abstract int readNextPacket(Packet packet);

	public abstract int open(String fileUNC, ContainerType read, ContainerFormat format);

	public abstract Stream getStream(int i);

	public abstract int getNumStreams();

	public abstract long getDuration();

	public abstract void close();

	public abstract long getFileSize();

	public abstract int getBitRate();

	public abstract long getStartTime();
}
	
	
	


