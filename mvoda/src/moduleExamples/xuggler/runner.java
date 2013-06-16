package moduleExamples.xuggler;

import moduleExamples.xuggler.DecodeAndPlayAudioAndVideo;

public class runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] file = new String[1];
		file[0] = "../../../MVODAInputs/Love/BrunoMarsJustTheWay.avi";
		DecodeAndPlayAudioAndVideo.main(file);

	}

}
