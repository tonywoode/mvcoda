package media.xuggle.types;

import com.xuggle.xuggler.IStream;

import media.types.Stream;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public class StreamXuggle extends Stream {
	
	/**
	 * Holds a reference to a xuggle stream
	 */
	private IStream stream;
	
	/**
	 * Returns a xuggle stream
	 * @param stream a xuggle stream
	 */
	public StreamXuggle(IStream stream) { this.stream = stream;	}
	
	/**
	 * Returns the stream in the internal imp i.e.: a xuggle IStream and needs casting as such
	 */
	@Override public Object getInternalStream() { return stream; }
	
	
}
