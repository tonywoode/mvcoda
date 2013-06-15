package runner;

import media.MusicVideo;

public class EntryPoint {

	/**
	 * Main launcher for application
	 * @param args
	 */
	public static void main(String[] args) {
		MusicVideo test = new MusicVideo("C:/Users/Tony/CODE/MVODAInputs/Love/RihannaYouDaOne.avi");
		System.out.println("Here is the height of your video: " + test.getHeight());

	}

}
