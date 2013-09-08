package media.xuggle.types;

import com.xuggle.xuggler.IStream;

import media.types.Stream;

public class StreamXuggle extends Stream {
	private IStream stream;
	
	public StreamXuggle(IStream stream) { this.stream = stream;	}
	
	@Override public Object getInternalStream() { return stream; }
	
	
}
