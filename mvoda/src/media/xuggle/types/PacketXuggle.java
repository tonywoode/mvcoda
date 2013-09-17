package media.xuggle.types;

import com.xuggle.xuggler.IPacket;

import media.types.Packet;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public class PacketXuggle extends Packet {
	
	/**
	 * Holds a reference to the Xuggle packet
	 */
	private IPacket packet;
	
	/**
	 * returns the xuggle media packet from a stream
	 * @param packet the xuggle media packet
	 */
	public PacketXuggle(IPacket packet) { this.packet = packet;	}

	/**
	 * Gets the size of the packet as reported by xuggle
	 */
	@Override public int getSize() { return packet.getSize(); }

	/**
	 * Gets the stream index of the packet as reported by xuggle
	 */
	@Override public int getStreamIndex() {	return packet.getStreamIndex();	}

	/**
	 * Returns the packet as the Xuggle internal packet type, requires casting to IPacket
	 */
	@Override public Object getInternalPacket() { return packet; }
}
