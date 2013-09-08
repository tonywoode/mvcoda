package util;

import lombok.Getter;
import lombok.Setter;

/**
 * Static methods and fields which help with framerates and framerate conversion. In particular holds the master framerate that will be used
 * when rendering, which is taken from the music video that is number one in the chart
 * @author tony
 *
 */
public class FrameRate {
	
	@Getter @Setter public static double frameRate;
	private static long timeBasis = 1000000; //we will be working in microseconds


	/**
	 * When passed a timestamp will return the number of frames that represents (according to the master framerate set in this class)
	 * @param timeStamp
	 * @return number of frames the timestamp represents (at the master framerate)
	 */
	public static int timeCodeToFrameIndexConverter(long timeStamp) {
		int frame = (int) (timeStamp * FrameRate.getFrameRate() - 1);		
		return frame;
	}

	/**
	 * When passed a number of frames, will return the time (in the time basis of the used timestamp, and relative to the master framerate) that represents
	 * @param frames
	 * @return how long that number of frames is in terms of the operation being performed
	 */
	public static long convertFrameToTime(long frames) {
		if (frames <= 0 ) { return 0; } //we don't throw an exception because MP4's can AND DO report frames of zero or less
		else {
			long result = frames * timeBasis; //the time basis
			result = result / Math.round(FrameRate.getFrameRate() ); //closest long to framerate
			return result;
		}
	}
}
