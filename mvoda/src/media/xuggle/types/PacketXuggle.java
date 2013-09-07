package media.xuggle.types;

import com.xuggle.xuggler.IPacket;

import media.types.Packet;

public class PacketXuggle extends Packet {
	private IPacket packet;
	
	public PacketXuggle(IPacket packet) {
		this.packet = packet;
		//assert(packet != null);
	}

	@Override
	public int getSize() {
		return packet.getSize();
	}

	@Override
	public int getStreamIndex() {
		return packet.getStreamIndex();
	}

	@Override
	public Object getInternalPacket() {
		return packet;
	}
}
