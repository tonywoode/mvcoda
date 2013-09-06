package media;

import media.xuggle.ContainerXuggle;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainer.Type;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;

public abstract class Container {

	public abstract int readNextPacket(IPacket packet);

 public static IContainer make() { return ContainerXuggle.make(); }

	public int open(String fileUNC, Type read, IContainerFormat format) {
		// TODO Auto-generated method stub
		return 0;
	}

	public abstract IStream getStream(int i);

	public abstract int getNumStreams();

	public abstract long getDuration();

	public abstract void close();

	public abstract long getFileSize();

	public abstract int getBitRate();

	public abstract long getStartTime();


}
	
	
	


