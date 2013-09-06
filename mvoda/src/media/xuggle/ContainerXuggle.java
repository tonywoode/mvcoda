package media.xuggle;

import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainer.Type;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IStream;

import media.Container;

public class ContainerXuggle extends Container {
	private IContainer container;

	public ContainerXuggle(IContainer container) {
		this.container = container;
	}

	@Override
	public int readNextPacket(IPacket packet) {
		return container.readNextPacket(packet);
	}

	
	public static IContainer make() {
		return IContainer.make();
	}

	@Override
	public int open(String fileUNC, IContainer.Type read, IContainerFormat format) {
		return container.open(fileUNC, read, format);
	}

	@Override
	public IStream getStream(int i) {
		return container.getStream(i);
	}

	@Override
	public int getNumStreams() {
		return container.getNumStreams();
	}

	@Override
	public long getDuration() {
		return container.getDuration();
	}

	@Override
	public void close() {
		container.close();
	}

	@Override
	public long getFileSize() {
		return container.getFileSize();
	}

	@Override
	public int getBitRate() {
		return container.getBitRate();
	}

	@Override
	public long getStartTime() {
		return container.getStartTime();
	}

}
