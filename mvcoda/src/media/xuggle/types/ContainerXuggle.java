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
	
	/**
	 * holds a reference to the xuggle container
	 */
	private IContainer container;

	/**
	 * returns the xuggle representation of the container
	 * @param container the xuggle container
	 */
	public ContainerXuggle(IContainer container) { this.container = container; }

	/**
	 * reads the next packet from the xuggle impo container
	 */
	@Override public int readNextPacket(Packet packet) { return container.readNextPacket((IPacket)packet.getInternalPacket()); }

	/**
	 * Makes an IContainer from the xuggle factory 
	 * @return
	 */
	public static IContainer make() { return IContainer.make(); }

	/**
	 * Opens a container as specified by thefilepath, container type and container format
	 */
	@Override public int open(String fileUNC, ContainerType read, ContainerFormat format) {
		return container.open(fileUNC, (IContainer.Type)read.getContainerType(), (IContainerFormat)format.getInternalFormat());
	}

	/**
	 * Gets the stream at index i from the xuggle container
	 */
	@Override public Stream getStream(int i) { return new StreamXuggle(container.getStream(i));	}

	/**
	 * gets the number of streams reported by xuggle for the container
	 */
	@Override public int getNumStreams() { return container.getNumStreams(); }

	/**
	 * return the duration of the container reported by xuggle
	 */
	@Override public long getDuration() { return container.getDuration(); }

	/**
	 * closes the container
	 */
	@Override public void close() { container.close(); }

	/**
	 * Gets the filesize of the file representing the container from xuggle
	 */
	@Override public long getFileSize() { return container.getFileSize(); }

	/**
	 * Gets the bitrate rerported by the container from xuggle
	 */
	@Override public int getBitRate() { return container.getBitRate(); }

	/**
	 * Gets the starting timestamp as reported from the container by xuggle
	 */
	@Override public long getStartTime() { return container.getStartTime();	}

}
