package media.xuggle.types;

import com.xuggle.xuggler.IPacket;

import media.types.Packet;

public class PacketXuggle extends Packet {
	private IPacket packet;
	
	public PacketXuggle(IPacket packet) {
		this.packet = packet;
		//assert(packet != null);
	}


}
