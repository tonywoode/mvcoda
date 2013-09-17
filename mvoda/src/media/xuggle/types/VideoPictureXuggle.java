package media.xuggle.types;

import com.xuggle.xuggler.IPixelFormat.Type;
import com.xuggle.xuggler.IVideoPicture;

import media.types.VideoPicture;

/**
 * Types for the decoupling adapter for the media framework of MV-CoDA
 * @author tony
 *
 */
public class VideoPictureXuggle extends VideoPicture {

	/**
	 * Holds a reference to a xuggle video picture
	 */
	IVideoPicture picture;
	
	/**
	 * returns a video picture from xuggles IVideoPicture
	 * @param picture
	 */
	public VideoPictureXuggle(IVideoPicture picture) { this.picture = picture; }
	
	/**
	 * Returns a picture in Xuggles internal IVideoPicture format and requires casting as such
	 */
	@Override public Object getInternalVideoPicture() {	return picture; }

	/**
	 * Returns true if the picture has been read by xuggle
	 * @return true when complete
	 */
	public boolean isComplete() { return picture.isComplete(); }

	/**
	 * Gets the timestamp of the video picture as reported by xuggle
	 * @return the timestamp
	 */
	public long getTimeStamp() { return picture.getTimeStamp();	}

	/**
	 * Gets the timestamp of the video picture as reported by xuggle in human readable format
	 * @return the timestamp in human readable format
	 */
	public String getFormattedTimeStamp() {	return picture.getFormattedTimeStamp();	}

	/**
	 * Gets the picture's pixel type as reported by xuggle
	 * @return
	 */
	public Type getPixelType() { return picture.getPixelType();	}

}
