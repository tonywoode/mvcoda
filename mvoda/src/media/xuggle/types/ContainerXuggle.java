package media.xuggle.types;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IPacket;

import media.types.Container;
import media.types.ContainerFormat;
import media.types.ContainerType;
import media.types.Packet;
import media.types.Stream;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public class ContainerXuggle extends Container {
	private IContainer container;

	public ContainerXuggle(IContainer container) { this.container = container; }

	@Override public int readNextPacket(Packet packet) { return container.readNextPacket((IPacket)packet.getInternalPacket()); }

	
	public static IContainer make() { return IContainer.make(); }

	@Override public int open(String fileUNC, ContainerType read, ContainerFormat format) {
		return container.open(fileUNC, (IContainer.Type)read.getContainerType(), (IContainerFormat)format.getInternalFormat());
	}

	@Override public Stream getStream(int i) { return new StreamXuggle(container.getStream(i));	}

	@Override public int getNumStreams() { return container.getNumStreams(); }

	@Override public long getDuration() { return container.getDuration(); }

	@Override public void close() { container.close(); }

	@Override public long getFileSize() { return container.getFileSize(); }

	@Override public int getBitRate() { return container.getBitRate(); }

	@Override public long getStartTime() { return container.getStartTime();	}

}
