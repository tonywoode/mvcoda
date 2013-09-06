package media.xuggle;

import com.xuggle.xuggler.IPacket;

import media.Packet;

public class PacketXuggle extends Packet {
	private IPacket packet;
	
	public PacketXuggle(IPacket packet) {
		this.packet = packet;
		//assert(packet != null);
	}


}
