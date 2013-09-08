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

	IVideoPicture picture;
	
	public VideoPictureXuggle(IVideoPicture picture) {
		this.picture = picture;
	}
	
	@Override public Object getInternalVideoPicture() {	return picture; }

	public boolean isComplete() { return picture.isComplete(); }

	public long getTimeStamp() { return picture.getTimeStamp();	}

	public String getFormattedTimeStamp() {	return picture.getFormattedTimeStamp();	}

	public Type getPixelType() { return picture.getPixelType();	}

}
