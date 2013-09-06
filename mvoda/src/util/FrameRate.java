package util;

public class FrameRate {

	public static int getFrameRate() {
		return 25;
	}
	
	public static int timeCodeToFrameIndexConverter(long timeStamp) {
		int frame = (int) (timeStamp * FrameRate.getFrameRate() - 1);		
		return frame;
	}
}
