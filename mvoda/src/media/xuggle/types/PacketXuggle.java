package media.xuggle.types;

import com.xuggle.xuggler.IPacket;

import media.types.Packet;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public class PacketXuggle extends Packet {
	private IPacket packet;
	
	public PacketXuggle(IPacket packet) { this.packet = packet;	}

	@Override public int getSize() { return packet.getSize(); }

	@Override public int getStreamIndex() {	return packet.getStreamIndex();	}

	@Override public Object getInternalPacket() { return packet; }
}
