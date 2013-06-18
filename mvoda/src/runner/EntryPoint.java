package runner;

import media.MusicVideo;

public class EntryPoint {

	/**
	 * Main launcher for application, will do something nice
	 * @param args
	 */
	public static void main(String[] args) {
		MusicVideo test = new MusicVideo("C:/Users/Tony/CODE/MVODAInputs/Love/RihannaYouDaOne.avi");
		System.out.println("Here is the height of your video: " + test.getHeight());
		System.out.println("Here is the duration of your video (ms): " + test.getDuration());
		

	}

}
