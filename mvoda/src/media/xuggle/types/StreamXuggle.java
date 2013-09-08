package media.xuggle.types;

import com.xuggle.xuggler.IStream;

import media.types.Stream;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public class StreamXuggle extends Stream {
	private IStream stream;
	
	public StreamXuggle(IStream stream) { this.stream = stream;	}
	
	@Override public Object getInternalStream() { return stream; }
	
	
}
